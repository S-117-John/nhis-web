package com.zebone.nhis.webservice.zhongshan.service;

import com.alibaba.fastjson.JSONObject;
import com.zebone.nhis.cn.ipdw.support.Constants;
import com.zebone.nhis.cn.pub.service.BdSnService;
import com.zebone.nhis.common.module.base.bd.price.BdHp;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.CnLabApply;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.module.cn.ipdw.CnRisApply;
import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.common.module.pv.PvOp;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.BeanUtils;
import com.zebone.nhis.common.support.CommonUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.webservice.support.RespJson;
import com.zebone.nhis.webservice.zhongshan.dao.ThiIpCnWebMapper;
import com.zebone.nhis.webservice.zhongshan.vo.ThiPeBdOrd;
import com.zebone.nhis.webservice.zhongshan.vo.ThiPeDelPvOpRec;
import com.zebone.nhis.webservice.zhongshan.vo.ThiPePvOpCnOrd;
import com.zebone.nhis.webservice.zhongshan.vo.ThiPePvOpRec;
import com.zebone.platform.common.support.User;
import com.zebone.platform.common.support.UserContext;
import com.zebone.platform.modules.dao.DataBaseHelper;
import com.zebone.platform.modules.exception.BusException;
import com.zebone.platform.modules.utils.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Classname ZsbaPhysicalExaminationService
 * @Description 三方接口-体键接口
 * @Date 2021-06-10 16:41
 * @Created by wuqiang
 */
@Service
public class ZsbaPhysicalExaminationService {
    private Logger logger = LoggerFactory.getLogger("nhis.zsbaWebSrv");

    @Autowired
    private ZsbaOutpatientOpChargeService zsbaOutpatientOpChargeService;

    @Resource
    private ThiIpCnWebMapper thiIpCnWebMapper;

    @Resource
    private BdSnService bdSnService;

    /**
     * @return java.lang.String
     * @Description 14-生成门诊就诊记录
     * @auther wuqiang
     * @Date 2021-06-10
     * @Param [inf]
     */
    public String generaPvOpRec(String inf) {
        inf = zsbaOutpatientOpChargeService.getIniParam(inf);

        List<ThiPePvOpRec> thiPePvOpRecs = JsonUtil.readValue(inf, new TypeReference<List<ThiPePvOpRec>>() {
        });
        List<ThiPePvOpRec> thiPePvOpRecList=new ArrayList<>(thiPePvOpRecs.size());
        for (ThiPePvOpRec thiPePvOpRec : thiPePvOpRecs) {
            String ret;
            //校验参数
            if (thiPePvOpRec == null) {
                ret = "-1|入参为空 " + " ";
                return new RespJson(ret, false).toString();
            }
            Date dateReg = new Date();
            try {
                ret = ZsbaOutpatientOpChargeService.vilaFiled(thiPePvOpRec);
                if (!StringUtils.isWhitespace(ret)) {
                    logger.error(" 14号接口: -1|入参为空 " + thiPePvOpRec.toString());
                    return new RespJson("-1|" + ret, false).toString();
                }
                dateReg = DateUtils.parseDate(thiPePvOpRec.getDateReg());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                logger.error(" 14号接口 -1|参数检测错误 " + thiPePvOpRec.toString());
                return new RespJson("-1|参数检验错误", false).toString();
            } catch (ParseException e) {
                logger.error(" 14号接口 -1|时间转换错误 " + thiPePvOpRec.toString());
            }
            PiMaster piMaster = zsbaOutpatientOpChargeService.getPiMaster(thiPePvOpRec.getCodeOp());
            if (piMaster == null) {
                return new RespJson("-1|未找到有效患者记录", false).toString();
            }
            BdOuDept bdOuDept = zsbaOutpatientOpChargeService.getBdOuDeptNoWithOrg(null, thiPePvOpRec.getPkDept());
            if (bdOuDept == null) {
                return new RespJson("-1|未找到有效就诊科室记录", false).toString();
            }
            BdOuEmployee bdOuEmployee = zsbaOutpatientOpChargeService.getBdOuEmployee(null, thiPePvOpRec.getPkEmpReg());
            if (bdOuEmployee == null) {
                return new RespJson("-1|未找到有效登记人", false).toString();
            }
            BdHp bdHp = getBdHp(thiPePvOpRec.getPkHp());
            if (bdHp == null) {
                return new RespJson("-1|未找到有效医保计划", false).toString();
            }
            User user = new User();
            user.setPkOrg(bdOuEmployee.getPkOrg());
            user.setPkEmp(bdOuEmployee.getPkEmp());
            user.setNameEmp(bdOuEmployee.getNameEmp());
            UserContext.setUser(user);
            PvEncounter pvEncount = zsbaOutpatientOpChargeService.savePvEncount(14, piMaster, bdHp, bdOuDept, bdOuEmployee, dateReg);
            //新建患者门诊就诊属性
            PvOp pvOp = zsbaOutpatientOpChargeService.savePvOp(pvEncount);//门诊就诊属性
            //新建患者医保属性
            zsbaOutpatientOpChargeService.savePvInsurance(pvEncount);
            ThiPePvOpRec thiPePvOpRecReturn = new ThiPePvOpRec();
            thiPePvOpRecReturn.setPkPv(pvEncount.getPkPv());
            thiPePvOpRecReturn.setCodeOp(piMaster.getCodeOp());
            thiPePvOpRecReturn.setPvTimes(String.valueOf(pvOp.getOpTimes()));
            thiPePvOpRecList.add(thiPePvOpRecReturn);
        }
        return new RespJson("0|成功|" + JsonUtil.writeValueAsString(thiPePvOpRecList), true).toString();
    }

