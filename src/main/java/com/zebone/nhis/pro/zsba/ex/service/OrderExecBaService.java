package com.zebone.nhis.pro.zsba.ex.service;

import com.zebone.nhis.bl.pub.service.IpCgPubService;
import com.zebone.nhis.common.module.bl.RefundVo;
import com.zebone.nhis.common.service.CnNoticeService;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.ex.dao.OrderExecBaMapper;
import com.zebone.nhis.pro.zsba.ex.vo.OrderCheckVO;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 医嘱执行单相关的组合服务 --博爱版本
 *
 * @author zhangtao
 */

@Service
public class OrderExecBaService {

    @Resource
    private OrderExecBaMapper orderExecBaMapper;

    @Resource
    private IpCgPubService ipCgPubService;

    @Resource
    private CnNoticeService cnNoticeService;//临床消息提醒
    private Logger logger = LoggerFactory.getLogger("com.zebone");

    /**
     * 检查检验作废医嘱核对
     *
     * @param param{list}
     * @param user
     * @return
     */
    public String cancelCheckOrd(String param, IUser user) {
        //核对医嘱,是否自动生成执行单，是否自动计费
        List<OrderCheckVO> checkList = JsonUtil.readValue(param, new TypeReference<List<OrderCheckVO>>() {
        });
        if (checkList == null || checkList.size() <= 0) throw new BusException("未获取到核对数据！");
        List<String> erase_pklist = new ArrayList<String>();
        List<OrderCheckVO> erase_list = new ArrayList<OrderCheckVO>();
        //根据医嘱状态，作废核对
        for (OrderCheckVO vo : checkList) {
            String pk_cnord = vo.getPkCnord();
            erase_list.add(vo);
            erase_pklist.add(pk_cnord);
        }



        User u = (User) user;
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("pkOrg", u.getPkOrg());
        paramMap.put("pkDept", u.getPkDept());
        paramMap.put("pkDeptNs", u.getPkDept());
        paramMap.put("pkEmp", u.getPkEmp());
        paramMap.put("nameEmp", u.getNameEmp());

        //更新医嘱状态
        updateOrdStateByTs(erase_list, u, checkList.size());
        //退费、更新执行单、更新申请单、更新医疗执行记录
        cancelExAndRtnCg(erase_pklist, u, paramMap);

        //发送医嘱核对信息至平台
        for (OrderCheckVO vo : checkList) {
            String pk_cnord = vo.getPkCnord();
            Map<String,Object> paramMs = new HashMap<String,Object>();
            paramMs.put("pkDept", vo.getPkDeptNsCur());
            paramMs.put("pkCnorder", pk_cnord);
            //核对后更新临床消息提醒数量
            cnNoticeService.updateChkCnNotice(paramMs);
        }

        paramMap = new HashMap<String, Object>();
        //传map格式
        paramMap.put("ordlist", JsonUtil.readValue(param, new TypeReference<List<Map<String, Object>>>() {
        }));
        //传vo格式
        List<OrderCheckVO> ordlistvo = JsonUtil.readValue(param, new TypeReference<List<OrderCheckVO>>() {
        });
        for (OrderCheckVO orderCheckVo : ordlistvo) {
            orderCheckVo.setPkOrg(u.getPkOrg());
        }

        paramMap.put("ordlistvo", ordlistvo);
        paramMap.put("control", "NW");//操作类型
        paramMap.put("ordStatus", "3");//医嘱状态
        paramMap.put("checkList", checkList);//深大需要数据
        paramMap.put("checkOrd", "");//深大发消息标志
        if (!"SyxPlatFormSendService".equals(ApplicationUtils.getPropertyValue("msg.send.switch", "0")))//孙逸仙改为医嘱签署时发送消息
            PlatFormSendUtils.sendExOrderCheckMsg(paramMap);
        paramMap = null;
        return "";
    }

