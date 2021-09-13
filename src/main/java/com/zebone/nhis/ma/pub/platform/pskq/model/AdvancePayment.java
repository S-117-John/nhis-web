package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

public class AdvancePayment {


    @MetadataDescribe(id= "LHDE0058001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0058002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0058003",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0058004",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0058005",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0058006",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0058007",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0058008",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0058009",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0058010",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0058011",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0058012",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0058013",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0058014",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0058015",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;

    @MetadataDescribe(id= "LHDE0058016",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;

    @MetadataDescribe(id= "LHDE0058017",name = "预交金充值记录ID",eName = "ADVANCE_MASTER_ID")
    private String advanceMasterId;

    @MetadataDescribe(id= "LHDE0058018",name = "预交金充值单号",eName = "ADVANCE_PAYMENT_NO")
    private String advancePaymentNo;

    @MetadataDescribe(id= "LHDE0058019",name = "充值类型代码",eName = "ADVANCE_TYPE_CODE")
    private String advanceTypeCode;

    @MetadataDescribe(id= "LHDE0058020",name = "充值类型名称",eName = "ADVANCE_TYPE_NAME")
    private String advanceTypeName;

    @MetadataDescribe(id= "LHDE0058021",name = "充值日期时间",eName = "ADVANCE_DATE_TIME")
    private String advanceDateTime;

    @MetadataDescribe(id= "LHDE0058022",name = "操作员ID",eName = "ADVANCE_OPERA_ID")
    private String advanceOperaId;

    @MetadataDescribe(id= "LHDE0058023",name = "操作员姓名",eName = "ADVANCE_OPERA_NAME")
    private String advanceOperaName;

    @MetadataDescribe(id= "LHDE0058024",name = "操作员窗口号",eName = "ADVANCE_WIN_NO")
    private String advanceWinNo;

    @MetadataDescribe(id= "LHDE0058025",name = "充值总费用",eName = "TOTAL_FEE")
    private String totalFee;

    @MetadataDescribe(id= "LHDE0058026",name = "费用状态",eName = "CHARGE_STATUS")
    private String chargeStatus;

    @MetadataDescribe(id= "LHDE0058027",name = "充值订单号",eName = "PAYMENT_ORDER_NO")
    private String paymentOrderNo;

    @MetadataDescribe(id= "LHDE0058028",name = "充值交易流水号",eName = "TRANSACTION_SERIAL_NO")
    private String transactionSerialNo;

    @MetadataDescribe(id= "LHDE0058029",name = "支付平台终端号",eName = "PAY_TERMINAL_NO")
    private String payTerminalNo;

    @MetadataDescribe(id= "LHDE0058030",name = "自助机终端编号",eName = "MACHINE_NO")
    private String machineNo;

    @MetadataDescribe(id= "LHDE0058031",name = "子系统",eName = "SUB_SYSTEM_CODE")
    private String subSystemCode;

    @MetadataDescribe(id= "LHDE0058032",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;

    @MetadataDescribe(id= "LHDE0058033",name = "充值发票明细ID",eName = "ADVANCE_DETAIL_ID")
    private String advanceDetailId;

    @MetadataDescribe(id= "LHDE0058034",name = "发票分类代码",eName = "RCPT_CLASS_CODE")
    private String rcptClassCode;

    @MetadataDescribe(id= "LHDE0058035",name = "发票分类名称",eName = "RCPT_CLASS_NAME")
    private String rcptClassName;

    @MetadataDescribe(id= "LHDE0058036",name = "HIS生成唯一编码",eName = "HIS_ORDER_NO")
    private String hisOrderNo;



    public String getEmpiId() {
        return empiId;
    }

    public void setEmpiId(String empiId) {
        this.empiId = empiId;
    }

    public String getPkPatient() {
        return pkPatient;
    }

    public void setPkPatient(String pkPatient) {
        this.pkPatient = pkPatient;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getEncounterTypeCode() {
        return encounterTypeCode;
    }

    public void setEncounterTypeCode(String encounterTypeCode) {
        this.encounterTypeCode = encounterTypeCode;
    }

    public String getEncounterTypeName() {
        return encounterTypeName;
    }

    public void setEncounterTypeName(String encounterTypeName) {
        this.encounterTypeName = encounterTypeName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(String genderCode) {
        this.genderCode = genderCode;
    }

    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAgeYear() {
        return ageYear;
    }

    public void setAgeYear(String ageYear) {
        this.ageYear = ageYear;
    }

    public String getAgeMonth() {
        return ageMonth;
    }

    public void setAgeMonth(String ageMonth) {
        this.ageMonth = ageMonth;
    }

    public String getAgeDay() {
        return ageDay;
    }

    public void setAgeDay(String ageDay) {
        this.ageDay = ageDay;
    }

    public String getAgeHour() {
        return ageHour;
    }

    public void setAgeHour(String ageHour) {
        this.ageHour = ageHour;
    }

    public String getPatientTypeCode() {
        return patientTypeCode;
    }

    public void setPatientTypeCode(String patientTypeCode) {
        this.patientTypeCode = patientTypeCode;
    }

    public String getPatientTypeName() {
        return patientTypeName;
    }

    public void setPatientTypeName(String patientTypeName) {
        this.patientTypeName = patientTypeName;
    }

    public String getAdvanceMasterId() {
        return advanceMasterId;
    }

    public void setAdvanceMasterId(String advanceMasterId) {
        this.advanceMasterId = advanceMasterId;
    }

    public String getAdvancePaymentNo() {
        return advancePaymentNo;
    }

    public void setAdvancePaymentNo(String advancePaymentNo) {
        this.advancePaymentNo = advancePaymentNo;
    }

    public String getAdvanceTypeCode() {
        return advanceTypeCode;
    }

    public void setAdvanceTypeCode(String advanceTypeCode) {
        this.advanceTypeCode = advanceTypeCode;
    }

    public String getAdvanceTypeName() {
        return advanceTypeName;
    }

    public void setAdvanceTypeName(String advanceTypeName) {
        this.advanceTypeName = advanceTypeName;
    }

    public String getAdvanceDateTime() {
        return advanceDateTime;
    }

    public void setAdvanceDateTime(String advanceDateTime) {
        this.advanceDateTime = advanceDateTime;
    }

    public String getAdvanceOperaId() {
        return advanceOperaId;
    }

    public void setAdvanceOperaId(String advanceOperaId) {
        this.advanceOperaId = advanceOperaId;
    }

    public String getAdvanceOperaName() {
        return advanceOperaName;
    }

    public void setAdvanceOperaName(String advanceOperaName) {
        this.advanceOperaName = advanceOperaName;
    }

    public String getAdvanceWinNo() {
        return advanceWinNo;
    }

    public void setAdvanceWinNo(String advanceWinNo) {
        this.advanceWinNo = advanceWinNo;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getPaymentOrderNo() {
        return paymentOrderNo;
    }

    public void setPaymentOrderNo(String paymentOrderNo) {
        this.paymentOrderNo = paymentOrderNo;
    }

    public String getTransactionSerialNo() {
        return transactionSerialNo;
    }

    public void setTransactionSerialNo(String transactionSerialNo) {
        this.transactionSerialNo = transactionSerialNo;
    }

    public String getPayTerminalNo() {
        return payTerminalNo;
    }

    public void setPayTerminalNo(String payTerminalNo) {
        this.payTerminalNo = payTerminalNo;
    }

    public String getMachineNo() {
        return machineNo;
    }

    public void setMachineNo(String machineNo) {
        this.machineNo = machineNo;
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }

    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode;
    }

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }

    public String getAdvanceDetailId() {
        return advanceDetailId;
    }

    public void setAdvanceDetailId(String advanceDetailId) {
        this.advanceDetailId = advanceDetailId;
    }

    public String getRcptClassCode() {
        return rcptClassCode;
    }

    public void setRcptClassCode(String rcptClassCode) {
        this.rcptClassCode = rcptClassCode;
    }

    public String getRcptClassName() {
        return rcptClassName;
    }

    public void setRcptClassName(String rcptClassName) {
        this.rcptClassName = rcptClassName;
    }

    public String getHisOrderNo() {
        return hisOrderNo;
    }

    public void setHisOrderNo(String hisOrderNo) {
        this.hisOrderNo = hisOrderNo;
    }

}
