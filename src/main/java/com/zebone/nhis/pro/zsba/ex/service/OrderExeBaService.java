package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.bl.pub.vo.BlPubParamVo;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.common.support.MathUtils;
import com.zebone.nhis.ex.pub.service.BlCgExPubService;
import com.zebone.nhis.ex.pub.vo.ExlistPubVo;
import com.zebone.nhis.pro.zsba.ex.dao.OrderAutoCgBaMapper;
import com.zebone.nhis.pro.zsba.ex.dao.OrderBaMapper;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Classname OrderExeBaService
 * @Description TODO
 * @Date 2020-09-18 19:34
 * @Created by wuqiang
 */
@Service
public class OrderExeBaService {
    @Autowired
    private IpCgPubService cgDao;
    @Autowired
    private OrderBaMapper orderMapper;
    @Resource
    private OrderAutoCgBaMapper orderAutoCgBaMapper;
    @Resource
    private BlCgExPubService blCgExPubService;

    /**
     * @return void
     * @Description 医嘱记费，有执行单的和没有执行单的分开记费
     * @auther wuqiang
     * @Date 2020-10-27
     * @Param [cheargeList, check_pklistExe, user]
     */
    public void OrderCharge(List<OrderCheckVO> cheargeList, Set<String> pkCnords, User user) {
        if (CollectionUtils.isEmpty(cheargeList)) {
            return;
        }
        List<BlPubParamVo> blPubParamVos = new ArrayList<BlPubParamVo>(16);
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrg", user.getPkOrg());
        Set<String> ordSet = new HashSet<String>();
        if (CollectionUtils.isNotEmpty(pkCnords)) {
            Iterator<OrderCheckVO> it = cheargeList.iterator();
            while (it.hasNext()) {
                OrderCheckVO checkVO = it.next();
                for (String pkCnOrds : pkCnords) {
                    if (pkCnOrds.equals(checkVO.getPkCnord())) {
                        it.remove();
                        ordSet.add(pkCnOrds);
                        break;
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(cheargeList)) {
            List<String> updateOrderList = new ArrayList<String>(16);
            for (OrderCheckVO OrderCheckVO : cheargeList) {
                BlPubParamVo blPubParamVo = setBlPubParamVo(OrderCheckVO, user);
                blPubParamVos.add(blPubParamVo);
                updateOrderList.add(this.generateUpdateSql("3", user, OrderCheckVO));
            }
            cgDao.chargeIpBatch(blPubParamVos, false);
            DataBaseHelper.batchUpdate(updateOrderList.toArray(new String[0]));
        }
        //4.获取执行单
        if (ordSet == null || ordSet.size() <= 0) {
            return;
        }
        paramMap.put("pkOrds", new ArrayList<String>(ordSet));
        List<ExlistPubVo> exlist = orderAutoCgBaMapper.queryExecListByOrd(paramMap);
        //5.根据执行单记费
        if (exlist == null || exlist.size() <= 0) {
            return;
        }
        blCgExPubService.execAndCg(exlist, user);
    }

    public List<OrderCheckVO> getOrder(List<String> list) {
        return orderMapper.getOrder(list);
    }

    public void updateOrderEuStatus(List<OrderCheckVO> check_listExe) {
        if (CollectionUtils.isEmpty(check_listExe)) {
            return;
        }
        DataBaseHelper.batchUpdate("update  CN_ORDER set EU_STATUS_ORD='3' where PK_CNORD=:pkCnord", check_listExe);
    }

    private BlPubParamVo setBlPubParamVo(OrderCheckVO ord, User user) {
        BlPubParamVo blP = new BlPubParamVo();
        blP.setOrdsn(ord.getOrdsn());
        blP.setOrdsnParent(ord.getOrdsnParent());
        blP.setFlagSign(ord.getFlagSign());
        blP.setPkOrg(user.getPkOrg());
        blP.setEuPvType("3");
        blP.setPkPv(ord.getPkPv());
        blP.setPkPi(ord.getPkPi());
        blP.setPkOrd(ord.getPkOrd());
        blP.setQuanCg(ord.getQuanCg());
        blP.setPkCnord(ord.getPkCnord());
        blP.setPkOrgEx(ord.getPkOrgExec());
        blP.setPkOrgApp(ord.getPkOrg());
        blP.setPkDeptEx(ord.getPkDeptExec());
        blP.setPkDeptApp(ord.getPkDept());
        blP.setDateHap(new Date());
        blP.setPkDeptCg(user.getPkDept());
        blP.setPkEmpCg(user.getPkEmp());
        blP.setNameEmpCg(user.getNameEmp());
        blP.setEuBltype("4");
        blP.setFlagPd(ord.getFlagDurg());
        Integer cnt = 1;
        //根据频次计算周期执行次数
        if (!CommonUtils.isEmptyString(ord.getCodeFreq())) {
            cnt = DataBaseHelper.queryForScalar("select cnt from bd_term_freq where code = ? and del_flag = '0'", Integer.class, ord.getCodeFreq());
        }
        if (cnt == null) {
            cnt = 1;
        }
        //如果是物品，取零售单位，重新计算数量
        if ("1".equals(ord.getFlagDurg())) {
            // blPubParamVo.setPkUnitPd();  //零售单位(pd的包装单位pk_unit_pack) 药品包装量(pack_size) 在已经在VO里
            Integer day = (ord.getDays() == null || ord.getDays() == 0) ? 1 : ord.getDays().intValue();
            double quanCg = MathUtils.mul(ord.getQuan(), MathUtils.mul(cnt.doubleValue(), day.doubleValue()));
            blP.setQuanCg(Math.ceil(quanCg));
            blP.setBatchNo("~");
            blP.setDateExpire(new Date());
            double price = ord.getPriceCg() == null ? 0 : ord.getPriceCg();
            Integer packSize = ord.getPackSize() == null ? 0 : ord.getPackSize().intValue();
            Double priceMin = MathUtils.div(price, packSize.doubleValue());
            blP.setPrice(priceMin);
            blP.setPriceCost(priceMin);
            blP.setPkItem(ord.getPkOrd()); // 药品的话此处传pkPd,非药品的话不用传，只要pk_ord有值就好
            blP.setPackSize(1);//记费单位存为基本单位
            blP.setPkUnitPd(ord.getPkUnit());
        } else {
            blP.setQuanCg(MathUtils.mul(ord.getQuan(), cnt.doubleValue()));
        }
        return blP;
    }


    /**
     * 医嘱核对--更新医嘱状态--校验时间戳
     * param{checkList}
     */
    public void updateOrdStateByTs(List<OrderCheckVO> clist, List<String> pkclist, List<OrderCheckVO> stoplist, List<String> pkstoplist, List<OrderCheckVO> eralist, User u, int total, Set<String> dietPkPvs) {
        int cnt = 0;
        String dateTs = "";
        //核对
        if (clist != null && clist.size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            //map.put("parentNos", clist);
            map.put("pkCnords", pkclist);
            map.put("pkEmpChk", u.getPkEmp());
            map.put("nameEmpChk", u.getNameEmp());
            map.put("dateChk", new Date());
            map.put("euStatus", "2");
            map.put("whereSql", " and eu_status_ord = 0 ");
            for (OrderCheckVO vo : clist) {
                dateTs = DateUtils.getDateTimeStr(vo.getTs());
                cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("2", u, vo), vo);
            }
            //更新各类申请单状态
            DataBaseHelper.update(getUpdateSql("cn_consult_apply"), map);//2018-12-21 增加会诊的申请单处理
            DataBaseHelper.update(getUpdateSql("cn_trans_apply"), map);
            DataBaseHelper.update(getUpdateSql("cn_op_apply"), map);
            DataBaseHelper.update(getUpdateSql("cn_pa_apply"), map);
            DataBaseHelper.update(getUpdateSql("cn_ris_apply"), map);
            DataBaseHelper.update(getUpdateSql("cn_lab_apply"), map);
        }
        //停止核对
        if (stoplist != null && stoplist.size() > 0) {
            Map<String, Object> stopmap = new HashMap<String, Object>();
            stopmap.put("pkCnords", pkstoplist);
            stopmap.put("dateStopChk", new Date());
            stopmap.put("pkEmpStopChk", u.getPkEmp());
            stopmap.put("nameEmpStopChk", u.getNameEmp());
            stopmap.put("flagStopChk", "1");
            stopmap.put("euStatus", "4");
            stopmap.put("whereSql", " and eu_status_ord >= 1 ");
            //先把护理医嘱的停止时间和停止人置上
            updateNsStopInfo(pkstoplist, stopmap);
            for (OrderCheckVO vo : stoplist) {
                cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("4", u, vo), vo);
            }
        }
        //作废核对
        if (eralist != null && eralist.size() > 0) {
            for (OrderCheckVO vo : eralist) {
                cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("9", u, vo), vo);
            }
        }
        //更新患者饮食医嘱等级
        if (dietPkPvs != null && dietPkPvs.size() > 0) {
            //获取患者饮食医嘱等级
            StringBuilder sql = new StringBuilder("");
            sql.append(" select ord.pk_pv,max(VAL_ATTR) AS dt_dietary from BD_DICTATTR t ");
            sql.append(" inner join  CN_ORDER ord on ord.PK_ORD=t.PK_DICT ");
            sql.append(" where t.CODE_ATTR ='0201' and ord.CODE_ORDTYPE='13' ");
            sql.append(" and ord.EU_STATUS_ORD in('2','3','4') and ord.pk_pv in (");
            sql.append(CommonUtils.convertSetToSqlInPart(dietPkPvs, "pk_pv"));
            sql.append(") and t.DEL_FLAG='0' group by ord.pk_pv ");
            List<Map<String, Object>> dietaryListMap = DataBaseHelper.queryForList(sql.toString());
            //更新患者饮食等级
            String updateSql = "update pv_ip set dt_dietary=:dtDietary where pk_pv =:pkPv";
            DataBaseHelper.batchUpdate(updateSql, dietaryListMap);
        }
        if (cnt < total)
            throw new BusException("您提交的医嘱部分已经发生了变更，请刷新后重新提交！");
    }

