package com.zebone.nhis.pro.zsba.ex.service;


import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubReturnVo;
import com.zebone.nhis.common.module.bl.BlIpDt;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.module.ex.nis.ns.ExOrderOcc;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ex.nis.ns.support.OrderCheckSortByOrdUtil;
import com.zebone.nhis.ex.pub.service.CreateExecListService;
import com.zebone.nhis.ex.pub.support.ExSysParamUtil;
import com.zebone.nhis.ex.pub.vo.GenerateExLisOrdVo;
import com.zebone.nhis.pro.zsba.ex.dao.OrderExecListBaMapper;
import com.zebone.nhis.pro.zsba.ex.vo.ExCgBaVo;
import com.zebone.nhis.pro.zsba.ex.vo.FreqTimeBaVo;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.NHISUUID;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * 医嘱执行单处理服务
 *
 * @author yangxue
 */
@Service
public class OrderExecListBaService {
    @Resource
    private OrderExecListBaMapper orderExListMapper;
    @Resource
    private CreateExecListService createExecListService;
    @Resource
    private IpCgPubService ipCgPubService;

    /**
     * 处理执行单
     *
     * @param isPk 是否 根据主键生成
     * @throws ParseException
     */
    public String processExecList(List<OrderCheckVO> check_obj_list, List<String> check_list, List<String> stop_list, List<OrderCheckVO> stop_obj_list, List<String> erase_list, Map<String, Object> paramMap, boolean isPk) throws ParseException {
        //生成执行单
        String msg = "";
        if (check_list != null && check_list.size() > 0) {
            if (isPk) {
                paramMap.put("pkOrds", check_list);
            } else {
                paramMap.put("ordsnParents", check_list);
            }
            msg = createExecListService.createExecList(paramMap);
            //包含首日次数的，追加或删除执行单数量
            createFirstExecList(check_obj_list);
        }
        if (stop_list != null && stop_list.size() > 0) {
            if (isPk) {
                paramMap.put("pkOrds", stop_list);
            } else {
                paramMap.put("ordsnParents", stop_list);
            }
            //createExecListService.createExecList(paramMap);
            //删除未执行执行单
            this.deleteExList(paramMap, isPk);
            //取消已发药的执行单
            this.cancelExList(paramMap, isPk);
        }
        //作废需要核对的情况
        if (erase_list != null && erase_list.size() > 0) {
            paramMap.put("dateCanc", new Date());
            paramMap.put("pkOrds", erase_list);
            if ("1".equals(ApplicationUtils.getSysparam("CN0028", false))) {
                orderExListMapper.cancelExecListByPkOrd(paramMap);
            }
        }
        return msg;
    }

    /**
     * 停止核对医嘱时生成医嘱停止时间之前未生成的执行单
     *
     * @param stop_list
     * @param paramMap
     */
    public void createCheckStopExlist(List<String> stop_list, Map<String, Object> paramMap) {
        paramMap.put("pkOrds", stop_list);
        createExecListService.createExecList(paramMap);
    }

