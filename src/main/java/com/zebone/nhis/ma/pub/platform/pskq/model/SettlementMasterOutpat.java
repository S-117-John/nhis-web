package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.TypeCode;

public class SettlementMasterOutpat {

    @MetadataDescribe(id= "LHDE0048001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0048002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0048003",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0048004",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0048005",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0048006",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0048007",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0048008",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0048009",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0048010",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0048011",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0048012",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0048013",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0048014",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0048015",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;

    @MetadataDescribe(id= "LHDE0048016",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;

    @MetadataDescribe(id= "LHDE0048017",name = "结算主记录ID",eName = "SETTLEMENT_MASTER_ID")
    private String settlementMasterId;

    @MetadataDescribe(id= "LHDE0048018",name = "结算单号",eName = "SETTLEMENT_NO")
    private String settlementNo;

    @MetadataDescribe(id= "LHDE0048019",name = "结算次数",eName = "SETTLEMENT_TIMES")
    private String settlementTimes;

    @TypeCode(value = "code")
    @MetadataDescribe(id= "LHDE0048020",name = "结算类型代码",eName = "ACCOUNT_TYPE_CODE")
    private String accountTypeCode;

    @TypeCode(value = "name")
    @MetadataDescribe(id= "LHDE0048021",name = "结算类型名称",eName = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;

    @MetadataDescribe(id= "LHDE0048022",name = "结算日期时间",eName = "SETTLEMENT_DATE_TIME")
    private String settlementDateTime;

    @MetadataDescribe(id= "LHDE0048023",name = "结算人ID",eName = "SETTLEMENT_OPERA_ID")
    private String settlementOperaId;

    @MetadataDescribe(id= "LHDE0048024",name = "结算人姓名",eName = "SETTLEMENT_OPERA_NAME")
    private String settlementOperaName;

    @MetadataDescribe(id= "LHDE0048025",name = "结算窗口号",eName = "SETTLEMENT_WIN_NO")
    private String settlementWinNo;

    @MetadataDescribe(id= "LHDE0048026",name = "结算总费用",eName = "TOTAL_FEE")
    private String totalFee;

    @MetadataDescribe(id= "LHDE0048027",name = "费用状态",eName = "CHARGE_STATUS")
    private String chargeStatus;

    @MetadataDescribe(id= "LHDE0048028",name = "医保支付费用",eName = "MI_PAYMENT_FEE")
    private String miPaymentFee;

    @MetadataDescribe(id= "LHDE0048029",name = "个人支付费用",eName = "SELF_PAYMENT_FEE")
    private String selfPaymentFee;

    @MetadataDescribe(id= "LHDE0048030",name = "支付单号",eName = "PAYMENT_NO")
    private String paymentNo;

    @MetadataDescribe(id= "LHDE0048031",name = "子系统",eName = "SUB_SYSTEM_CODE")
    private String subSystemCode;

    @MetadataDescribe(id= "LHDE0048032",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;
    
    @MetadataDescribe(id= "LHDE0048033",name = "是否医保",eName = "IS_INSURANCE")
    private String isInsurance;

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
        switch(genderCode){
            case "02":
                this.genderCode = "1";
                break;
            case "03":
                this.genderCode = "2";
                break;        
            default:
                this.genderCode = "9";
                break;
        }
    }
    public String getGenderName() {
        return genderName;
    }

    public void setGenderName(String genderName) {
    	String name = "未说明的性别";
        switch(genderName){
            case "男":
                name = "男性";
                break;
            case "女":
                name = "女性";
                break;
  
        }
        this.genderName = name;
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

    public String getSettlementMasterId() {
        return settlementMasterId;
    }

    public void setSettlementMasterId(String settlementMasterId) {
        this.settlementMasterId = settlementMasterId;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public String getSettlementTimes() {
        return settlementTimes;
    }

    public void setSettlementTimes(String settlementTimes) {
        this.settlementTimes = settlementTimes;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountTypeName() {
        return accountTypeName;
    }

    public void setAccountTypeName(String accountTypeName) {
        this.accountTypeName = accountTypeName;
    }

    public String getSettlementDateTime() {
        return settlementDateTime;
    }

    public void setSettlementDateTime(String settlementDateTime) {
        this.settlementDateTime = settlementDateTime;
    }

    public String getSettlementOperaId() {
        return settlementOperaId;
    }

    public void setSettlementOperaId(String settlementOperaId) {
        this.settlementOperaId = settlementOperaId;
    }

    public String getSettlementOperaName() {
        return settlementOperaName;
    }

    public void setSettlementOperaName(String settlementOperaName) {
        this.settlementOperaName = settlementOperaName;
    }

    public String getSettlementWinNo() {
        return settlementWinNo;
    }

    public void setSettlementWinNo(String settlementWinNo) {
        this.settlementWinNo = settlementWinNo;
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

    public String getMiPaymentFee() {
        return miPaymentFee;
    }

    public void setMiPaymentFee(String miPaymentFee) {
        this.miPaymentFee = miPaymentFee;
    }

    public String getSelfPaymentFee() {
        return selfPaymentFee;
    }

    public void setSelfPaymentFee(String selfPaymentFee) {
        this.selfPaymentFee = selfPaymentFee;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
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

	public String getIsInsurance() {
		return isInsurance;
	}

	public void setIsInsurance(String isInsurance) {
		this.isInsurance = isInsurance;
	}
    
}