    /**
     * 获取更新申请单状态sql
     *
     * @param tableName
     * @param sortno_parent
     * @return
     */
    private String getUpdateSql(String tableName) {
        return "update " + tableName + "  set eu_status = 1 where pk_cnord  in (:pkCnords) ";
    }


    /**
     * 医嘱核对时生成医嘱状态更新语句
     *
     * @param status
     * @param u
     * @param checkOrdVo
     * @return
     */
    private String generateUpdateSql(String status, User u, OrderCheckVO vo) {
        StringBuilder sql = new StringBuilder("	update cn_order set eu_status_ord ='").append(status).append("'");
        String curTime = DateUtils.getDateTimeStr(new Date());
        String dateTs = DateUtils.getDateTimeStr(vo.getTs());
        sql.append(",ts=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
        if ("2".equals(status)) {//核对
            sql.append(",pk_emp_chk='").append(u.getPkEmp()).append("'");
            sql.append(",name_emp_chk='").append(u.getNameEmp()).append("'");
            sql.append(",date_chk=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
            sql.append(",modifier='").append(u.getPkEmp()).append("'");
            sql.append(" where eu_status_ord = '0' ");
        } else if ("4".equals(status)) {//停止
            sql.append(",pk_emp_stop_chk='").append(u.getPkEmp()).append("'");
            sql.append(",name_emp_stop_chk='").append(u.getNameEmp()).append("'");
            sql.append(",date_stop_chk=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
            sql.append(",flag_stop_chk='1'");
            sql.append(",modifier='").append(u.getPkEmp()).append("'");
            sql.append(" where eu_status_ord >= 1  ");
        } else if ("9".equals(status)) {
            sql.append(",pk_emp_erase_chk='").append(u.getPkEmp()).append("'");
            sql.append(",name_erase_chk='").append(u.getNameEmp()).append("'");
            sql.append(",date_erase_chk=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
            sql.append(",flag_erase_chk='1'");
            sql.append(",modifier='").append(u.getPkEmp()).append("'");
            sql.append(" where eu_status_ord > 1  ");
        } else if ("3".equals(status)) {
            sql.append(",date_last_ex=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
            sql.append(",modifier='").append(u.getPkEmp()).append("'");
            sql.append(" where eu_status_ord = '2' ");
            sql.append(" and pk_cnord='" + vo.getPkCnord() + "'");
            return sql.toString();
        }
        sql.append(" and to_char(ts,'YYYYMMDDHH24MISS')='" + dateTs + "'");
        sql.append(" and pk_cnord=:pkCnord");
        return sql.toString();
    }


    /**
     * 更新护理医嘱的停止时间
     *
     * @param stoplist
     * @param stopmap
     */
    private void updateNsStopInfo(List<String> stoplist, Map<String, Object> stopmap) {
        StringBuilder sql = new StringBuilder("update cn_order  set ");
        sql.append(" cn_order.date_stop=(select dr.date_stop from cn_order dr where cn_order.ordsn_parent=dr.ordsn and dr.flag_stop='1')");
        sql.append(",cn_order.flag_stop='1' ,cn_order.pk_emp_stop = :pkEmpStopChk,cn_order.name_emp_stop = :nameEmpStopChk ");
        sql.append(" where cn_order.pk_cnord in (:pkCnords) and cn_order.flag_doctor = '0' ");
        DataBaseHelper.update(sql.toString(), stopmap);
    }
}