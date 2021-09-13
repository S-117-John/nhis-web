package com.zebone.nhis.ma.pub.platform.send.impl.yh.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.Resource;
import com.zebone.nhis.common.module.emr.rec.rec.EmrLisResult;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdApplyDetail;
import com.zebone.nhis.common.module.ex.nis.ns.ExPdDe;

import com.zebone.nhis.common.module.pi.PiMaster;
import com.zebone.nhis.common.module.pv.PvEncounter;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd.BdPd;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CheckReport.CheckReportMsg;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CheckReport.CheckReportRow;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CnLabApplyAndOcc.CnLabApplyAndOccMsg;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CnLabApplyAndOcc.CnLabApplyAndOccRow;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CnLabApplyAndOccDetail.CnLabApplyAndOccDetailMsg;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.CnLabApplyAndOccDetail.CnLabApplyAndOccDetailRow;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.vo.BdPd.ExPdApply;
import com.zebone.platform.modules.exception.BusException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zebone.mq.msg.service.EwellMqHelper;
import com.zebone.mq.msg.service.MqMsgService;
import com.zebone.mq.msg.support.SDKCaller;
import com.zebone.nhis.common.module.base.ou.BdOuDept;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;
import com.zebone.nhis.common.module.cn.ipdw.BdUnit;
import com.zebone.nhis.common.module.cn.ipdw.CnOrder;
import com.zebone.nhis.common.support.ApplicationUtils;
import com.zebone.nhis.common.support.DateUtils;
import com.zebone.nhis.ma.pub.platform.send.impl.yh.dao.SendMsgMapper;
import com.zebone.platform.modules.dao.DataBaseHelper;

@Service
public class YHPlatFormAdtHandler {

    @Resource
    private EwellMqHelper ewellMqHelper;

    @Resource
    private SendMsgMapper sendMsgMapper;

    @Resource
    private MqMsgService mqMsgService;