    private BdHp getBdHp(String pkHp) {
        String sql = "select top 1 PK_HP, NAME, CODE from BD_HP BH " +
                "where DEL_FLAG = '0' and PK_HP = ?  ";
        return DataBaseHelper.queryForBean(sql, BdHp.class, pkHp);
    }

    /**
     * @return java.lang.String
     * @Description 15-删除门诊就诊记录接口
     * @auther wuqiang
     * @Date 2021-06-10
     * @Param [inf]
     */
    public String deletePvOpRec(String inf) {
        String ret;
        ThiPeDelPvOpRec thiPeDelPvOpRec = JsonUtil.readValue(inf, ThiPeDelPvOpRec.class);
        //校验参数
        if (thiPeDelPvOpRec == null) {
            ret = "-1|入参为空 " + " ";
            return new RespJson(ret, false).toString();
        }
        Date dateReg = new Date();
        try {
            ret = ZsbaOutpatientOpChargeService.vilaFiled(thiPeDelPvOpRec);
            if (!StringUtils.isWhitespace(ret)) {
                logger.error(" 15号接口: -1|入参为空 " + thiPeDelPvOpRec.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
            dateReg = DateUtils.parseDate(thiPeDelPvOpRec.getDateCancel());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            logger.error(" 15号接口 -1|参数检测错误 " + thiPeDelPvOpRec.toString());
            return new RespJson("-1|参数检验错误", false).toString();
        } catch (ParseException e) {
            logger.error(" 15号接口 -1|时间转换错误 " + thiPeDelPvOpRec.toString());
        }
        String pkPv = thiPeDelPvOpRec.getPkPv();
        StringBuffer sql1 = new StringBuffer();
        sql1.append(" select * from bl_op_dt where flag_settle='1' and pk_pv=?  ");
        List<Map<String, Object>> result = DataBaseHelper.queryForList(sql1.toString(), new Object[]{pkPv});
        if (result != null && result.size() > 0) {
            throw new BusException("该患者已缴费，不能取消接诊！");
        }
        List<Map<String, Object>> result1 = DataBaseHelper.queryForList(" select * from PV_IP_NOTICE where pk_pv_op=? ", new Object[]{pkPv});
        if (result1 != null && result1.size() > 0) {
            throw new BusException("该患者已申请住院，不能取消接诊！");
        }
        try {
            //4664 【门诊医生站】取消就诊时物理删除患者对应的医嘱、病历、诊断等，删除对应的就诊记录
            //：医嘱，病历，诊断，收费明细，检查/检验申请单 物理删除,其他逻辑保持不变
            String sql = "select *\n" +
                    "from pv_encounter pv\n" +
                    "         inner join bl_settle st on pv.pk_pv = st.pk_pv\n" +
                    "         inner join bd_hp hp on st.pk_insurance = hp.pk_hp\n" +
                    "where st.dt_sttype = '01'\n" +
                    "  and st.eu_stresult = '0'\n" +
                    "  and pv.eu_pvtype in (1, 2, 4)\n" +
                    "and pv.PK_PV = ?\n" +
                    "order by st.date_st desc";
            List<Map<String, Object>> resultBlSettle = DataBaseHelper.queryForList(sql, new Object[]{pkPv});
            List<String> orderNo = new ArrayList<>();
            if (resultBlSettle.size() <= 0) {
                //获取检查检验申请单号
                sql = "select PK_CNORD from CN_ORDER where PK_PV = ?";
                orderNo = DataBaseHelper.queryForList(sql, String.class, new Object[]{pkPv});
                //更新就诊记录状态为9
                sql = "update PV_ENCOUNTER set EU_STATUS = '9' where PK_PV = ?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                //物理删除收费明细
                sql = "delete from bl_op_dt where PK_PV = ?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                //物理删除医嘱
                sql = "delete from CN_ORDER where PK_PV = ?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                sql = "delete from pv_doc where pk_pv=?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                //物理删除诊断
                sql = "delete from PV_DIAG where PK_PV = ?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                //清空预约记录就诊主键
                sql = "update SCH_APPT_PV set PK_PV=null where PK_PV = ?";
                DataBaseHelper.execute(sql, new Object[]{pkPv});

                //物理删除病历记录
                sql = "delete from cn_emr_op where pk_pv= ? ";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                sql = "delete from PV_DOC where pk_pv= ? ";
                DataBaseHelper.execute(sql, new Object[]{pkPv});
                orderNo.forEach(a -> {
                    String tempsql = "delete from CN_LAB_APPLY where PK_CNORD = ?";
                    DataBaseHelper.execute(tempsql, new Object[]{a});
                    tempsql = "delete from CN_RIS_APPLY where PK_CNORD = ?";
                    DataBaseHelper.execute(tempsql, new Object[]{a});
                });
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        //更新就诊状态，解锁
        int upCount = DataBaseHelper.update("update pv_encounter  set eu_status='9', pk_emp_phy=null, name_emp_phy=null, eu_locked='0' where pk_pv=? and eu_locked!='2'  ", new Object[]{pkPv});
        if (upCount > 0) {
            List<Map<String, Object>> opMap = DataBaseHelper.queryForList("select * from pv_op where pk_pv=?", new Object[]{pkPv});
            List<Map<String, Object>> erMap = DataBaseHelper.queryForList("select * from pv_er where pk_pv=?", new Object[]{pkPv});
            if (opMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy=?,name_emp_phy=? where pk_pv=?", new Object[]{opMap.get(0).get("pkDeptPv"), opMap.get(0).get("pkEmpPv"), opMap.get(0).get("nameEmpPv"), pkPv});
            } else if (erMap.size() > 0) {
                DataBaseHelper.update("update pv_encounter  set pk_dept=?,pk_emp_phy=?,name_emp_phy=? where pk_pv=?", new Object[]{erMap.get(0).get("pkDeptPv"), erMap.get(0).get("pkEmpPv"), erMap.get(0).get("nameEmpPv"), pkPv});
            }
        } else {
            return new RespJson("-1|取消接诊失败", false).toString();
        }
        return new RespJson("0|成功", false).toString();
    }

    /**
     * @return java.lang.String
     * @Description 16-体检医嘱录入接口
     * @auther wuqiang
     * @Date 2021-06-10
     * @Param [inf]
     */
    public String saveOpCnOrder(String inf) {
        Map<String, Object> map = JsonUtil.readValue(inf, Map.class);
        if (MapUtils.isEmpty(map)) {
            return new RespJson("-1|参数为空", false).toString();
        }
        String pkPv = MapUtils.getString(map, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            return new RespJson("-1|就诊主键不允许为空", false).toString();
        }  
        JSONObject jsonObject = JSONObject.parseObject(inf);
        List<ThiPePvOpCnOrd> thiPePvOpCnOrds = JsonUtil.readValue(jsonObject.getString("datalist"), new TypeReference<List<ThiPePvOpCnOrd>>(){});
        if (CollectionUtils.isEmpty(thiPePvOpCnOrds)) {
            return new RespJson("-1|datalist参数为空", false).toString();
        }
        PvEncounter pvEncounter = zsbaOutpatientOpChargeService.getPvconter(null, pkPv, "1");
        if (pvEncounter == null) {
            return new RespJson("-1|未找到有效的就诊记录，请核实患者是否就诊", false).toString();
        }
        if ("2".equals(pvEncounter.getEuLocked())) {
            return new RespJson("-1|患者正在收费，不允许开立医嘱", false).toString();
        }
        List<ThiPePvOpCnOrd> thiPePvOpCnOrdReturnList = new ArrayList<>(thiPePvOpCnOrds.size());
        for (ThiPePvOpCnOrd thiPePvOpCnOrd : thiPePvOpCnOrds) {
            String ret = null;
            try {
                ret = ZsbaOutpatientOpChargeService.vilaFiled(thiPePvOpCnOrd);
            } catch (IllegalAccessException e) {
                logger.error(" 14号接口: -1|入参为空 " + thiPePvOpCnOrd.toString());
                return new RespJson("-1|" + ret, false).toString();
            }
            if (StringUtils.isNotBlank(ret)) {
                return new RespJson("-1|" + ret, false).toString();
            }
            if (Integer.valueOf(thiPePvOpCnOrd.getQuan()) <= 0) {
                logger.error(" ZsbaOutpatientOpChargeService-OpCharge: -1|记费数量小于0 " + thiPePvOpCnOrd.toString());
                return new RespJson("-1|记费数量不允许小于0", false).toString();
            }
            ThiPeBdOrd thiPeBdOrd = thiIpCnWebMapper.getThiPeBdOrd(thiPePvOpCnOrd.getPkOrd(), thiPePvOpCnOrd.getCodeOrd());
            if (null == thiPeBdOrd && !BeanUtils.isNotNull(thiPeBdOrd)) {
                return new RespJson("-1|未找到有效医嘱项目，请检查医嘱主键参数是否正确", false).toString();
            }
            BdOuEmployee bdOuEmployeeInput = zsbaOutpatientOpChargeService.getBdOuEmployee(null, thiPePvOpCnOrd.getPkEmpInput());
            if (bdOuEmployeeInput==null){
                return new RespJson("-1|未找到有效录入人，请检查录入人参数是否正确", false).toString();
            }
            BdOuEmployee bdOuEmployee = zsbaOutpatientOpChargeService.getBdOuEmployee(null, thiPePvOpCnOrd.getPkEmpOrd());
            if (bdOuEmployee==null){
                return new RespJson("-1|未找到有效开立人，请检查开立人参数是否正确", false).toString();
            }
            BdOuDept bdOuDept= zsbaOutpatientOpChargeService.getBdOuDeptNoWithOrg(null,thiPePvOpCnOrd.getPkDept());
            if (bdOuDept==null){
                return new RespJson("-1|未找到有效开立科室，请检查开立科室参数是否正确", false).toString();
            }
            BdOuDept bdOuDeptExe= zsbaOutpatientOpChargeService.getBdOuDeptNoWithOrg(null,thiPePvOpCnOrd.getPkDeptExec());
            if (bdOuDeptExe==null){
                return new RespJson("-1|未找到有效执行科室，请检查执行科室参数是否正确", false).toString();
            }
            User user = new User();
            user.setPkOrg(bdOuEmployee.getPkOrg());
            user.setPkEmp(bdOuEmployee.getPkEmp());
            user.setNameEmp(bdOuEmployee.getNameEmp());
            UserContext.setUser(user);
            CnOrder cnOrder= saveCnorder(thiPePvOpCnOrd,pvEncounter,thiPeBdOrd,bdOuEmployeeInput,bdOuEmployee,bdOuDept,bdOuDeptExe);
            saveRisOrCnLabApply(cnOrder,thiPeBdOrd);
            zsbaOutpatientOpChargeService.saveBlOpDt(0.00D,pvEncounter,null,cnOrder,bdOuDeptExe,bdOuDept,bdOuEmployee,bdOuEmployee,bdOuDept);
            ThiPePvOpCnOrd thiPePvOpCnOrdRet=new ThiPePvOpCnOrd();
            thiPePvOpCnOrdRet.setPkCnord(cnOrder.getPkCnord());
            thiPePvOpCnOrdRet.setOrderid(thiPePvOpCnOrd.getOrderid());
            thiPePvOpCnOrdRet.setNameOrd(cnOrder.getNameOrd());
            thiPePvOpCnOrdReturnList.add(thiPePvOpCnOrdRet);
        }
        return new RespJson("0|成功|"+JsonUtil.writeValueAsString(thiPePvOpCnOrdReturnList), true).toString();
    }

    private void saveRisOrCnLabApply(CnOrder cnOrder, ThiPeBdOrd thiPeBdOrd) {
        if (cnOrder.getCodeOrdtype().startsWith(Constants.RIS_SRVTYPE)){
            CnRisApply cnRisApply=new CnRisApply();
            cnRisApply.setPkCnord(cnOrder.getPkCnord());
            cnRisApply.setDtRistype(cnOrder.getDtRistype());
            cnRisApply.setDescBody(thiPeBdOrd.getDtBody());
            cnRisApply.setEuStatus("0");//申请单状态
            cnRisApply.setFlagPrint2("0");
            cnRisApply.setFlagPrint("0");
            cnRisApply.setRisNotice(thiPeBdOrd.getDescAtt());//检查注意事项
            cnRisApply.setNoteDise(cnOrder.getNoteDise());//病情描述
            ApplicationUtils.setDefaultValue(cnRisApply, true);
            DataBaseHelper.insertBean(cnRisApply);
        }
        if (cnOrder.getCodeOrdtype().startsWith(Constants.LIS_SRVTYPE)){
            CnLabApply cnLabApply = new CnLabApply();
            cnLabApply.setPkCnord(cnOrder.getPkCnord());
            cnLabApply.setDtColtype(thiPeBdOrd.getDtColltype()); //采集方法
            cnLabApply.setDtSamptype(thiPeBdOrd.getDtSamptype());
            cnLabApply.setDtTubetype(thiPeBdOrd.getDtContype());
            cnLabApply.setDelFlag("0");
            cnLabApply.setEuStatus("0");//申请单状态
            cnLabApply.setNote("祥润接口写入");//备注
            cnLabApply.setPurpose(cnOrder.getPurpose());//检验目的
            cnLabApply.setPkOrg(cnOrder.getPkOrg());
            ApplicationUtils.setDefaultValue(cnLabApply, true);
            DataBaseHelper.insertBean(cnLabApply);
        }

    }

    private CnOrder saveCnorder(ThiPePvOpCnOrd thiPePvOpCnOrd, PvEncounter pvEncounter, ThiPeBdOrd thiPeBdOrd, BdOuEmployee bdOuEmployeeInput, BdOuEmployee bdOuEmployee, BdOuDept bdOuDept, BdOuDept bdOuDeptExe) {
        CnOrder cnOrder = new CnOrder();
        Date dateStart = new Date();
        Date dateEnter = new Date();
        try {
            dateStart= DateUtils.parseDate(thiPePvOpCnOrd.getDateStart());
            dateEnter= DateUtils.parseDate(thiPePvOpCnOrd.getDateStart());
        } catch (ParseException e) {
            logger.error("15号接口转化时间失败" + thiPePvOpCnOrd.toString());
        }
        int ordSn = bdSnService.getSerialNo("CN_ORDER", "ORDSN", 1, UserContext.getUser());
        cnOrder.setOrdsnChk(ordSn);
        cnOrder.setDescFit("0");
        cnOrder.setEuPvtype(pvEncounter.getEuPvtype());
        cnOrder.setPkPv(pvEncounter.getPkPv());
        cnOrder.setPkPi(pvEncounter.getPkPi());
        cnOrder.setDateEffe(new Date());
        cnOrder.setCodeOrdtype(thiPeBdOrd.getCodeOrdtype());
        cnOrder.setEuAlways("1");
        cnOrder.setOrdsn(ordSn);
        cnOrder.setOrdsnParent(ordSn);
        cnOrder.setPkOrd(thiPeBdOrd.getPkOrd());
        cnOrder.setCodeOrd(thiPeBdOrd.getCodeord());
        cnOrder.setQuanDisp(0.0D);
        cnOrder.setNameOrd(thiPeBdOrd.getName());
        cnOrder.setCodeApply(ApplicationUtils.getCode("0402"));
        //优先使用接口传入，没有则取基本数据维护，最后默认临时
        String codeFerq=StringUtils.isNotBlank(thiPePvOpCnOrd.getCodeFreq()) ? thiPePvOpCnOrd.getCodeFreq(): StringUtils.isBlank(thiPeBdOrd.getCodeFreq()) ? "QD" : thiPeBdOrd.getCodeFreq();
        cnOrder.setCodeFreq(codeFerq);
        cnOrder.setDosage(0.0D);
        Double quan = StringUtils.isBlank(thiPePvOpCnOrd.getQuan()) ? 1.0D : Double.valueOf(thiPePvOpCnOrd.getQuan());
        cnOrder.setQuan(quan);
        cnOrder.setQuanCg(quan);
        cnOrder.setPackSize(0.0D);
        cnOrder.setPriceCg(thiPeBdOrd.getPrice());
        cnOrder.setDays(1L);
        cnOrder.setOrds(1L);
        cnOrder.setPkOrgExec(bdOuDeptExe.getPkOrg());
        cnOrder.setPkDeptExec(bdOuDeptExe.getPkDept());
        cnOrder.setEuStatusOrd("0");
        cnOrder.setDateEnter(dateEnter);
        cnOrder.setDateStart(dateStart);
        cnOrder.setFlagBl("1");
        cnOrder.setPkDept(bdOuDept.getPkDept());
        cnOrder.setPkEmpInput(bdOuEmployeeInput.getPkEmp());
        cnOrder.setNameEmpInput(bdOuEmployeeInput.getNameEmp());
        cnOrder.setPkEmpOrd(bdOuEmployee.getPkEmp());
        cnOrder.setNameEmpOrd(bdOuEmployee.getNameEmp());
        cnOrder.setFlagDoctor("1");
        cnOrder.setPkEmpEntry(bdOuEmployee.getPkEmp());
        cnOrder.setNameEmpEntry(bdOuEmployee.getNameEmp());
        cnOrder.setGroupno(1);
        cnOrder.setFlagOcc("0");
        ApplicationUtils.setDefaultValue(cnOrder, true);
        DataBaseHelper.insertBean(cnOrder);
        return cnOrder;

    }

    /**
     * @return java.lang.String
     * @Description 17-体检医嘱作废接口
     * @auther wuqiang
     * @Date 2021-06-10
     * @Param [inf]
     */
    public String deleteOpCnOrder(String inf) {
        Map<String, Object> map = JsonUtil.readValue(inf, Map.class);
        if (MapUtils.isEmpty(map)) {
            return new RespJson("-1|参数为空", false).toString();
        }
        String pkPv = MapUtils.getString(map, "pkPv");
        if (StringUtils.isBlank(pkPv)) {
            return new RespJson("-1|就诊主键不允许为空", false).toString();
        }
        List<Map<String,Object>> thiPePvOpCnOrds = (List<Map<String, Object>>) MapUtils.getObject(map, "datalist");
        if (CollectionUtils.isEmpty(thiPePvOpCnOrds)) {
            return new RespJson("-1|datalist参数为空", false).toString();
        }
        List<String> pkCnords=thiPePvOpCnOrds.stream().map(m->MapUtils.getString(m,"pkCnord")).collect(Collectors.toList());

        String sql="DELETE BL_OP_DT where PK_CNORD in("+ CommonUtils.convertListToSqlInPart(pkCnords) +")and FLAG_SETTLE='0'";
        int cou=DataBaseHelper.execute(sql);
         if (cou<pkCnords.size()){
             return new RespJson("-1|删除失败，请核实是否已收费", false).toString();
         }
          sql="delete from CN_ORDER where PK_CNORD in ("+ CommonUtils.convertListToSqlInPart(pkCnords) +")";
          DataBaseHelper.execute(sql);
        sql="delete from CN_RIS_APPLY where PK_CNORD in ("+ CommonUtils.convertListToSqlInPart(pkCnords) +")";
        DataBaseHelper.execute(sql);
        sql="delete from  CN_LAB_APPLY where PK_CNORD in ("+ CommonUtils.convertListToSqlInPart(pkCnords) +")";
        DataBaseHelper.execute(sql);
        sql="delete from EX_ASSIST_OCC " +
                "where PK_ASSOCC in ( select EAOD.PK_ASSOCC from EX_ASSIST_OCC_DT EAOD where EAOD.PK_CNORD in ("+ CommonUtils.convertListToSqlInPart(pkCnords) +"))";
        DataBaseHelper.execute(sql);
        sql="delete from EX_ASSIST_OCC_DT where PK_CNORD in ("+ CommonUtils.convertListToSqlInPart(pkCnords) +")";
        DataBaseHelper.execute(sql);
        return new RespJson("0|成功", false).toString();
    }
}