    /**
     * 处理停止医嘱
     */
    public String processStopOrd(List<OrderCheckVO> stop_obj_list, Map<String, Object> paramMap, User user) {
        if (stop_obj_list == null || stop_obj_list.size() <= 0)
            return null;
        StringBuilder msg = new StringBuilder("");
        StringBuilder error = new StringBuilder("");
        //无末次医嘱删除执行单的sql参数集合
        List<String> pk_nolist = new ArrayList<String>();
        //有末次医嘱删除执行单的sql集合
        List<String> update_list = new ArrayList<String>();
        //有末次医嘱可删除执行单的sql集合
        List<String> del_list = new ArrayList<String>();
        //本病区已执行的非药品医嘱执行单列表
        List<ExCgBaVo> exlist = new ArrayList<ExCgBaVo>();
        //非本病区已执行的非药品医嘱执行单列表
        List<String> update_other_list = new ArrayList<String>();
        //当前时间
        String currTime = DateUtils.getDefaultDateFormat().format(new Date());
        //按末次计算的可删除的执行单sql
        StringBuilder del_sql = new StringBuilder(" delete from ex_order_occ  where (ex_order_occ.eu_status = '0' or ex_order_occ.eu_status = '9') ");
        del_sql.append(" and (ex_order_occ.pk_pdapdt is null or ex_order_occ.pk_pdapdt ='') and exists (select pk_ord ");
        del_sql.append(" from cn_order where cn_order.pk_cnord = ex_order_occ.pk_cnord ");
        StringBuilder update_sql = new StringBuilder(" update ex_order_occ  set ex_order_occ.eu_status = '9',ex_order_occ.flag_canc = '1',");
        update_sql.append("ex_order_occ.date_canc = to_date('");
        update_sql.append(currTime);
        update_sql.append("','YYYYMMDDHH24MISS'),ex_order_occ.pk_dept_canc = '");
        update_sql.append(user.getPkDept());
        update_sql.append("',ex_order_occ.pk_emp_canc  = '");
        update_sql.append(user.getPkEmp());
        update_sql.append("',ex_order_occ.name_emp_canc  = '");
        update_sql.append(user.getNameEmp());
        update_sql.append("'   where exists (select pk_cnord from cn_order  where cn_order.pk_cnord = ex_order_occ.pk_cnord and ex_order_occ.flag_canc = '0' ");
        update_sql.append(" and cn_order.eu_always = '0'  and ex_order_occ.eu_status != '9' and cn_order.flag_stop_chk = '1' and ");
        update_sql.append(" (cn_order.flag_durg = '1' and (ex_order_occ.pk_pdapdt is not null or ex_order_occ.pk_pdapdt !='' ");
        paramMap.put("dateStopChk", new Date());
        paramMap.put("pkEmpStopChk", user.getPkEmp());
        paramMap.put("nameEmpStopChk", user.getNameEmp());
        paramMap.put("flagStopChk", "1");
        paramMap.put("euStatus", "4");
        for (OrderCheckVO stopvo : stop_obj_list) {
            //停止时间--日期部分
            String stopDate = DateUtils.getDateStr(stopvo.getDateStop());
            String beginTime = stopDate + "000000";
            //校验医嘱是否已经被修改
            paramMap.put("pkCnOrdUpdate", stopvo.getPkCnord());
            paramMap.put("lastNum", stopvo.getLastNum());
            paramMap.put("dateStop", stopvo.getDateStop());
            StringBuilder update_ord = new StringBuilder(" update cn_order set eu_status_ord=:euStatus,date_stop_chk=:dateStopChk,");
            update_ord.append(" pk_emp_stop_chk=:pkEmpStopChk ,name_emp_stop_chk=:nameEmpStopChk,flag_stop_chk=:flagStopChk,last_num=:lastNum where pk_cnord=:pkCnOrdUpdate and date_stop=:dateStop");
            int update_num = DataBaseHelper.update(update_ord.toString(), paramMap);
            if (update_num <= 0) {
                msg.append(msg + stopvo.getNameOrd()).append(",");
                continue;
            }
            if ("1".equals(stopvo.getEuAlways())) {//临时医嘱末次无效
                continue;
            }
            //计算医嘱频次周期次数
            List<FreqTimeBaVo> timelist = orderExListMapper.queryFreqTimeList(stopvo.getCodeFreq());
            if (stopvo.getLastNum() != null && stopvo.getLastNum().longValue() != 0 && !isStopNumValid(stopvo, timelist)) {
                stopvo.setLastNum(null);
            }
            if (stopvo.getLastNum() == null) {//无末次
                //删掉未执行的sql参数
                pk_nolist.add(stopvo.getPkCnord());
                //查询已执行的执行单
                if ((user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) || "1".equals(stopvo.getFlagDurg()) && "1".equals(stopvo.getFlagBase())) {
                    //执行科室为本病区的非药品或者基数药查询
                    exlist.addAll(orderExListMapper.queryExListByWhereSql(" occ.pk_cnord = '" + stopvo.getPkCnord() + "' and occ.date_plan > to_date('" + DateUtils.getDefaultDateFormat().format(stopvo.getDateStop()) + "','YYYYMMDDHH24MISS') and occ.pk_dept_occ = '" + stopvo.getPkDeptExec() + "' "));
                } else if (!user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) {
                    //执行科室为非本病区的非药品,直接更新执行单
                    StringBuilder update_t = new StringBuilder("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='");
                    update_t.append(user.getPkEmp());
                    update_t.append("',name_emp_canc='").append(user.getNameEmp());
                    update_t.append("',pk_dept_canc='").append(user.getPkDept());
                    update_t.append("', date_canc=to_date('");
                    update_t.append(DateUtils.getDefaultDateFormat().format(new Date()));
                    update_t.append("','YYYYMMDDHH24MISS') where  pk_cnord = '");
                    update_t.append(stopvo.getPkCnord());
                    update_t.append("' and date_plan > to_date('").append(DateUtils.getDefaultDateFormat().format(stopvo.getDateStop()));
                    update_t.append("','YYYYMMDDHH24MISS') and pk_dept_occ = '").append(stopvo.getPkDeptExec()).append("' and eu_status = '1'");
                    update_other_list.add(update_t.toString());
                }
            } else if (stopvo.getLastNum().longValue() == 0) {//全删
                StringBuilder sql_t = new StringBuilder(del_sql.toString()).append(" and cn_order.pk_cnord = '").append(stopvo.getPkCnord());
                sql_t.append("') and ex_order_occ.date_plan >= to_date('").append(beginTime).append("','YYYYMMDDHH24MISS') ");
                del_list.add(sql_t.toString());
                //查询已执行的执行单
                if ((user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) || "1".equals(stopvo.getFlagDurg()) && "1".equals(stopvo.getFlagBase())) {
                    //执行科室为本病区的非药品或者基数药查询
                    exlist.addAll(orderExListMapper.queryExListByWhereSql(" occ.pk_cnord = '" + stopvo.getPkCnord() + "' and occ.date_plan >= to_date('" + beginTime + "','YYYYMMDDHH24MISS') and occ.pk_dept_occ = '" + stopvo.getPkDeptExec() + "' "));
                } else if (!user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) {
                    //执行科室为非本病区的非药品,直接更新执行单
                    update_other_list.add("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='" + user.getPkEmp() + "',name_emp_canc='" + user.getNameEmp() + "',pk_dept_canc='" + user.getPkDept() + "',"
                            + " date_canc=to_date('" + currTime + "','YYYYMMDDHH24MISS') where  pk_cnord = '" + stopvo.getPkCnord() + "' and date_plan >= to_date('" + beginTime + "','YYYYMMDDHH24MISS') and pk_dept_occ = '" + stopvo.getPkDeptExec() + "' and eu_status = '1'");
                }
                StringBuilder update_t = new StringBuilder(update_sql.toString()).append("))) and ex_order_occ.date_plan >= to_date('").append(beginTime).append("','YYYYMMDDHH24MISS')  and ex_order_occ.pk_cnord ='").append(stopvo.getPkCnord() + "'");
                update_list.add(update_t.toString());
            } else if (stopvo.getFirstNum() != null && DateUtils.getDateStr(stopvo.getDateStart()).equals(DateUtils.getDateStr(stopvo.getDateStop()))) {//当天开当天停，末次有效
                if (stopvo.getLastNum().intValue() > stopvo.getFirstNum().intValue()) {
                    //末次大于首次，不考虑
                    error.append(stopvo.getNameOrd()).append(",");
                    continue;
                } else if (stopvo.getLastNum().intValue() <= stopvo.getFirstNum().intValue()) {
                    //末次小于首次，以末次为准,删除或取消多余执行单
                    //本病区已执行，取消并退费；非本病区已执行，取消执行单；未执行，删除
                    this.processStopNumWhenSameDate(user, update_other_list, del_list, exlist, stopvo, currTime);
                }
            } else {
                //无执行时刻，则执行单全删
                if (timelist == null || timelist.size() <= 0) {
                    pk_nolist.add(stopvo.getPkCnord());
                } else {
                    //查询停止日当天执行单数量，无执行单则补对应数量的执行单(停止时间不在频次时刻范围内，未生成执行单情况)
                    Map<String, Object> queryParam = new HashMap<String, Object>();
                    queryParam.put("pkCnord", stopvo.getPkCnord());
                    queryParam.put("dateStop", DateUtils.getDateStr(stopvo.getDateStop()));
                    List<ExOrderOcc> stopDayOccList = DataBaseHelper.queryForList("select  *  from ex_order_occ where pk_cnord=:pkCnord and to_char(date_plan,'YYYYMMDD')=:dateStop ", ExOrderOcc.class, queryParam);
                    //补执行单
                    if (stopDayOccList == null || stopDayOccList.size() <= 0 || stopDayOccList.size() < stopvo.getLastNum().intValue()) {
                        int count = (stopDayOccList == null || stopDayOccList.size() <= 0) ? stopvo.getLastNum().intValue() : stopvo.getLastNum().intValue() - stopDayOccList.size();
                        for (int m = 0; m < count; m++) {
                            ExOrderOcc occ = new ExOrderOcc();
                            occ.setDatePlan(stopvo.getDateStop());
                            occ.setEuStatus("0");
                            occ.setPkCnord(stopvo.getPkCnord());
                            occ.setDripSpeed(CommonUtils.getLong(stopvo.getDripSpeed()));
                            occ.setFlagBase(stopvo.getFlagBase());
                            occ.setPkOrg(stopvo.getPkOrg());
                            occ.setPkOrgOcc(stopvo.getPkOrgExec());
                            occ.setPkPi(stopvo.getPkPi());
                            occ.setPkPv(stopvo.getPkPv());
                            occ.setQuanOcc(stopvo.getQuan());
                            occ.setPackSize(stopvo.getPackSize() == null ? 1 : stopvo.getPackSize().longValue());
                            occ.setPkUnit(stopvo.getPkUnit());
                            occ.setQuanCg(stopvo.getQuanCg());
                            occ.setPkUnitCg(stopvo.getPkUnitCg());
                            //基数药，自备药,执行机构
                            occ.setFlagSelf(stopvo.getFlagSelf());
                            occ.setPkDeptOcc(stopvo.getPkDeptExec());
                            occ.setFlagModi("0");
                            occ.setFlagCanc("0");
                            occ.setFlagPivas("0");
                            occ.setCreateTime(new Date());
                            if (UserContext.getUser() != null) {
                                occ.setCreator(UserContext.getUser().getPkEmp());
                            }
                            occ.setDelFlag("0");
                            occ.setTs(new Date());
                            DataBaseHelper.insertBean(occ);
                        }
                    } else {
                        int index = stopvo.getLastNum().intValue();
                        if (timelist.size() >= stopvo.getLastNum()) {
                            FreqTimeBaVo timevo = timelist.get(index - 1);
                            if (timevo != null && timevo.getTimeOcc() != null && !"".equals(timevo.getTimeOcc())) {
                                beginTime = stopDate + timevo.getTimeOcc().replaceAll(":", "");
                            }
                            StringBuilder sql_t = new StringBuilder(del_sql.toString()).append(" and cn_order.pk_cnord = '");
                            sql_t.append(stopvo.getPkCnord()).append("') and ex_order_occ.date_plan > to_date('");
                            sql_t.append(beginTime).append("','YYYYMMDDHH24MISS') ");
                            del_list.add(sql_t.toString());
                            //查询已执行的执行单
                            if ((user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) || "1".equals(stopvo.getFlagDurg()) && "1".equals(stopvo.getFlagBase())) {
                                //执行科室为本病区的非药品或者基数药查询
                                exlist.addAll(orderExListMapper.queryExListByWhereSql(" occ.pk_cnord = '" + stopvo.getPkCnord() + "' and occ.date_plan > to_date('" + beginTime + "','YYYYMMDDHH24MISS') and occ.pk_dept_occ = '" + stopvo.getPkDeptExec() + "' "));
                            } else if (!user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) {
                                //执行科室为非本病区的非药品,直接更新执行单
                                update_other_list.add("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='" + user.getPkEmp() + "',name_emp_canc='" + user.getNameEmp() + "',pk_dept_canc='" + user.getPkDept() + "',"
                                        + " date_canc=to_date('" + currTime + "','YYYYMMDDHH24MISS') where  pk_cnord = '" + stopvo.getPkCnord() + "' and date_plan > to_date('" + beginTime + "','YYYYMMDDHH24MISS') and pk_dept_occ = '" + stopvo.getPkDeptExec() + "' and eu_status = '1'");
                            }
                            StringBuilder update_t = new StringBuilder(update_sql.toString()).append(" ))) and  ex_order_occ.date_plan > to_date('").append(beginTime).append("','YYYYMMDDHH24MISS')  and ex_order_occ.pk_cnord ='").append(stopvo.getPkCnord()).append("'");
                            update_list.add(update_t.toString());
                        }
                    }
                }
            }
        }
        //发送执行单至平台
        //Map<String,Object> sendMap = sendOccListToPlat(del_list,exlist,update_list,update_other_list);
        //删除掉无末次的医嘱执行单
        if (pk_nolist != null && pk_nolist.size() > 0) {
            paramMap.put("pkOrds", pk_nolist);
            //删除无末次的执行单
            this.deleteExList(paramMap, true);
        }
        //删除有末次的执行单
        if (del_list != null && del_list.size() > 0) {
            DataBaseHelper.batchUpdate(del_list.toArray(new String[0]));
        }
        ///本病区执行已执行的非物品执行单与基数药置取消状态并进行退费
        if (exlist != null && exlist.size() > 0) {
            rtnCgAndCancelEx(exlist, user);
        }
        ///非本病区执行的执行单，更新状态
        if (update_other_list != null && update_other_list.size() > 0) {
            DataBaseHelper.batchUpdate(update_other_list.toArray(new String[0]));
        }
        if (pk_nolist != null && pk_nolist.size() > 0) {
            ///无末次已请领物品执行单置取消状态，
            this.cancelExList(paramMap, true);
        }
        ///有末次的已请领的物品执行单置取消状态
        if (update_list != null && update_list.size() > 0) {
            DataBaseHelper.batchUpdate(update_list.toArray(new String[0]));
        }
        if (msg != null && !"".equals(msg.toString()))
            msg.append("以上医嘱已发生变更，将被刷新，请重新操作!\n");
        if (error != null && !"".equals(error.toString()))
            error.append("以上医嘱末次数量大于首次，系统无法自动处理医嘱执行单，请手工处理！");
        //发送执行单至平台
//    	if(sendMap!=null){
//    		PlatFormSendUtils.sendAllExOrderOccMsg(sendMap);
//    	}
        return msg.append(error).toString();
    }