    /**
     * 医嘱核对--更新医嘱状态--校验时间戳
     * param{checkList}
     */
    public void updateOrdStateByTs(List<OrderCheckVO> eralist, User u, int total) {
        int cnt = 0;
        String dateTs = "";
        //作废核对
        if (eralist != null && eralist.size() > 0) {
            for (OrderCheckVO vo : eralist) {
                cnt = cnt + DataBaseHelper.update(this.generateUpdateSql("9", u, vo), vo);
            }
        }
        if (cnt < total)
            throw new BusException("您提交的医嘱部分已经发生了变更，请刷新后重新提交！");
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
        if ("9".equals(status)) {
            sql.append(",pk_emp_erase_chk='").append(u.getPkEmp()).append("'");
            sql.append(",name_erase_chk='").append(u.getNameEmp()).append("'");
            sql.append(",date_erase_chk=to_date('" + curTime + "','YYYYMMDDHH24MISS')");
            sql.append(",flag_erase_chk='1'");
            sql.append(",modifier='").append(u.getPkEmp()).append("'");
            sql.append(" where eu_status_ord > 1  ");
        }
        sql.append(" and to_char(ts,'YYYYMMDDHH24MISS')='" + dateTs + "'");
        sql.append(" and pk_cnord=:pkCnord");
        return sql.toString();
    }

    /**
     * 取消执行并退费
     */
    public void cancelExAndRtnCg(List<String> erase_list, User u, Map<String, Object> paramMap) {
        if (erase_list == null || erase_list.size() <= 0) return;
        //1、退费
        Map<String, Object> pkOrdMap = new HashMap<String, Object>();
        pkOrdMap.put("pk_cnord", erase_list);
        String querySql = "SELECT bl.pk_cnord,bl.pk_cgip,bl.quan AS quan_re,tt.sumQuan FROM " +
                          " bl_ip_dt bl " +
                          " INNER JOIN cn_order ord ON bl.pk_cnord = ord.pk_cnord AND ord.del_flag= '0' " +
                          " INNER JOIN (select SUM(quan) sumQuan,pk_cnord from bl_ip_dt where del_flag = '0' AND pk_cnord IN ( :pk_cnord) GROUP BY pk_cnord) tt on tt.pk_cnord = bl.pk_cnord  and tt.sumQuan > 0" +
                          " WHERE " +
                          " bl.del_flag = '0' " +
                          " AND bl.pk_cnord IN ( :pk_cnord)";
        List<RefundVo> refundVos = DataBaseHelper.queryForList(querySql, RefundVo.class, pkOrdMap);
        if (refundVos != null && refundVos.size() > 0) {
            ipCgPubService.refundInBatch(refundVos);
        }
        //2、更新执行单
        paramMap.put("dateCanc", new Date());
        paramMap.put("pkOrds", erase_list);
        orderExecBaMapper.cancelExecListByPkOrd(paramMap);
        //3、更新申请单
        DataBaseHelper.update("update cn_ris_apply set eu_status='0' where pk_cnord in (:pk_cnord) and eu_status > '0'", pkOrdMap);//更新检查申请单
        DataBaseHelper.update("update cn_lab_apply set eu_status='0' where pk_cnord in (:pk_cnord) and eu_status > '0'", pkOrdMap);//更新检验申请单
        //4、更新医嘱执行记录
        String dateTime = DateUtils.getDateTimeStr(new Date());
        StringBuilder sql = new StringBuilder("	update ex_assist_occ set flag_canc='1' ,EU_STATUS='9'  ");
        sql.append(",pk_emp_canc='").append(u.getPkEmp()).append("'");
        sql.append(",name_emp_canc='").append(u.getNameEmp()).append("'");
        sql.append(",date_canc=to_date('" + dateTime + "','YYYYMMDDHH24MISS')");
        sql.append(",modifier='").append(u.getPkEmp()).append("'");
        sql.append(",modity_time=to_date('" + dateTime + "','YYYYMMDDHH24MISS')");
        sql.append(" where pk_cnord in (:pk_cnord) ");
        DataBaseHelper.update(sql.toString(), pkOrdMap);//更新医疗执行记录主表
        DataBaseHelper.update("delete from ex_assist_occ_dt where pk_cnord in (:pk_cnord) ", pkOrdMap);//删除医疗执行记录明细
    }
}