    /**
     * 患者转科
     *
     * @param param
     * @throws Exception
     */
    @Transactional
    public void sendDeptInMsg(Map<String, Object> param) throws Exception {
        String path = this.getClass().getResource("../xml/PS10034.xml").getPath();
        BdOuDept dept = null;
        //用于接收替换后的消息
        String msgReplace = null;
        //Map<String,Object> result = sendMsgMapper.queryPiInfo(param.get("pkPv").toString());
        Map<String, String> map = new HashMap<String, String>();
        map.put("$patientId$", param.get("codePi").toString());//患者编码
        map.put("$admissTimes$", param.get("ipTimes").toString());//住院次数
        map.put("$tranDate$", DateUtils.formatDate(DateUtils.strToDate(param.get("dateBegin").toString(), "yyyy-MM-dd HH:mm:ss"), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//转科时间--时间格式确定
        dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{param.get("codeDept").toString()});

        if (dept != null) {
            map.put("$origDept$", dept.getCodeDept());//原科室
        } else {
            map.put("$origDept$", "");
        }
        dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{param.get("nsCodeDept").toString()});
        if (dept != null) {
            map.put("$origWard$", dept.getCodeDept());//原病区
        } else {
            map.put("$origWard$", "");
        }
        map.put("$origBed$", param.get("bedNo").toString());//原床位
        //根据科室主键得到科室编码
        dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{param.get("pkDept").toString()});
        if (dept != null) {
            map.put("$currDept$", dept.getCodeDept());//现科室
        } else {
            map.put("$currDept$", "");//现科室
        }
        dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{param.get("pkDeptNs").toString()});
        if (dept != null) {
            map.put("$currWard$", dept.getCodeDept());//现病区
        } else {
            map.put("$currWard$", "");//现病区
        }
        if (param.get("bedNoNow") != null) {
            map.put("$currBed$", param.get("codeIpBed").toString());//现床位
        }
        map.put("$currOpera$", param.get("currOpera").toString());//操作人
        msgReplace = ewellMqHelper.reBuildReq(path, map);
        //替换消息中的null值
        msgReplace = msgReplace.replace("null", "");
        //替换消息中的时间
        msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
        //记录日志
        mqMsgService.saveSysMsgResp(msgReplace, "PS10034", null, "");
        SDKCaller sdkCaller = new SDKCaller() {
            @Override
            public Object OnMessage(String msg, String msgid, String fid) {
                System.out.println(msg);
                System.out.println(msgid);
                return null;
            }
        };
        //消息发送
        String responseString = ewellMqHelper.getResponseString(msgReplace, "PS10034", "QMGR.P40_1");
        System.out.println(responseString);
    }

    /**
     * 患者出院
     *
     * @param param
     */
    @Transactional
    public void sendPvOutMsg(Map<String, Object> param) {
        String path = this.getClass().getResource("../xml/PS10035.xml").getPath();
        //用于接受替换后的消息
        String msgReplace = null;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("$patientId$", checkNull(param.get("codePi")));//患者编码
            map.put("$admissTimes$", checkNull(param.get("ipTimes")));//住院次数
            map.put("$disDate$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//出院时间--时间格式确定
            map.put("$currDept$", checkNull(param.get("codeDept")));//出院科室
            map.put("$currWard$", checkNull(param.get("nsCodeDept")));//出院病区
            map.put("$currBed$", checkNull(param.get("bedno")));//床位
            map.put("$currOpera$", checkNull(param.get("codeEmp")));//操作人
            msgReplace = ewellMqHelper.reBuildReq(path, map);
            //替换消息中的null值
            msgReplace = msgReplace.replace("null", "");
            //替换消息中的时间
            msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
            System.out.println(msgReplace);
            //记录日志
            mqMsgService.saveSysMsgResp(msgReplace, "PS10035", null, "");
            SDKCaller sdkCaller = new SDKCaller() {
                @Override
                public Object OnMessage(String msg, String msgid, String fid) {
                    System.out.println(msg);
                    System.out.println(msgid);
                    return null;
                }
            };
            //消息发送
            String responseString = ewellMqHelper.getResponseString(msgReplace, "PS10035", "QMGR.P40_1");
            System.out.println(responseString);
        } catch (Exception e) {
            e.printStackTrace();
            mqMsgService.saveSysMsgResp(msgReplace, "PS10035", null, "出现异常");
        }


    }

    /**
     * 发送检查
     *
     * @param orderList
     * @throws Exception
     */
    public void sendCnRisApplyMsg(List<CnOrder> orderList) throws Exception {
        //根据开关，开的话在进行消息处理

        String path = this.getClass().getResource("../xml/PS35017.xml").getPath();
        //用于接收替换后的消息
        //String msgReplace = null;

        //String replaceMsg = null;
        String msgReplace = "";
        String replaceMsg = "";
        //根据pk_pv得到患者id，就诊次数，住院号
        Map<String, Object> piInfo = DataBaseHelper.queryForMap("select ip.ip_times,en.code_pv,ma.code_pi from pv_ip ip "
                + "left join pv_encounter en on ip.pk_pv = en.pk_pv "
                + "left join pi_master ma on ma.pk_pi=en.pk_pi where ip.pk_pv=?", new Object[]{orderList.get(0).getPkPv()});
        BdOuDept dept = null;
        BdOuEmployee employee = null;
        for (CnOrder cn : orderList) {//2_307719-02-178
            String msg = "<Msg action=" + "\"insert\"" + "> "
                    + "<EXAM_SERIAL>$examSerial$</EXAM_SERIAL>"
                    + "<EXAM_TYPE>$examType$</EXAM_TYPE>"
                    + "<EXAM_SUB_TYPE>$examSubType$</EXAM_SUB_TYPE>"
                    + "<PATIENT_ID>$patientId$</PATIENT_ID>"
                    + "<TIMES>$times$</TIMES>"
                    + "<EXEC_UNIT>$execUnit$</EXEC_UNIT>"
                    + "<APPLY_DATE>$applyDate$</APPLY_DATE>"
                    + "<APPLY_DOCTOR>$applyDoctor$</APPLY_DOCTOR>"
                    + "<APPLY_UNIT>$applyUnit$</APPLY_UNIT>"
                    + "<CHARGE_NO>$chargeNo$</CHARGE_NO>"
                    + "<CHARGE_AMOUNT>$chargeAmount$</CHARGE_AMOUNT>"
                    + "<INPATIENT_NO>$inpatientNo$</INPATIENT_NO>"
                    + "<YZ_ACT_ORDER_NO>$yzActOrderNo$</YZ_ACT_ORDER_NO>"
                    + "<SCHEDULED_DATE>$scheduledDate$</SCHEDULED_DATE>"
                    + "<ORDER_CODE>$orderCode$</ORDER_CODE>"
                    + "<ORDER_NAME>$orderName$</ORDER_NAME>"
                    + "<ENTER_TIME>$enterTime$</ENTER_TIME>"
                    + "<CONFIRM_TIME>$confirmTime$</CONFIRM_TIME>"
                    + "<START_TIME>$startTime$</START_TIME>"
                    + "<ORDER_TYPE>$orderType$</ORDER_TYPE>"
                    + "<WARD_SN>$wardSn$</WARD_SN>"
                    + "<CONFIRM_OPERA>$confirmOpera$</CONFIRM_OPERA> </Msg>";
            msg = msg.replace("$examSerial$", String.valueOf(cn.getOrdsn().intValue() + Integer.valueOf("70000000").intValue()));//申请单号
            if (cn.getCodeOrd() != null) {//2_307719-02-178
                String[] type = cn.getCodeOrd().split("_");

                msg = msg.replace("$examType$", type[2]);//检查分类
                msg = msg.replace("$examSubType$", type[3]);//检查子分类
                msg = msg.replace("$orderCode$", type[1]);//医嘱编码
            } else {
                msg = msg.replace("$examType$", "");//检查分类
                msg = msg.replace("$examSubType$", "");//检查子分类
                msg = msg.replace("$orderCode$", "");//医嘱编码
            }
            msg = msg.replace("$patientId$", piInfo.get("codePi").toString());//患者id
            msg = msg.replace("$times$", piInfo.get("ipTimes").toString());//住院次数
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDeptExec()});
            if (dept != null) {
                msg = msg.replace("$execUnit$", dept.getCodeDept());//执行科室
            }
            msg = msg.replace("$applyDate$", DateUtils.formatDate(cn.getDateSign(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//申请时间--时间格式
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpInput()});
            if (employee != null) {
                msg = msg.replace("$applyDoctor$", employee.getCodeEmp());//申请医生
            }
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDept()});
            if (dept != null) {
                msg = msg.replace("$applyUnit$", dept.getCodeDept());//申请科室
            }
            msg = msg.replace("$chargeNo$", String.valueOf(cn.getOrdsn().intValue() + Integer.valueOf("800000000").intValue()));//医嘱号
            if (cn.getQuan() != null) {
                msg = msg.replace("$chargeAmount$", cn.getQuan().toString());//数量
            }
            msg = msg.replace("$inpatientNo$", piInfo.get("codePv").toString().split("_")[0]);//住院号
            msg = msg.replace("$yzActOrderNo$", cn.getOrdsn().toString());//医嘱号
            msg = msg.replace("$scheduledDate$", DateUtils.formatDate(cn.getDatePlanEx(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//预约时间
            msg = msg.replace("$orderName$", cn.getNameOrd());//医嘱名称
            msg = msg.replace("$enterTime$", DateUtils.formatDate(cn.getDateEnter(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱录入时间
            msg = msg.replace("$confirmTime$", DateUtils.formatDate(cn.getDateChk(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱确认时间
            msg = msg.replace("$startTime$", DateUtils.formatDate(cn.getDateStart(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱开始时间
            msg = msg.replace("$orderType$", cn.getCodeOrdtype().substring(1));//医嘱类型
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDeptNs()});
            msg = msg.replace("$wardSn$", dept.getCodeDept());//申请病区
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpChk()});
            if (employee != null) {
                msg = msg.replace("$confirmOpera$", employee.getCodeEmp());//确认医嘱护士工号
            }
            replaceMsg = replaceMsg + msg;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("MSGINFO", replaceMsg);
        //替换之后的消息
        msgReplace = ewellMqHelper.reBuildReq(path, map);
        //替换消息中的null值
        msgReplace = msgReplace.replace("null", "");
        //替换消息中的时间
        msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
        System.out.println(msgReplace);//输出至控制台
        //记录日志
        mqMsgService.saveSysMsgResp(msgReplace, "PS15011", null, "");
        SDKCaller sdkCaller = new SDKCaller() {
            @Override
            public Object OnMessage(String msg, String msgid, String fid) {
                return null;
            }
        };
        //消息发送
        String responseString = ewellMqHelper.getResponseString(msgReplace, "PS35017", "QMGR.P40_1");
        System.out.println(responseString);


    }

    /**
     * 发送检验
     *
     * @param orderList
     * @throws Exception
     */
    public void sendCnLisApplyMsg(List<CnOrder> orderList) throws Exception {

        String path = this.getClass().getResource("../xml/PS35018.xml").getPath();
        //用于接受替换后的消息
        String msgReplace = null;

        String replaceMsg = null;
        //根据pk_pv得到患者id，就诊次数，住院号
        Map<String, Object> piInfo = DataBaseHelper.queryForMap("select ip.ip_times,en.code_pv,ma.code_pi from pv_ip ip "
                + "left join pv_encounter en on ip.pk_pv = en.pk_pv "
                + "left join pi_master ma on ma.pk_pi=en.pk_pi where ip.pk_pv=?", new Object[]{orderList.get(0).getPkPv()});
        BdOuDept dept = null;
        BdOuEmployee employee = null;
        BdUnit unit = null;
        for (CnOrder cn : orderList) {
            String msg = "<Msg action=" + "\"insert\"" + "> "
                    + "<ORDER_NO>$orderNo$</ORDER_NO>"
                    + "<PATIENT_ID>$patientId$</PATIENT_ID>"
                    + "<ADMISS_TIMES>$admissTimes$</ADMISS_TIMES>"
                    + "<ORDER_CODE>$orderCode$</ORDER_CODE>"
                    + "<SERIAL>$serial$</SERIAL>"
                    + "<ENTER_TIME>$enterTime$</ENTER_TIME>"
                    + "<CONFIRM_TIME>$confirmTime$</CONFIRM_TIME>"
                    + "<START_TIME>$startTime$</START_TIME>"
                    + "<END_TIME>$endTime$</END_TIME>"
                    + "<ORDER_DOCTOR>$orderDoctor$</ORDER_DOCTOR>"
                    + "<STOP_DOCTOR>$stopDoctor$</STOP_DOCTOR>"
                    + "<CONFIRM_OPERA>$confirmOpera$</CONFIRM_OPERA>"
                    + "<STOP_CONFIRM_OPERA>$stopConfirmOpera$</STOP_CONFIRM_OPERA>"
                    + "<FREQU_CODE>$frequCode$</FREQU_CODE>"
                    + "<ORDER_TYPE>$orderType$</ORDER_TYPE>"
                    + "<SUPPLY_CODE>supplyCode</SUPPLY_CODE>"
                    + "<DRUG_SPECIFICATION>$drugSpecification$</DRUG_SPECIFICATION>"
                    + "<DOSEAGE>$doseage$</DOSEAGE>"
                    + "<DOSEAGE_UNIT>$doseageUnit$</DOSEAGE_UNIT>"
                    + "<CHARGE_AMOUNT>$chargeAmount$</CHARGE_AMOUNT>"
                    + "<DRUG_OCC>$drugOcc$</DRUG_OCC>"
                    + "<MINI_UNIT>$miniUnit$</MINI_UNIT>"
                    + "<PARENT_NO>$parentNo$</PARENT_NO>"
                    + "<FIT_FLAG>$fitFlag$</FIT_FLAG>"
                    + "<SELF_BUY>$selfBuy$</SELF_BUY>"
                    + "<INSTRUCTION>$instruction$</INSTRUCTION>"
                    + "<DEPT_SN>$deptSn$</DEPT_SN>"
                    + "<WARD_SN>$wardSn$</WARD_SN>"
                    + "<ORDER_NAME>$orderName$</ORDER_NAME>"
                    + "<EXEC_UNIT>$execUnit$</EXEC_UNIT> </Msg>";
            msg = msg.replace("$orderNo$", String.valueOf(cn.getOrdsn().intValue() + Integer.valueOf("800000000").intValue()));//医嘱号--要特殊处理
            msg = msg.replace("$patientId$", piInfo.get("codePi").toString());//患者id
            msg = msg.replace("$admissTimes$", piInfo.get("ipTimes").toString());//住院次数
            if (cn.getCodeOrd() != null && cn.getCodeOrd().contains("_")) {
                String[] type = cn.getCodeOrd().split("_");
                msg = msg.replace("$orderCode$", type[1]);//医嘱编码
                msg = msg.replace("$orderType$", type[0]);//医嘱类型
            } else {
                msg = msg.replace("$orderCode$", "**");//医嘱编码
                msg = msg.replace("$orderType$", "**");//医嘱类型
            }
            //得到药品包装
            if (cn.getFlagDurg() != null && "1".equals(cn.getFlagDurg())) {
                Map<String, Object> queryForMap = DataBaseHelper.queryForMap("SELECT (CASE WHEN DT.FLAG_PD = '1' THEN CAST(SUBSTRING(PD.CODE, CHARINDEX('_',PD.CODE) + 1, LEN(PD.CODE) - 1) AS VARCHAR) ELSE '**' END) AS SERIAL_NO"
                        + "FROM CN_ORDER AS ORD INNER JOIN BL_IP_DT AS DT ON DT.PK_CNORD = ORD.PK_CNORD "
                        + "LEFT OUTER JOIN BD_PD AS PD ON PD.PK_PD = DT.PK_ITEM where ord.pk_cnord=?", new Object[]{cn.getPkCnord()});
                msg = msg.replace("$serial$", queryForMap.get("serialNo").toString());//药品包装
                msg = msg.replace("$supplyCode$", cn.getCodeSupply());//给药方式
                msg = msg.replace("$drugSpecification$", cn.getSpec());//药品规格
            } else {
                msg = msg.replace("$serial$", "**");//药品包装
                msg = msg.replace("$supplyCode$", "**");//给药方式
                msg = msg.replace("$drugSpecification$", "**");//药品规格
            }
            msg = msg.replace("$enterTime$", DateUtils.formatDate(cn.getDateEnter(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//录入时间
            msg = msg.replace("$confirmTime$", DateUtils.formatDate(cn.getDateChk(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱确认时间
            msg = msg.replace("$startTime$", DateUtils.formatDate(cn.getDateStart(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱开始时间
            msg = msg.replace("$endTime$", DateUtils.formatDate(cn.getDateChk(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));//医嘱结束时间
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpInput()});
            if (employee != null) {
                msg = msg.replace("$orderDoctor$", employee.getCodeEmp());//开立医生工号
            }
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpInput()});
            if (employee != null) {
                msg = msg.replace("$stopDoctor$", employee.getCodeEmp());//停止医生工号
            }
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpChk()});
            if (employee != null) {
                msg = msg.replace("$confirmOpera$", employee.getCodeEmp());//确认护士工号
            }
            employee = DataBaseHelper.queryForBean("select code_emp from bd_ou_employee where pk_emp=? and del_flag='0'", BdOuEmployee.class, new Object[]{cn.getPkEmpStopChk()});
            if (employee != null) {
                msg = msg.replace("$stopConfirmOpera$", employee.getCodeEmp());//确认停止护士工号
            } else {
                msg = msg.replace("$stopConfirmOpera$", "**");//确认停止护士工号
            }

            msg = msg.replace("$frequCode$", cn.getCodeFreq());//频率


            if (cn.getDosage() != null) {
                msg = msg.replace("$doseage$", cn.getDosage().toString());//用药单次剂量

            }
            unit = DataBaseHelper.queryForBean("select code from bd_unit where pk_unit=? and del_flag='0'", BdUnit.class, new Object[]{cn.getPkUnitDos()});
            if (unit != null) {
                msg = msg.replace("$doseageUnit$", unit.getCode());//单次剂量单位
            } else {
                msg = msg.replace("$doseageUnit$", "**");//单次剂量单位
            }
            if (cn.getQuan() != null) {
                msg = msg.replace("$chargeAmount$", cn.getQuan().toString());//单次领量
            } else {
                msg = msg.replace("$chargeAmount$", "**");//单次领量
            }
            if (cn.getQuanCg() != null) {
                msg = msg.replace("$drugOcc$", cn.getQuanCg().toString());//每次用量
            } else {
                msg = msg.replace("$drugOcc$", "**");//每次用量
            }
            unit = DataBaseHelper.queryForBean("select code from bd_unit where pk_unit=? and del_flag='0'", BdUnit.class, new Object[]{cn.getPkUnit()});
            if (unit != null) {
                msg = msg.replace("$miniUnit$", unit.getCode());//每次用量单位
            } else {
                msg = msg.replace("$miniUnit$", "**");//每次用量单位
            }

            msg = msg.replace("$parentNo$", String.valueOf(cn.getOrdsnParent().intValue() + Integer.valueOf("800000000").intValue()));//父医嘱--需特殊处理
            msg = msg.replace("$fitFlag$", "1");//适应症标准
            msg = msg.replace("$selfBuy$", "0");//费用标志
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDept()});
            if (dept != null) {
                msg = msg.replace("$deptSn$", dept.getCodeDept());//申请科室
            }
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDeptNs()});
            if (dept != null) {
                msg = msg.replace("$wardSn$", dept.getCodeDept());//申请病区
            }
            dept = DataBaseHelper.queryForBean("select code_dept from bd_ou_dept where pk_dept=? and del_flag='0'", BdOuDept.class, new Object[]{cn.getPkDeptExec()});
            if (dept != null) {
                msg = msg.replace("$execUnit$", dept.getCodeDept());//执行科室
            }
            msg = msg.replace("$orderName$", cn.getNameOrd());//医嘱名称
            msg = msg.replace("$instruction$", "**");//医生嘱托描述
            replaceMsg = replaceMsg + msg;
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("$msg$", replaceMsg);
        msgReplace = ewellMqHelper.reBuildReq(path, map);
        //替换消息中的null值
        msgReplace = msgReplace.replace("null", "");
        //替换消息中的时间
        msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
        //记录日志
        mqMsgService.saveSysMsgResp(msgReplace, "PS35018", null, "");
        SDKCaller sdkCaller = new SDKCaller() {
            @Override
            public Object OnMessage(String msg, String msgid, String fid) {
                return null;
            }
        };
        //消息发送
        String responseString = ewellMqHelper.getResponseString(msgReplace, "PS35018", "QMGR.P40_1");
        System.out.println(responseString);


    }

    /**
     * 发送检查检验
     *
     * @param param
     * @throws Exception
     */
    @Transactional
    public void sendCnLisOrrRisApplyMsg(Map<String, Object> param) throws Exception {
        //根据开关，开的话在进行消息处理

         List<CnOrder> lisList = new ArrayList<CnOrder>();
        List<CnOrder> risList = new ArrayList<CnOrder>();
        //循环传进来的order集合
        for (String st : (List<String>) param.get("orderList")) {
            //根据医嘱主键查询具体信息
//            CnOrder cn = DataBaseHelper.queryForBean("select * from cn_order where pk_cnord=?", CnOrder.class, st);
            CnOrder cn = sendMsgMapper.queryCnOrder(st);
            if ("02".equals(cn.getCodeOrdtype())) {
                risList.add(cn);
            }
                if ("03".equals(cn.getCodeOrdtype())) {
                lisList.add(cn);
            }
        }
        if (lisList != null && lisList.size() > 0) {
            //调用发送检验
            sendCnLisApplyMsg(lisList);
        }
        if (risList != null && risList.size() > 0) {
            //调用发送检查
            sendCnRisApplyMsg(risList);
        }

    }

    /**
     * 发送检查检验费用信息
     *
     * @param param
     * @throws Exception
     */
    @Transactional
    public void sendCnMedApplyBlMsg(Map<String, Object> param) throws Exception {

        String path = this.getClass().getResource("../xml/PS15014.xml").getPath();
        //用于接受替换后的消息
        String msgReplace = null;

        String replaceMsg = "";
        //得到map中的医嘱集合
        List<String> orderList = (List<String>) param.get("orderList");
        BdOuDept dept = null;
        BdOuEmployee employee = null;
        for (String st : orderList) {
            //根据医嘱主键查询医嘱具体信息
            CnOrder cn = DataBaseHelper.queryForBean("select * from cn_order where pk_cnord=?", CnOrder.class, new Object[]{st});
            //费用信息
            List<Map<String, Object>> blInfoList = sendMsgMapper.getBlInfo(st);
            if (blInfoList != null && blInfoList.size() > 0) {
                for (Map<String, Object> blInfo : blInfoList) {
                    String msg = "<Msg action=" + "\"insert\"" + "> "
                            + "<PATIENT_ID>$patientId$</PATIENT_ID>"
                            + "<ADMISS_TIMES>$admissTimes$</ADMISS_TIMES>"
                            + "<OCC_TIMES>$occTimes$</OCC_TIMES>"
                            + "<HAPPEN_DATE>$happenDate$</HAPPEN_DATE>"
                            + "<CHARGE_CODE>$chargeCode$</CHARGE_CODE>"
                            + "<CHARGE_PRICE>$chargePrice$</CHARGE_PRICE>"
                            + "<CHARGE_AMOUNT>$chargeAmount$</CHARGE_AMOUNT>"
                            + "<DOCTOR_SN>$docterSn$</DOCTOR_SN>"
                            + "<YB_FLAG>$ybFlag$</YB_FLAG>"
                            + "<WARD_SN>$wardSn$</WARD_SN>"
                            + "<DEPT_SN>$deptSn$</DEPT_SN>"
                            + "<ORDER_NO>$orderNo$</ORDER_NO>"
                            + "<EXEC_UNIT>$execUnit$</EXEC_UNIT>"
                            + "<JZ_SN>$jzSn$</JZ_SN> </Msg>";

                    msg = msg.replace("$patientId$", checkNull(blInfo.get("codePi")));//患者id
                    msg = msg.replace("$admissTimes$", checkNull(blInfo.get("times")));//住院次数
                    msg = msg.replace("$occTimes$", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//计划执行时间
                    msg = msg.replace("$happenDate$", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//费用发生日期
                    msg = msg.replace("$chargeCode$", checkNull(blInfo.get("chargeCode")));//收费编码
//						msg = msg.replace("$confirmTime$", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));//确定时间
                    msg = msg.replace("$chargePrice$", checkNull(blInfo.get("chargePrice")));//单价
                    msg = msg.replace("$chargeAmount$", checkNull(blInfo.get("chargeAmount")));//数量
                    msg = msg.replace("$docterSn$", checkNull(blInfo.get("enterOpera")));//医生工号
                    msg = msg.replace("$ybFlag$", checkNull(blInfo.get("ybFlag")));//医保标记
                    msg = msg.replace("$wardSn$", checkNull(blInfo.get("deptnsdept")));//病区编码
                    msg = msg.replace("$deptSn$", checkNull(blInfo.get("deptapp")));//科室编码
                    msg = msg.replace("$orderNo$", checkNull(blInfo.get("chargeNo")));//医嘱号
                    msg = msg.replace("$execUnit$", checkNull(blInfo.get("deptex")));//执行科室编码
                    msg = msg.replace("$jzSn$", checkNull(blInfo.get("jzSn")));//急诊收费流水号
                    replaceMsg = replaceMsg + msg;
                }
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("$msg$", replaceMsg);
        msgReplace = ewellMqHelper.reBuildReq(path, map);
        //替换消息中的null值
        msgReplace = msgReplace.replace("null", "");
        //替换消息中的时间
        msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
        //记录日志
        mqMsgService.saveSysMsgResp(msgReplace, "PS15014", null, "");
        System.out.println(msgReplace);
        SDKCaller sdkCaller = new SDKCaller() {
            @Override
            public Object OnMessage(String msg, String msgid, String fid) {
                return null;
            }
        };
        //消息发送
        String responseString = ewellMqHelper.getResponseString(msgReplace, "PS15014", "QMGR.P40_1");
        System.out.println(responseString);


    }

    /**
     * 方法判空
     */
    private String checkNull(Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 医嘱作废
     *
     * @param param
     * @throws Exception
     */
    @Transactional
    public void sendCancleOrder(Map<String, Object> param) throws Exception {
        //根据开关，开的话在进行消息处理
        String sendMsgSwitch = ApplicationUtils.getPropertyValue("yhmsg.send.switch", "0");
        if (sendMsgSwitch != null && "1".equals(sendMsgSwitch)) {
            String path = this.getClass().getResource("../xml/PS35020.xml").getPath();
            String msgReplace = null;
            String replaceMsg = null;

            List<CnOrder> orderList = (List<CnOrder>) param.get("orderList");
            for (CnOrder co : orderList) {
                String msg = "<Msg action=" + "\"insert\"" + "> "
                        + "<ORDER_NO>$orderNo$</ORDER_NO> "
                        + "<PATIENT_ID>$patientId$</PATIENT_ID> "
                        + "<ADMISS_TIMES>$admissTimes$</ADMISS_TIMES> </Msg>";
                msg = msg.replace("$orderNo$", String.valueOf(co.getOrdsn().intValue() + Integer.valueOf("800000000").intValue()));
                msg = msg.replace("$patientId$", param.get("codePi").toString());
                msg = msg.replace("$admissTimes$", param.get("times").toString());

                replaceMsg = replaceMsg + msg;
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("$msg$", replaceMsg);//医嘱号
            msgReplace = ewellMqHelper.reBuildReq(path, map);
            //替换消息中的null值
            msgReplace = msgReplace.replace("null", "");
            //替换消息中的时间
            msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
            System.out.println(msgReplace);
            //记录日志
            mqMsgService.saveSysMsgResp(msgReplace, "PS35020", null, "");
            SDKCaller sdkCaller = new SDKCaller() {
                @Override
                public Object OnMessage(String msg, String msgid, String fid) {
                    return null;
                }
            };
            //消息发送
            String responseString = ewellMqHelper.getResponseString(msgReplace, "PS35020", "QMGR.P40_1");
            System.out.println(responseString);
        }
    }


    /**
     * 发送发药信息
     *
     * @param param
     * @throws Exception
     */
    public void sendDrugMsg(Map<String, Object> param) throws Exception {
        //根据开关，开的话在进行消息处理
        String path = this.getClass().getResource("../xml/PS05008.xml").getPath();
        //用于接受替换后的消息
        String msgReplace = null;

        String replaceMsg = "";

        //从map中获取请领明细和发药明细
        List<ExPdApplyDetail> exPdAppDetails = (List<ExPdApplyDetail>) param.get("exPdAppDetails");//请领明细
        List<ExPdDe> exPdDes = (List<ExPdDe>) param.get("deDruglist");//发药明细

        BdOuDept dept = null;
        BdOuEmployee employee = null;
        Integer inseri = 0;
        for (ExPdApplyDetail exPdApplyDetail :
                exPdAppDetails) {
            //查询请领信息
            ExPdApply exPdApply = sendMsgMapper.getExPdApply(exPdApplyDetail.getPkPdap());
            //获取病区编码
            String code_dept = sendMsgMapper.getCodeDept(exPdApply.getPkDeptAp());
            //查询药品信息
            BdPd bdPd = sendMsgMapper.getBdPd(exPdApplyDetail.getPkPd());
            //药品编码截取
            String hisbdcode = bdPd.getCode().split("_")[0];
            //药品的大小包装
            String hisspec = bdPd.getCode().split("_")[1];
            //获取发药科室
            String providecode = sendMsgMapper.getCodeDept(exPdApply.getPkDeptDe());
            String msg = "<Msg action=" + "\"insert\"" + "> "
                    + "<IN_SERI>$inseri$</IN_SERI>"
                    + "<APPLY_DATE>$applydate$</APPLY_DATE>"
                    + "<DRUG_CODE>$drugcode$</DRUG_CODE>"
                    + "<DRUG_SPEC>$drugspec$</DRUG_SPEC>"
                    + "<APPLY_NUMBER>$applynumber$</APPLY_NUMBER>"
                    + "<APPLY_NAME>$applyname$</APPLY_NAME>"
                    + "<NATION_CODE>$nationcode$</NATION_CODE>"
                    + "<APPLY_DRUGROOM>$applydrugroom$</APPLY_DRUGROOM>"
                    + "<PROVIDE_DRUGROOM>$providedrugroom$</PROVIDE_DRUGROOM>"
                    + "<APPLY_NO>$applyno$</APPLY_NO>"
                    + "<APPLY_DEPT>$applydept$</APPLY_DEPT>"
                    + "<APPLY_NS>$applyns$</APPLY_NS>"
                    + "<PRICE>$price$</PRICE>"
                    + "<FACTORY>$factory$</FACTORY>"
                    + "<Flag_CHARGE>$flagcharge$</Flag_CHARGE>"
                    + "<CONFIRM_NAME>$confirmname$</CONFIRM_NAME>"
                    + "<CONFIRM_STATUS>$confirmstatus$</CONFIRM_STATUS> </Msg>";
            System.out.println(exPdApplyDetail.getCreateTime());
            // msg = msg.replace("$applydate$", exPdApplyDetail.getCreateTime());//请领时间
            inseri = inseri + 1;
            msg = msg.replace("$inseri$", inseri.toString());
            msg = msg.replace("$applydate$", DateUtils.dateToStr("yyyy-MM-dd HH:mm:ss", exPdApply.getDateAp()));//请领时间
            msg = msg.replace("$drugcode$", checkNull(hisbdcode));//药品编码
            msg = msg.replace("$drugspec$", hisspec);//药品规格
            msg = msg.replace("$applynumber$", checkNull(exPdApplyDetail.getQuanPack()));//请领数量
            msg = msg.replace("$applyname$", checkNull(exPdApply.getNameEmpAp()));//请领人
            msg = msg.replace("$nationcode$", "2");//请领标志
            msg = msg.replace("$applydrugroom$", "");//请领药房
//						msg = msg.replace("$providedrugroom$", checkNull(exPdApply.getPkDeptDe()));//发药药房 2040108
            msg = msg.replace("$providedrugroom$", providecode);//发药药房(急诊药房)
            msg = msg.replace("$applyno$", checkNull(exPdApply.getCodeApply()));//请领单号
            msg = msg.replace("$applyns$", checkNull(code_dept));//请领病区
            msg = msg.replace("$applydept$", "1070000");//请领科室（急诊科）
            msg = msg.replace("$price$", "");//零售价
            msg = msg.replace("$factory$", "");//生产厂商
            msg = msg.replace("$flagcharge$", checkNull(exPdApply.getEuDirect()));//计费标志 1 是请领 -1 是请退
            msg = msg.replace("$confirmname$", "");
            msg = msg.replace("$confirmstatus$", "");
            replaceMsg = replaceMsg + msg;

        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("$msg$", replaceMsg);
        msgReplace = ewellMqHelper.reBuildReq(path, map);
        //替换消息中的null值
        msgReplace = msgReplace.replace("null", "");
        //替换消息中的时间
        msgReplace = msgReplace.replace("$date$", DateUtils.formatDate(new Date(), new Object[]{"yyyy-MM-dd HH:mm:ss"}));
        //记录日志
        mqMsgService.saveSysMsgResp(msgReplace, "PS05008", null, "");
        System.out.println(msgReplace);

        //消息发送
        String responseString = ewellMqHelper.getResponseString(msgReplace, "PS05008", "QMGR.P40_1");
        System.out.println(responseString);
    }


    /**
     * 获取检查报告
     * BS20001 检查报告 V3.10
     *
     * @param patindexNo 患者索引号  begin 查询开始时间  end 查询结束时间
     * @author fujw
     * @date 2018-12-16 11:39:5
     */
    public List<CheckReportRow> getCheckReport(String patindexNo, String begin, String end) {
        String path = this.getClass().getResource("../xml/BS20001.txt").getPath();
        String fid = "BS20001";
        List<String> checkList = null;
        List<CheckReportRow> rowList = new ArrayList<CheckReportRow>();
        try {
            String Req = ewellMqHelper.getFileString(path);


            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = df.format(date);
            Req = Req.replace("$patindexNo$", patindexNo).replace("$begin$", begin).replace("$end$", end)
                    .replace("$date$", dateStr);
            List<CheckReportMsg> msgs = ewellMqHelper.putReqAndGetRespForStr(Req, "BS20001", CheckReportMsg.class);

            try {

                for (CheckReportMsg msg : msgs) {
                    rowList.add(msg.getBody().getRow());
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return rowList;
    }

    /**
     * 获取检验报告
     * BS20003 检验报告 V3.10
     *
     * @param patindexNo 患者索引号  begin 查询开始时间  end 查询结束时间
     * @author fujw
     * @date 2018-12-19
     */
    public List<CnLabApplyAndOccRow> getCnLabApplyAndOcc(String patindexNo, String begin, String end) {
        List<CnLabApplyAndOccRow> cnLabApplyAndOccRows = new ArrayList<>();
        try {
            String path = this.getClass().getResource("../xml/BS20003.txt").getPath();
            String fid = "BS20003";
            String Req = ewellMqHelper.getFileString(path);
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = df.format(date);
            Req = Req.replace("$patindexNo$", patindexNo).replace("$begin$", begin).replace("$end$", end)
                    .replace("$date$", dateStr);

            List<CnLabApplyAndOccMsg> msgs = ewellMqHelper.putReqAndGetRespForStr(Req, "BS20003", CnLabApplyAndOccMsg.class);

            for (CnLabApplyAndOccMsg msg : msgs) {
                cnLabApplyAndOccRows.add(msg.getBody().getRow());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return cnLabApplyAndOccRows;
    }

    /**
     * 获取检验明细
     * BS20004 检验明细 V3.10
     *
     * @param electr_requistion_no
     * @author fujw
     * @date 2018-12-19
     */
    public List<CnLabApplyAndOccDetailRow> getCnLabApplyAndOccDetail(String electr_requistion_no) {
        String path = this.getClass().getResource("../xml/BS20004.txt").getPath();
        String fid = "BS20004";
        List<String> cnLabDetailList = null;
        List<CnLabApplyAndOccDetailRow> cnLabApplyAndOccDetailRows = new ArrayList<>();

        try {
            String Req = ewellMqHelper.getFileString(path);
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateStr = df.format(date);
            Req = Req.replace("$SAMPLE_TYPE_CODE$", electr_requistion_no)
                    .replace("$date$", dateStr);
            List<CnLabApplyAndOccDetailMsg> msgs = ewellMqHelper.putReqAndGetRespForStr(Req, "BS20004", CnLabApplyAndOccDetailMsg.class,100000);


            for (CnLabApplyAndOccDetailMsg msg : msgs) {


                cnLabApplyAndOccDetailRows.add(msg.getBody().getRow());

            }

        } catch (Exception e) {
            System.out.println("---------------------------------------  ----");
            e.printStackTrace();
            System.out.println("-------------------------------------------");

        }
        return cnLabApplyAndOccDetailRows;
    }

    /**
     * 检验结果
     *
     * @param map
     * @return List<EmrLisResult>
     */
    public List<Map<String, Object>> queryPatLisResult(Map<String, Object> map) {
        List<Map<String, Object>> EmrLisResults = new ArrayList<>();
        String pkPv = map.get("pkPv").toString();


            PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, pkPv);
            if (!"".equals(pvEncounter) && pvEncounter != null) {
                PiMaster piMaster = DataBaseHelper.queryForBean("select i.* from pv_encounter v LEFT JOIN  pi_master i on v.PK_PI=i.PK_PI where v.PK_PV=?", PiMaster.class, pkPv);
                if (!"".equals(piMaster) && piMaster != null) {

                    List<CnLabApplyAndOccDetailRow> details = new ArrayList<>();
                    //检验结果明细
//                    if (StringUtils.isNotBlank((String) map.get("smpNo"))) {
//                        details = this.getCnLabApplyAndOccDetail((String) map.get("smpNo"));
                    if (StringUtils.isNotBlank((String) map.get("smpNo"))) {
                        details = this.getCnLabApplyAndOccDetail((String) map.get("smpNo"));
                        for (CnLabApplyAndOccDetailRow Detail : details) {
                            HashMap<String, Object> emrMap = new HashMap<>();
                            EmrLisResult EmrLisResult = new EmrLisResult();
                            emrMap.put("pkPv",pkPv);
                            emrMap.put("pkOrg",pvEncounter.getPkOrg());
                            emrMap.put("range",Detail.getREFERENCERANGES());//范围
                            emrMap.put("itemName",Detail.getTestdetailitemcname());//项目名称
                            emrMap.put("unit",Detail.getTESTRESULTVALUEUNIT());//单位
                            emrMap.put("itemCode",Detail.getTestdetailitemcode());//项目编码
                            emrMap.put("mark",Detail.getNORMALFLAG());//标记
                            emrMap.put("result",Detail.getTESTRESULTVALUE());
                            emrMap.put("reqNo",Detail.getELECTRREQUISITIONNO());
                            String[] split = fSplit(Detail.getREFERENCERANGES(), "--", "<", ">");
                            emrMap.put("lowerLimit",split[0]);
                            emrMap.put("upperLimit",split[1]);
                            emrMap.put("testName",Detail.getTESTITEMNAME());

                            EmrLisResults.add(emrMap);
                        }
                    } else {
                        //查询检验结果
                        if (map.get("beginDate") != null || map.get("endDate") != null) {
                            String begin = map.get("beginDate").toString();
                            String end = map.get("endDate").toString();
                        List<CnLabApplyAndOccRow> cnLabApplyAndOccs = this.getCnLabApplyAndOcc(piMaster.getCodePi(), begin, end);
                        for (CnLabApplyAndOccRow row : cnLabApplyAndOccs) {
                            HashMap<String, Object> emrLisResult = new HashMap<>();
                            emrLisResult.put("pkPv",pkPv);
                            emrLisResult.put("pkOrg",pvEncounter.getPkOrg());
                            emrLisResult.put("testName",row.getTESTCATEGNAME());//检验大类项目名称
                            emrLisResult.put("itemName",row.getTESTCATEGNAME());
                            emrLisResult.put("itemCode",row.getTESTCATEGCODE());//检验大类项目编码
//                            emrLisResult.put("smpNo",row.getSAMPLENO());
                            emrLisResult.put("smpNo",row.getELECTRREQUISITIONNO());
//                            emrLisResult.setMark(row.getNORMALFLAG());//标记
                            emrLisResult.put("result",row.getCLINICDIAGNAME());
                            emrLisResult.put("reqNo",row.getELECTRREQUISITIONNO());
                            if (StringUtils.isNotEmpty(row.getEXECUTDATE())) {
                                emrLisResult.put("testDate",DateUtils.strToDate(row.getEXECUTDATE(), "yyyy-MM-dd'T'HH:mm:ss"));
                            } else {
                                emrLisResult.put("testDate",DateUtils.strToDate("0000-00-00T00:00:00", "yyyy-MM-dd'T'HH:mm:ss"));
                            }

                            EmrLisResults.add(emrLisResult);
                        }

                    }
                }
                return EmrLisResults;
            }

            return new ArrayList<Map<String, Object>>();

        } else {
            throw new BusException("查询结果为空");
        }





}


    /**
     * 检查结果
     *
     * @param map
     * @return List<JmEmrRisResult>
     */
    public List<Map<String, Object>> queryPatRisResult(Map<String, Object> map) {

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String pkPv = map.get("pkPv").toString();
//        User user1=(User)user;

        if (map.get("beginDate") != null || map.get("endDate") != null) {
            String begin = map.get("beginDate").toString();
            String end = map.get("endDate").toString();

            //PvEncounter pvEncounter = DataBaseHelper.queryForBean("select * from pv_encounter where pk_pv=?", PvEncounter.class, pkPv);
            PiMaster piMaster = DataBaseHelper.queryForBean("select i.* from pv_encounter v LEFT JOIN  pi_master i on v.PK_PI=i.PK_PI where v.PK_PV=?", PiMaster.class, pkPv);
            if ("".equals(piMaster) || piMaster == null) {
                throw new BusException("查询结果为空");
            }
            List<CheckReportRow> checkReport = this.getCheckReport(piMaster.getCodePi(), begin, end);


            for (CheckReportRow checkReportRow : checkReport) {
                Map<String, Object> emrRisResult = new HashMap<>();
                emrRisResult.put("checkName",checkReportRow.getEXAMITEMNAME());//检查名称
                emrRisResult.put("checkResult",checkReportRow.getEXAMDESCR());//检查结果
                emrRisResult.put("reqNo",checkReportRow.getELECTRREQUISITIONNO());//申请单号
                //jmEmrRisResult.setPkCnord(checkReportRow.getOrderno());//医嘱号
                emrRisResult.put("checkDiags",checkReportRow.getCLINICDIAGNAME());//诊断

                if (StringUtils.isNotEmpty(checkReportRow.getEXECUTDATE())) {
                    emrRisResult.put("checkDate",DateUtils.strToDate(checkReportRow.getEXECUTDATE(), "yyyyMMddHHmmss"));//检查时间
                    emrRisResult.put("dateCheck",checkReportRow.getEXECUTDATE());
                } else {
                    emrRisResult.put("checkDate",DateUtils.strToDate("00000000000000", "yyyyMMddHHmmss"));//检查时间
                    emrRisResult.put("dateCheck","00000000000000");//
                }

                emrRisResult.put("pkOrg",piMaster.getPkOrg());
                emrRisResult.put("pkPv",pkPv);
                list.add(emrRisResult);
            }
        }
        return list;
    }

    /**
     * 拆分范围得到最值
     *
     * @param str   待分割的字符串
     * @param regex 分割字符串
     * @param small 小于
     * @param big   大于
     * @return
     */
    private String[] fSplit(String str, String regex, String small, String big) {
        boolean contains = str.contains(regex);
        boolean smaller = str.contains(small);
        boolean bigger = str.contains(big);
        String[] strings = new String[2];
        if (contains) {
            strings = str.split(regex);

        }
        if (smaller) {
            strings = str.split(small);
            String flag = strings[0];
            strings[0] = "";
            strings[1] = flag;
        }
        if (bigger) {
            strings = str.split(big);
            strings[1] = "";
        }

        return strings;
    }

}
