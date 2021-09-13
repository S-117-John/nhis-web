package com.zebone.nhis.pro.zsba.cn.ipdw.service;

import com.zebone.nhis.cn.pub.service.CnPubService;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnSignCa;
import com.zebone.nhis.common.module.pv.PvAdt;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.PlatFormSendUtils;
import com.zebone.nhis.pro.zsba.cn.ipdw.dao.CnOrdBaMapper;
import com.zebone.nhis.pro.zsba.cn.ipdw.vo.BaCanlParam;
import com.zebone.nhis.pro.zsba.cn.ipdw.vo.CanRisLabVo;
import com.zebone.platform.common.support.User;
import com.zebone.platform.framework.support.IUser;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Ris（检查）中山博爱专版
 */
@Service
public class CnOrdBaService {
    @Autowired
    private CnOrdBaMapper risDao;
    @Autowired
    private CnPubService cnPubService;

    /**
     * 撤销检查
     *
     * @param param
     * @param user
     */
    public void cancleRisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        BaCanlParam rcp = JsonUtil.readValue(param, BaCanlParam.class);
        User u = (User) user;
        List<String> pkCnOrds = rcp.getPkCnOrds();
        if (rcp.getOrdsns() == null || rcp.getOrdsns().size() <= 0) {
            throw new BusException("未获取到对应的医嘱数据！");
        }
        //获取患者信息
        if (rcp.getPkPv() == null || StringUtils.isBlank(rcp.getPkPv())) {
            throw new BusException("未获取到当前患者信息！");
        }
        boolean ifTransfer = qryPvInfo(rcp.getPkPv(), u.getPkDept()); //患者是否转科
        if (ifTransfer) {
            transferCancleRis(rcp, u);
        } else {
            noTraTnsferCancleRis(rcp, u);
        }
        //其他操作，CA写入，消息发送
        List<CnSignCa> cnSignCaList = rcp.getCnSignCaList();
        cnPubService.caRecord(cnSignCaList);
        List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?", CnOrder.class, new Object[]{pkCnOrds.get(0)});
        cnPubService.sendMessage("作废医嘱", cnOrds, false);
        //发送平台消息
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkCnList", pkCnOrds);
        PlatFormSendUtils.sendCancleRisApplyListMsg(paramMap);
    }

    /**
     * 本科室患者作废
     *
     * @param rcp
     * @param u
     */
    private void transferCancleRis(BaCanlParam rcp, User u) {
        List<BigDecimal> ordsns = new ArrayList<BigDecimal>();
        for (String vo : rcp.getOrdsns()) {
            ordsns.add(new BigDecimal(vo));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ordsns", ordsns); //传入的是父医嘱号
        //数据校验
        List<CanRisLabVo> exList = risDao.qryRisOrd(map);
        if (exList != null && exList.size() > 0) {
            String mess = "";
            boolean ifThrow = false;
            for (CanRisLabVo item : exList) {
                if ("1".equals(item.getOccStatus())
                        && !"1".equals(item.getAppStatus())) {
                    ifThrow = true;
                    mess = mess + "【" + item.getNameOrd() + "】状态为 " + getExecNameStatus(item.getAppStatus(), 0) + ",请联系【" + item.getNameDept() + "】进行回退操作！\r\n";
                }
            }
            //mess = mess+ "请先通知医技科室取消执行后再进行作废！";
            if (ifThrow && StringUtils.isNotEmpty(mess)) throw new BusException(mess);
        } else {
            throw new BusException("未查询到医嘱信息！请核对数据。");
        }
        //作废医嘱更新语句
        List<String> orderSqlList = new ArrayList<String>(8);

        for (BigDecimal ord : ordsns) {
            orderSqlList.add("update cn_order set eu_status_ord='9',date_erase='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase='" + u.getPkEmp() + "',name_emp_erase='" + u.getNameEmp() + "',flag_erase='1' where ORDSN_PARENT=" + ord + "");

        }
        //作废医嘱
        if (null != orderSqlList && orderSqlList.size() > 0)
        { DataBaseHelper.batchUpdate(orderSqlList.toArray(new String[0]));}

    }

    private void noTraTnsferCancleRis(BaCanlParam rcp, User u) {
        List<BigDecimal> ordsns = new ArrayList<BigDecimal>();
        for (String vo : rcp.getOrdsns()) {
            ordsns.add(new BigDecimal(vo));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ordsns", ordsns); //传入的是父医嘱号
        //数据校验
        List<CanRisLabVo> exList = risDao.qryRisOrd(map);
        if (exList != null && exList.size() > 0) {
            String mess = "该患者已经转科，";
            boolean ifThrow = false;
            for (CanRisLabVo item : exList) {
                if ("1".equals(item.getOccStatus())) {
                    ifThrow = true;
                    //mess = mess+"【"+item.getNameOrd()+"】状态为 "+getExecNameStatus(item.getOccStatus(),0)+",请联系【"+item.getNameDept()+"】进行回退操作！\r\n";
                }
            }
            mess = mess + "请先通知护士在医技申请管理中取消执行后再进行撤销操作！";
            if (ifThrow && StringUtils.isNotEmpty(mess)) throw new BusException(mess);
        } else {
            throw new BusException("未查询到医嘱信息！请核对数据。");
        }
        //作废医嘱更新语句
        List<String> orderSqlList = new ArrayList<String>(8);
        List<String> exordSqlList = new ArrayList<String>(8);
        for (BigDecimal ord : ordsns) {
            orderSqlList.add("update cn_order set eu_status_ord='9',date_erase='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase='" + u.getPkEmp() + "',name_emp_erase='" + u.getNameEmp() + "',flag_erase='1' ,flag_erase_chk='1',date_erase_chk='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase_chk='" + u.getPkEmp() + "',name_erase_chk='" + u.getNameEmp() + "' where ORDSN_PARENT=" + ord + "");
            exordSqlList.add("update EX_ORDER_OCC set EU_STATUS='9' where PK_CNORD in ("
                    +"select CO.PK_CNORD  from CN_ORDER CO where co.ORDSN_PARENT=" + ord + ")"
            );
        }
        //作废医嘱
        if (null != orderSqlList && orderSqlList.size() > 0)
        { DataBaseHelper.batchUpdate(orderSqlList.toArray(new String[0]));}
        if (!CollectionUtils.isEmpty(exordSqlList))
        { DataBaseHelper.batchUpdate(exordSqlList.toArray(new String[0]));}

    }

    /**
     * 撤销检验
     *
     * @param param
     * @param user
     */
    public void cancleLisApplyList(String param, IUser user) throws IllegalAccessException, InvocationTargetException {
        BaCanlParam rcp = JsonUtil.readValue(param, BaCanlParam.class);
        User u = (User) user;
        List<String> pkCnOrds = rcp.getPkCnOrds();
        if (rcp.getOrdsns() == null || rcp.getOrdsns().size() <= 0) {
            throw new BusException("未获取到对应的医嘱数据！");
        }
        //获取患者信息
        if (rcp.getPkPv() == null || StringUtils.isBlank(rcp.getPkPv())) {
            throw new BusException("未获取到当前患者信息！");
        }
        boolean ifTransfer = qryPvInfo(rcp.getPkPv(), u.getPkDept()); //患者是否转科
        if (ifTransfer) {
            transferCancleLis(rcp, u);
        } else {
            noTraTnsferCancleLis(rcp, u);
        }
        //其他操作，CA写入，消息发送
        List<CnSignCa> cnSignCaList = rcp.getCnSignCaList();
        cnPubService.caRecord(cnSignCaList);
        List<CnOrder> cnOrds = DataBaseHelper.queryForList("select name_ord,pk_pv,pk_dept_ns,pk_cnord,ordsn from cn_order where pk_cnord=?", CnOrder.class, new Object[]{pkCnOrds.get(0)});
        cnPubService.sendMessage("作废医嘱", cnOrds, false);
        //发送平台消息
        Map<String, Object> paramMap = JsonUtil.readValue(param, Map.class);
        paramMap.put("pkCnList", pkCnOrds);
        PlatFormSendUtils.sendCancleRisApplyListMsg(paramMap);
    }

    /**
     * 本科室作废--检查
     */
    private void transferCancleLis(BaCanlParam rcp, User u) {
        List<BigDecimal> ordsns = new ArrayList<BigDecimal>();
        for (String vo : rcp.getOrdsns()) {
            ordsns.add(new BigDecimal(vo));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ordsns", ordsns); //传入的是父医嘱号
        //数据校验
        List<CanRisLabVo> exList = risDao.qryLabOrd(map);
        if (exList != null && exList.size() > 0) {
            String mess = "";
            boolean ifThrow = false;
            for (CanRisLabVo item : exList) {
                if ("1".equals(item.getOccStatus())
                        && !"1".equals(item.getAppStatus())) {
                    ifThrow = true;
                    mess = mess + "【" + item.getNameOrd() + "】状态为 " + getExecNameStatus(item.getAppStatus(), 1) + ",请联系【" + item.getNameDept() + "】进行回退操作！\r\n";
                }
            }
            //mess = mess+ "请先通知医技科室取消执行后再进行作废！";
            if (ifThrow && StringUtils.isNotEmpty(mess)) throw new BusException(mess);
        } else {
            throw new BusException("未查询到医嘱信息！请核对数据。");
        }
        //作废医嘱更新语句
        List<String> orderSqlList = new ArrayList<String>();
        for (BigDecimal ord : ordsns) {
            orderSqlList.add("update cn_order set eu_status_ord='9',date_erase='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase='" + u.getPkEmp() + "',name_emp_erase='" + u.getNameEmp() + "',flag_erase='1' where ORDSN_PARENT=" + ord + "");
        }
        //作废医嘱
        if (null != orderSqlList && orderSqlList.size() > 0)
            DataBaseHelper.batchUpdate(orderSqlList.toArray(new String[0]));
    }

    /**
     * 转科患者作废
     *
     * @param rcp
     * @param u
     */
    private void noTraTnsferCancleLis(BaCanlParam rcp, User u) {
        List<BigDecimal> ordsns = new ArrayList<BigDecimal>();
        for (String vo : rcp.getOrdsns()) {
            ordsns.add(new BigDecimal(vo));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("ordsns", ordsns); //传入的是父医嘱号
        //数据校验
        List<CanRisLabVo> exList = risDao.qryLabOrd(map);
        if (exList != null && exList.size() > 0) {
            String mess = "该患者已经转科，";
            boolean ifThrow = false;
            for (CanRisLabVo item : exList) {
                if ("1".equals(item.getOccStatus())) {
                    ifThrow = true;
                    //mess = mess+"【"+item.getNameOrd()+"】状态为 "+getExecNameStatus(item.getOccStatus(),1)+",请联系【"+item.getNameDept()+"】进行回退操作！\r\n";
                }
            }
            mess = mess + "请先通知护士在医技申请管理中取消执行后再进行撤销操作！";
            if (ifThrow && StringUtils.isNotEmpty(mess)) throw new BusException(mess);
        } else {
            throw new BusException("未查询到医嘱信息！请核对数据。");
        }
        //作废医嘱更新语句
        List<String> orderSqlList = new ArrayList<String>();
        List<String> exordSqlList = new ArrayList<String>(8);
        for (BigDecimal ord : ordsns) {
            orderSqlList.add("update cn_order set eu_status_ord='9',date_erase='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase='" + u.getPkEmp() + "',name_emp_erase='" + u.getNameEmp() + "',flag_erase='1',flag_erase_chk='1',date_erase_chk='" + DateUtils.getDate("yyyy-MM-dd HH:mm:ss") + "',pk_emp_erase_chk='" + u.getPkEmp() + "',name_erase_chk='" + u.getNameEmp() + "' where ORDSN_PARENT=" + ord + "");
            exordSqlList.add("update EX_ORDER_OCC set EU_STATUS='9' where PK_CNORD in ("
                    +"select CO.PK_CNORD  from CN_ORDER CO where co.ORDSN_PARENT=" + ord + ")"
            );
        }
        //作废医嘱
        if (null != orderSqlList && orderSqlList.size() > 0)
        {DataBaseHelper.batchUpdate(orderSqlList.toArray(new String[0]));}
        if (!CollectionUtils.isEmpty(exordSqlList))
        { DataBaseHelper.batchUpdate(exordSqlList.toArray(new String[0]));}
    }

    /**
     * 根据状态获取对应的状态名称
     */
    private String getExecNameStatus(String indexStaus, int type) {
        String[] risstatus = new String[]{"申请", "提交", "预约", "检查", "报告"};
        String[] lisstatus = new String[]{"申请", "提交", "采集", "核收", "报告"};
        if (StringUtils.isBlank(indexStaus)) return "";
        int index = Integer.valueOf(indexStaus);
        if (0 == type && index < risstatus.length) {
            return risstatus[index];
        }
        if (1 == type && index < lisstatus.length) {
            return lisstatus[index];
        }
        return "";
    }

    /***
     * 判断患者是在本科室
     * @param pkPv  患者主键
     * @param pkDept 当前科室
     * @return
     */
    private boolean qryPvInfo(String pkPv, String pkDept) {
        boolean ifTransfer = true;
        String sql = "select * from PV_ENCOUNTER where pk_pv=?";
        PvEncounter pvInfo = DataBaseHelper.queryForBean(sql, PvEncounter.class, pkPv);
        ifTransfer = pvInfo.getPkDept().equals(pkDept); //患者是否转科
        if (ifTransfer) { //判断患者是否已发起转科申请
            sql = "select * from PV_ADT where PK_PV = ? and DATE_END is null and FLAG_ADMIT = '0'";
            PvAdt pvAdt = DataBaseHelper.queryForBean(sql, PvAdt.class, pkPv);
            if (pvAdt != null)
                ifTransfer = pvInfo.getPkDept().equals(pvAdt.getPkDept());//转科科室是否为当前科室
        }
        return ifTransfer;
    }
}