    /**
     * 获取需要发送平台的执行单
     *
     * @param del_list
     * @param exlist
     * @param update_list
     * @param update_other_list
     * @return
     */
    private Map<String, Object> sendOccListToPlat(List<String> del_list, List<ExCgBaVo> exlist, List<String> update_list, List<String> update_other_list) {
        //启用平台标志
        boolean platFormFlag = ("1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0")) && "1".equals(ApplicationUtils.getPropertyValue("msg.send.exlist", "0")));
        List<ExOrderOcc> delOccList = new ArrayList<ExOrderOcc>();
        List<ExOrderOcc> updateOccList = new ArrayList<ExOrderOcc>();
        Map<String, Object> sendParam = new HashMap<String, Object>();
        if (platFormFlag) {
            //查询需要删除的执行单集合
            if (del_list != null && del_list.size() > 0) {
                for (String delSql : del_list) {
                    List<ExOrderOcc> occ = DataBaseHelper.queryForList("select *  " + delSql.substring(delSql.indexOf("from"), delSql.length()), ExOrderOcc.class, new Object[]{});
                    if (occ != null && occ.size() > 0) {
                        delOccList.addAll(occ);
                    }
                }
            }
            //根据主键查询需要取消的执行单集合
            if (exlist != null && exlist.size() > 0) {
                List<String> pkExlist = new ArrayList<String>();
                for (ExCgBaVo occ : exlist) {
                    pkExlist.add(occ.getPkExocc());
                }
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("pkExList", pkExlist);
                List<ExOrderOcc> occ = DataBaseHelper.queryForList("select *  from  ex_order_occ where pk_exocc in (:pkExList)", ExOrderOcc.class, param);
                if (occ != null && occ.size() > 0) {
                    updateOccList.addAll(occ);
                }
            }
            //根据sql查询需要取消的执行单集合
            if (update_other_list != null && update_other_list.size() > 0) {
                for (String updateSql : update_other_list) {
                    List<ExOrderOcc> occ = DataBaseHelper.queryForList("select * from ex_order_occ " + updateSql.substring(updateSql.indexOf("where"), updateSql.length() - 1), ExOrderOcc.class, new Object());
                    if (occ != null && occ.size() > 0) {
                        updateOccList.addAll(occ);
                    }
                }
            }
            //根据sql查询需要取消的执行单集合
            if (update_list != null && update_list.size() > 0) {
                for (String updateSql : update_list) {
                    List<ExOrderOcc> occ = DataBaseHelper.queryForList("select * from ex_order_occ " + updateSql.substring(updateSql.indexOf("where"), updateSql.length() - 1), ExOrderOcc.class, new Object());
                    if (occ != null && occ.size() > 0) {
                        updateOccList.addAll(occ);
                    }
                }
            }
            if (updateOccList != null && updateOccList.size() > 0) {
                sendParam.put("addList", updateOccList);
            }
            if (delOccList != null && delOccList.size() > 0) {
                sendParam.put("deleteList", delOccList);
            }
            sendParam.put("IsSendSD", true);
            return sendParam;
        } else {
            return null;
        }
    }

    /**
     * 末次数量是否生效
     *
     * @return
     */
    private boolean isStopNumValid(OrderCheckVO vo, List<FreqTimeBaVo> timelist) {
        //开始日期=停止日期时，末次无效，按首次规则，中二修改为当天开当天停，末次有效
        //if(DateUtils.getDateStr(vo.getDateStart()).equals(DateUtils.getDateStr(vo.getDateStop())))
        //	return false;
        boolean flagWeek = "1".equals(vo.getEuCycle());
        //频次按执行周期当天本应该无执行单的，首日不生效
        if (flagWeek) {//按周执行，判断周数是否与当日一致
            boolean sameFlag = false;
            for (FreqTimeBaVo freqtime : timelist) {
                if (freqtime.getWeekNo() != null && DateUtils.getDayNumOfWeek(vo.getDateStop()) == freqtime.getWeekNo().intValue()) {
                    sameFlag = true;
                    break;
                }
            }
            if (sameFlag)//当前周数与执行频次里面
                return true;
            else
                return false;
        } else {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("pkCnord", vo.getPkCnord());
            param.put("flagStop", "1");
            param.put("dateStart", DateUtils.getSpecifiedDateStr(vo.getDateStop(), -7) + "000000");//取停止时间之前最后一次执行单生成时间
            param.put("end", DateUtils.getDateStr(vo.getDateStop()) + "235959");
            List<ExOrderOcc> exlist = orderExListMapper.queryExListByDate(param);
            if (exlist == null || exlist.size() <= 0) {
                return false;
            } else if ((DateUtils.getDateStr(exlist.get(0).getDatePlan()) + "235959").equals(DateUtils.getDateStr(vo.getDateStop()) + "235959")) {
                //如果最后一次执行记录的执行日期与停止日期相同，证明当天有执行单，末次有效
                return true;
            }
            param.put("dateStart", DateUtils.getDateStr(exlist.get(0).getDatePlan()) + "235959");
            List<ExOrderOcc> exVirList = createExecListService.createExListBySpecialTime(param);
            if (exVirList == null || exVirList.size() <= 0)
                return false;
            else
                return true;
        }
    }

    /**
     * 退费并取消执行单
     *
     * @param exlist
     * @param u
     */
    private void rtnCgAndCancelEx(List<ExCgBaVo> exlist, User u) {
        List<RefundVo> rtnList = new ArrayList<RefundVo>();
        for (ExCgBaVo exvo : exlist) {
            RefundVo vo = new RefundVo();
            vo.setNameEmp(u.getNameEmp());
            vo.setPkCgip(exvo.getPkCgip());
            vo.setPkDept(u.getPkDept());
            vo.setPkOrg(u.getPkOrg());
            vo.setPkEmp(u.getPkEmp());
            vo.setQuanRe(exvo.getQuan());
            rtnList.add(vo);
        }
        try {
            if (rtnList != null && rtnList.size() > 0) {
                BlPubReturnVo cgvo = ipCgPubService.refundInBatch(rtnList);
                //循环更新执行单的退费主键
                for (ExCgBaVo exvo : exlist) {
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("dateCanc", new Date());
                    paramMap.put("pkDept", u.getPkDept());
                    paramMap.put("pkEmp", u.getPkEmp());
                    paramMap.put("nameEmp", u.getNameEmp());
                    paramMap.put("pkCgRtn", "");
                    String pk_exocc = exvo.getPkExocc();
                    if (cgvo != null && cgvo.getBids() != null && cgvo.getBids().size() > 0) {
                        for (BlIpDt dt : cgvo.getBids()) {
                            if (pk_exocc.equals(dt.getPkOrdexdt())) {
                                paramMap.put("pkCgRtn", dt.getPkCgip());
                            }
                        }
                    }
                    StringBuilder sql = new StringBuilder("update ex_order_occ set flag_canc='1',pk_cg_cancel=:pkCgRtn,date_canc = :dateCanc,eu_status='9',");
                    sql.append(" pk_dept_canc=:pkDept,pk_emp_canc=:pkEmp,name_emp_canc=:nameEmp where pk_exocc = :pkExocc");
                    paramMap.put("pkExocc", pk_exocc);
                    // 更新执行状态。
                    DataBaseHelper.update(sql.toString(), paramMap);
                }
            }
        } catch (BusException e) {
            throw new BusException("住院退费出错:" + e.getMessage());
        }
    }

    /**
     * 停止长期医嘱时删除执行单
     *
     * @param paramMap{ordsnParents,pkOrds}
     * @param paramMap
     * @return
     * @throws BusException
     */
    public void deleteExList(Map<String, Object> paramMap, boolean isPk) throws BusException {
        //String sortno_parents = CommonUtils.getString(paramMap.get("ordsnParents"));
        //String pk_ords = CommonUtils.getString(paramMap.get("pkOrds"));
        StringBuilder delSql = new StringBuilder(" select pk_exocc from ex_order_occ  where (ex_order_occ.eu_status = '0' or ex_order_occ.eu_status = '9') ");
        delSql.append(" and (ex_order_occ.pk_pdapdt is null or ex_order_occ.pk_pdapdt ='') and exists (select pk_ord ");
        delSql.append(" from cn_order where cn_order.pk_cnord = ex_order_occ.pk_cnord ");
        delSql.append(" and ex_order_occ.date_plan > cn_order.date_stop  ");
        if (isPk) {
            delSql.append(" and cn_order.pk_cnord in (:pkOrds))");
        } else {
            delSql.append(" and cn_order.ordsn_parent in (:ordsnParents))");
        }
        List<Map<String, Object>> delPkexoccList = DataBaseHelper.queryForList(delSql.toString(), paramMap);
        StringBuilder sql = new StringBuilder(" delete from ex_order_occ  where (ex_order_occ.eu_status = '0' or ex_order_occ.eu_status = '9') ");
        sql.append(" and (ex_order_occ.pk_pdapdt is null or ex_order_occ.pk_pdapdt ='') and exists (select pk_ord ");
        sql.append(" from cn_order where cn_order.pk_cnord = ex_order_occ.pk_cnord ");
        sql.append(" and ex_order_occ.date_plan > cn_order.date_stop  ");
        if (isPk) {
            sql.append(" and cn_order.pk_cnord in (:pkOrds))");
            DataBaseHelper.update(sql.toString(), paramMap);
        } else {
            sql.append(" and cn_order.ordsn_parent in (:ordsnParents))");
            DataBaseHelper.update(sql.toString(), paramMap);
        }
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("exlist", delPkexoccList);
        msgMap.put("IsSendSD", true);//深大是否发送执行单删除信息
        //发送删除信息至平台
        //PlatFormSendUtils.sendDelExOrderOccMsg(msgMap);
    }

    /**
     * 停止长期医嘱时取消执行单
     *
     * @param paramMap{ordsnParents,pkOrds}
     * @param isPk
     * @return
     * @throws BusException
     */
    public void cancelExList(Map<String, Object> paramMap, boolean isPk) throws BusException {
        Date now = new Date();
        paramMap.put("dateCanc", now);
        StringBuffer query = new StringBuffer();
        query.append(" select pk_exocc from ex_order_occ");
        query.append(" where exists (select pk_cnord from cn_order  where cn_order.pk_cnord = ex_order_occ.pk_cnord and ex_order_occ.flag_canc = '0' ");
        query.append(" and cn_order.eu_always = '0' and ex_order_occ.date_plan > cn_order.date_stop  and ex_order_occ.eu_status != '9' and cn_order.flag_stop_chk = '1' and ");
        query.append(" (cn_order.flag_durg = '1' and (ex_order_occ.pk_pdapdt is not null or ex_order_occ.pk_pdapdt !=''))  "); //长期的已请领的药品医嘱
        if (isPk) {
            query.append(" and cn_order.pk_cnord in (:pkOrds))");
        } else {
            query.append(" and cn_order.ordsn_parent in (:ordsnParents))");
        }
        List<Map<String, Object>> pkExoccList = DataBaseHelper.queryForList(query.toString(), paramMap);
        StringBuilder sql = new StringBuilder(" update ex_order_occ  set ex_order_occ.eu_status = 9,ex_order_occ.flag_canc = '1',");
        sql.append("ex_order_occ.date_canc = :dateCanc,ex_order_occ.pk_dept_canc = :pkDept,ex_order_occ.pk_emp_canc  = :pkEmp,ex_order_occ.name_emp_canc  = :nameEmp ");
        sql.append(" where exists (select pk_cnord from cn_order  where cn_order.pk_cnord = ex_order_occ.pk_cnord and ex_order_occ.flag_canc = '0' ");
        sql.append(" and cn_order.eu_always = '0' and ex_order_occ.date_plan > cn_order.date_stop  and ex_order_occ.eu_status != '9' and cn_order.flag_stop_chk = '1' and ");
        sql.append(" (cn_order.flag_durg = '1' and (ex_order_occ.pk_pdapdt is not null or ex_order_occ.pk_pdapdt !=''))  "); //长期的已请领的药品医嘱
        if (isPk) {
            sql.append(" and cn_order.pk_cnord in (:pkOrds))");
        } else {
            sql.append(" and cn_order.ordsn_parent in (:ordsnParents))");
        }
        DataBaseHelper.update(sql.toString(), paramMap);
        Map<String, Object> msgMap = new HashMap<String, Object>();
        msgMap.put("exlist", pkExoccList);
        msgMap.put("IsSendSD", true);//深大是否发送执行单取消信息
        //发送取消执行信息至平台
        //PlatFormSendUtils.sendUpDateExOrderOccMsg(paramMap);
    }

    /**
     * 根据医嘱主键取消未执行的执行单
     *
     * @param pk_ords
     */
    public void cancelExListByPkOrd(Map<String, Object> param, String condition, IUser user) {
        User u = (User) user;
        param.put("dateCanc", new Date());
        param.put("pkDept", u.getPkDept());
        param.put("pkEmp", u.getPkEmp());
        param.put("nameEmp", u.getNameEmp());
        orderExListMapper.cancelExecListByPkOrd(param);
    }

    /**
     * 根据医嘱主键或执行单主键取消执行单并退费
     *
     * @param pkOrds ArrayList的形式传入
     */
    public void cancelExListAndRegCg(Map<String, Object> param, IUser user, boolean isPkOrd) {
        User u = (User) user;
        param.put("dateCanc", new Date());
        param.put("pkDept", u.getPkDept());
        param.put("pkEmp", u.getPkEmp());
        param.put("nameEmp", u.getNameEmp());
        StringBuilder sql = new StringBuilder(" update ex_order_occ  set eu_status = 9,flag_canc = '1',");
        sql.append("date_canc = :dateCanc,pk_dept_canc = :pkDept,pk_emp_canc  = :pkEmp,name_emp_canc  = :nameEmp ");
        sql.append(" where pk_cnord in (:pkOrds)");
        DataBaseHelper.update(sql.toString(), param);
        //退费TODO
    }

    /**
     * 根据医嘱主键查询对应已执行的执行单数量
     *
     * @param pk_ords
     * @return
     */
    public List<Map<String, Object>> queryExListNumByPkOrd(Map<String, Object> pk_ords) {
        return orderExListMapper.queryOrderExecListByOrd(pk_ords);
    }

    /**
     * 根据不同条件查询待生成执行单的医嘱列表
     *
     * @param param{pkPvs(List<String>),dateEnd,flagDrug,pkDeptNs}
     * @param user
     * @return
     * @throws ParseException
     */
    public List<GenerateExLisOrdVo> queryOrderExPlan(String param, IUser user) throws ParseException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        List<String> pvlist = (List) paramMap.get("pkPvs");
        //根据执行单相关参数重新加工生成执行单截止日期
        String dateEnd = getEndDate(CommonUtils.getString(paramMap.get("dateEnd")));
        paramMap.put("dateEnd", dateEnd);
        String pk_dept_cur = ((User) user).getPkDept();
        paramMap.put("pkDeptNs", pk_dept_cur);
        List<GenerateExLisOrdVo> list = orderExListMapper.queryOrderPlanList(paramMap);
        if (list != null && list.size() > 0) {
            new OrderCheckSortByOrdUtil().ordGroup(list);
        }
        return list;
    }

    /**
     * 生成执行单
     *
     * @param param{pkOrds:List<String),endDate}
     * @param user
     * @throws ParseException
     */
    public void generExecList(String param, IUser user) throws ParseException {
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        //处理医嘱主键，将数组转换为字符串形式
        List<String> pkOrds = (List<String>) paramMap.get("pkOrds");
        if (pkOrds == null || pkOrds.size() <= 0) return;
        //处理截至日期
        String dateEnd = getEndDate(CommonUtils.getString(paramMap.get("endDate")));
        paramMap.put("end", dateEnd);
        paramMap.put("pkDeptNs", ((User) user).getPkDept());
        //先查询已核对未生成执行单的医嘱，用来单独处理首日次数
        List<OrderCheckVO> chklist = orderExListMapper.getCheckedOrdList(paramMap);
        createExecListService.exec(paramMap);
        //单独处理由于医嘱核对时未生成执行单，导致未处理首日次数的情况
        this.createFirstExecList(chklist);
    }

    /**
     * 根据系统参数，计算生成执行单的截止日期
     *
     * @param param
     * @return
     * @throws ParseException
     */
    private String getEndDate(String param) throws ParseException {
        String dateEnd = null;
        String time = ExSysParamUtil.getExListEndTime();
        if (param == null || param.equals("")) {
            String days = ExSysParamUtil.getExListDays();
            Date end = DateUtils.getSpecifiedDay(new Date(), CommonUtils.getInt(days));
            dateEnd = DateUtils.getDateStr(end) + time;
        } else {
            dateEnd = param.substring(0, 8) + time;
        }
        return dateEnd;
    }

    /**
     * 根据首日次数追加或删除首日执行单数量
     * 首日数量firstNum不为null的认为设置了首日数量
     *
     * @param checklist
     * @return
     * @throws ParseException
     */
    private void createFirstExecList(List<OrderCheckVO> checklist) throws ParseException {
        if (checklist == null || checklist.size() <= 0)
            return;
//    	List<ExOrderOcc> deleteOccList = new ArrayList<ExOrderOcc>();
//    	List<ExOrderOcc> addOccList = new ArrayList<ExOrderOcc>();
        //启用平台标志
        //boolean platFormFlag = ("1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0"))&&!CommonUtils.isEmptyString(ApplicationUtils.getPropertyValue("msg.processClass", "")));
        for (OrderCheckVO vo : checklist) {
            if (vo.getFirstNum() == null)//未设置首次的不生效
                continue;
            String dateBegin = DateUtils.getDateStr(vo.getDateStart());
            //firstNum=0时删除开立日期所有执行单
            if (vo.getFirstNum().intValue() == 0) {
//    			if(platFormFlag){
//    				List<ExOrderOcc> delocc = DataBaseHelper.queryForList("select * from ex_order_occ where pk_cnord = ? and to_char(date_plan,'YYYYMMDD') = ? ",ExOrderOcc.class, new Object[]{vo.getPkCnord(),dateBegin});
//    			    if(delocc!=null&&delocc.size()>0){
//    			    	deleteOccList.addAll(delocc);
//    			    }
//    			}
                DataBaseHelper.execute("delete from ex_order_occ where pk_cnord = ? and to_char(date_plan,'YYYYMMDD') = ?", new Object[]{vo.getPkCnord(), dateBegin});
                continue;
            }
            //查询当日生成的执行单数量
            List<ExOrderOcc> exlist = DataBaseHelper.queryForList("select * from ex_order_occ where pk_cnord = ? and  to_char(date_plan,'YYYYMMDD') = ? order by date_plan desc ", ExOrderOcc.class, new Object[]{vo.getPkCnord(), dateBegin});
            int exsize = exlist == null ? 0 : exlist.size();
            if ("1".equals(vo.getEuAlways())) {
                //临时医嘱只首日次数小于执行单数的情况
                if (vo.getFirstNum().intValue() < exsize) {
                    int delnum = exsize - vo.getFirstNum().intValue();
                    //取当天的执行计划，删除多余的执行计划
                    for (int i = 0; i < delnum; i++) {
                        //deleteOccList.add(exlist.get(i));
                        DataBaseHelper.execute("delete from ex_order_occ where pk_exocc = ?", exlist.get(i).getPkExocc());
                    }
                }
                continue;
            }
            //首日次数小于执行单数，删掉多余的执行单
            if (vo.getFirstNum().intValue() < exsize) {
                int delnum = exsize - vo.getFirstNum().intValue();
                //取当天的执行计划，删除多余的执行计划
                for (int i = 0; i < delnum; i++) {
                    //deleteOccList.add(exlist.get(i));
                    DataBaseHelper.execute("delete from ex_order_occ where pk_exocc = ?", exlist.get(i).getPkExocc());
                }
                continue;
            }
            //计算医嘱频次周期次数
            List<FreqTimeBaVo> timelist = orderExListMapper.queryFreqTimeList(vo.getCodeFreq());
            boolean flagWeek = "1".equals(vo.getEuCycle());
            List<FreqTimeBaVo> timeWeeklist = null;//按周执行当天需要执行的频次时刻集合
            List<ExOrderOcc> exVirList = null;//根据虚拟医嘱时间生成的执行单集合
            Map<String, Object> param = null;//生成虚拟执行单参数
            boolean hasVirtal = false;//是否生成过虚拟执行单标志
            boolean flagChange = false;//是否需要变更医嘱状态标志
            //频次按执行周期当天本应该无执行单的，首日不生效
            if (flagWeek) {//按周执行，判断周数是否与当日一致
                timeWeeklist = new ArrayList<FreqTimeBaVo>();
                boolean sameFlag = false;
                for (FreqTimeBaVo freqtime : timelist) {
                    if (freqtime.getWeekNo() != null && DateUtils.getDayNumOfWeek(vo.getDateStart()) == freqtime.getWeekNo().intValue()) {
                        sameFlag = true;
                        timeWeeklist.add(freqtime);
                    }
                }
                if (!sameFlag)
                    continue;
            } else {
                param = new HashMap<String, Object>();
                param.put("pkCnord", vo.getPkCnord());
                param.put("flagFirst", "1");
                param.put("dateStart", dateBegin + "000000");
                param.put("end", dateBegin + "235959");
                exVirList = createExecListService.createExListBySpecialTime(param);
                if (exVirList == null || exVirList.size() <= 0)
                    continue;
                hasVirtal = true;
            }
            //首日次数大于执行单数量，
            if ((!flagWeek && vo.getFirstNum().intValue() > exsize && timelist.size() < 5) || (flagWeek && vo.getFirstNum().intValue() > exsize && timeWeeklist.size() < 5)) {
                //需要补的执行单数量
                int addnum = vo.getFirstNum().intValue() - exsize;
                //取最后一次执行单的计划执行时刻，根据执行时刻进行药品半衰期追加
                //默认追加执行计划的开始时间
                Date beginDatePlan = new Date();
                List<ExOrderOcc> addlist = new ArrayList<ExOrderOcc>();
                ExOrderOcc baseocc = new ExOrderOcc();
                if (exlist != null && exlist.size() > 0) {
                    baseocc = exlist.get(0);
                    //将开始追加执行计划时间设置为6小时以后
                    beginDatePlan = exlist.get(0).getDatePlan();
                } else {
                    //复制其他执行单
                    List<ExOrderOcc> t_exlist = DataBaseHelper.queryForList("select * from ex_order_occ where pk_cnord = ? and eu_status='0' ", ExOrderOcc.class, new Object[]{vo.getPkCnord()});
                    if (t_exlist == null || t_exlist.size() <= 0) {
                        if (exVirList == null && !hasVirtal) {//hasVirtal标志，避免二次生成
                            param = new HashMap<String, Object>();
                            param.put("pkCnord", vo.getPkCnord());
                            param.put("flagFirst", "1");
                            param.put("dateStart", dateBegin + "000000");
                            param.put("end", dateBegin + "235959");
                            exVirList = createExecListService.createExListBySpecialTime(param);
                        }
                        t_exlist = exVirList;
                        flagChange = true;
                    }
                    if (t_exlist == null)
                        throw new BusException("处理首日次数时，生成周期执行次数小于5的执行单时未生成任何数据 ！");
                    baseocc = t_exlist.get(0);
                    beginDatePlan = vo.getDateStart();
                }
                for (int i = 0; i < addnum; i++) {
                    Date datePlan = vo.getDateStart();
                    if (i > 0) {
                        datePlan = addHour(beginDatePlan, i * 6);
                    }
                    //超出当天235959,不再追加
                    if (datePlan.after(DateUtils.getDefaultDateFormat().parse(dateBegin + "235959"))) {
                        break;
                    }
                    ExOrderOcc occ = new ExOrderOcc();
                    ApplicationUtils.copyProperties(occ, baseocc);
                    occ.setPkExocc(NHISUUID.getKeyId());
                    occ.setDatePlan(datePlan);
                    addlist.add(occ);
                }
                //批量插入
                if (addlist != null && addlist.size() > 0) {
                    //addOccList.addAll(addlist);
                    DataBaseHelper.batchUpdate(DataBaseHelper.getInsertSql(ExOrderOcc.class), addlist);
                }
                if (flagChange) {
                    if (param == null)
                        param = new HashMap<String, Object>();
                    param.put("pkCnord", vo.getPkCnord());
                    param.put("dateEx", dateBegin + "235959");
                    DataBaseHelper.update("update cn_order set date_last_ex = to_date(:dateEx,'YYYYMMDDHH24MISS'), eu_status_ord = '3' where pk_cnord = :pkCnord", param);
                }
            } else if (vo.getFirstNum().intValue() > exsize && timelist.size() > 4) {
                //频次大于4，首日次数大于执行单数量，当前时刻添加一次
                ExOrderOcc baseocc = new ExOrderOcc();
                if (exsize > 0) {
                    baseocc = exlist.get(0);
                } else if (hasVirtal) {
                    baseocc = exVirList.get(0);
                } else {
                    throw new BusException("处理首日次数时,生成周期执行次数大于4的执行单时未生成任何数据 ！");
                }
                ExOrderOcc occ = new ExOrderOcc();
                ApplicationUtils.copyProperties(occ, baseocc);
                occ.setPkExocc(NHISUUID.getKeyId());
                occ.setDatePlan(vo.getDateStart());
                DataBaseHelper.insertBean(occ);
                //addOccList.add(occ);
            }
        }
        //发送变更的执行单到平台 2020.3.20注释
//    	if((addOccList==null||addOccList.size()<=0)&&(deleteOccList==null||deleteOccList.size()<=0))
//    		return;
//    	if("1".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0"))
//    			&&"1".equals(ApplicationUtils.getPropertyValue("msg.send.exlist", "0"))){
//    		Map<String,Object> paramMap = new HashMap<String,Object>();
//        	paramMap.put("addList", addOccList);
//        	paramMap.put("deleteList", deleteOccList);
//        	paramMap.put("IsSendSD", true);
//        	PlatFormSendUtils.sendAllExOrderOccMsg(paramMap);
//    	}
    }

    /**
     * 处理日期增加小时数
     *
     * @param dateTime
     * @param hour
     * @return
     */
    private Date addHour(Date dateTime, int hour) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(dateTime);
        rightNow.add(Calendar.HOUR, hour);// 增加小时
        return rightNow.getTime();
    }

    /**
     * 处理当天开当天停医嘱执行
     */
    private void processStopNumWhenSameDate(User user, List<String> update_other_list, List<String> del_list, List<ExCgBaVo> exlist, OrderCheckVO stopvo, String currTime) {
        List<ExOrderOcc> allOccList = orderExListMapper.queryExlist(stopvo.getPkCnord());
        if (allOccList == null || allOccList.size() <= 0 || allOccList.size() <= stopvo.getLastNum().intValue())
            return;
        for (int i = stopvo.getLastNum().intValue(); i < allOccList.size(); i++) {
            ExOrderOcc occvo = allOccList.get(i);
            if ("0".equals(occvo.getEuStatus())) {
                if ("1".equals(stopvo.getFlagDurg()) && CommonUtils.isNotNull(occvo.getPkPdapdt())) {
                    update_other_list.add("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='" + user.getPkEmp() + "',name_emp_canc='" + user.getNameEmp() + "',pk_dept_canc='" + user.getPkDept() + "',"
                            + " date_canc=to_date('" + currTime + "','YYYYMMDDHH24MISS') where  pk_exocc = '" + occvo.getPkExocc() + "'");
                } else {
                    del_list.add("delete from ex_order_occ where pk_exocc = '" + occvo.getPkExocc() + "'");
                }
            } else if ("1".equals(occvo.getEuStatus())) {
                //查询已执行的执行单
                if ((user.getPkDept().equals(stopvo.getPkDeptExec()) && "0".equals(stopvo.getFlagDurg())) || "1".equals(stopvo.getFlagDurg()) && "1".equals(stopvo.getFlagBase())) {
                    //执行科室为本病区的非药品或者基数药查询
                    List<ExCgBaVo> cglist = orderExListMapper.queryExAndCglist(occvo.getPkExocc());
                    if (cglist != null && cglist.size() > 0)//将所有已执行计费记录进行退费
                        exlist.addAll(cglist);
                    else //无记费记录，只取消执行单
                        update_other_list.add("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='" + user.getPkEmp() + "',name_emp_canc='" + user.getNameEmp() + "',pk_dept_canc='" + user.getPkDept() + "',"
                                + " date_canc=to_date('" + currTime + "','YYYYMMDDHH24MISS') where  pk_exocc = '" + occvo.getPkExocc() + "'");
                } else if (!user.getPkDept().equals(stopvo.getPkDeptExec())) {
                    //执行科室为非本病区的,直接更新执行单
                    update_other_list.add("update ex_order_occ set eu_status = '9',flag_canc = '1',pk_emp_canc='" + user.getPkEmp() + "',name_emp_canc='" + user.getNameEmp() + "',pk_dept_canc='" + user.getPkDept() + "',"
                            + " date_canc=to_date('" + currTime + "','YYYYMMDDHH24MISS') where  pk_exocc = '" + occvo.getPkExocc() + "'");
                }
            }
        }
    }
}
