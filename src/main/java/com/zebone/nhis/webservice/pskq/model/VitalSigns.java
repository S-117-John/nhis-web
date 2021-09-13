package com.zebone.nhis.webservice.pskq.model;



import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

import java.util.Date;

/**
 * 生命体征
 * @author 卡卡西
 */
public class VitalSigns {

    @MetadataDescribe(id= "LHDE0028001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0028002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;


    @MetadataDescribe(id= "LHDE0028003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0028004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0028005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0028006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0028007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0028008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0028009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0028010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0028011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0028012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0028013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @MetadataDescribe(id= "LHDE0028014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0028015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0028016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0028017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0028018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0028019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0028020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0028021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0028022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0028023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0028023",name = "生命体征记录ID",eName = "VITAL_SIGNS_ID")
    private String vitalSignsId;

    @MetadataDescribe(id= "LHDE0028023",name = "生命体征记录序号",eName = "VITAL_SIGNS_NO")
    private String vitalSignsNo;

    @MetadataDescribe(id= "LHDE0028023",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0028023",name = "体征代码",eName = "SIGN_CODE")
    private String signCode;

    @MetadataDescribe(id= "LHDE0028023",name = "体征名称",eName = "SIGN_NAME")
    private String signName;

    @MetadataDescribe(id= "LHDE0028023",name = "测量方法代码",eName = "MEASURE_METHOD_CODE")
    private String measureMethodCode;

    @MetadataDescribe(id= "LHDE0028023",name = "测量方法名称",eName = "MEASURE_METHOD_NAME")
    private String measureMethodName;

    @MetadataDescribe(id= "LHDE0028023",name = "指标数值",eName = "MEASURE_VALUE")
    private String measureValue;

    @MetadataDescribe(id= "LHDE0028023",name = "指标单位代码",eName = "SIGN_UNIT_CODE")
    private String signUnitCode;

    @MetadataDescribe(id= "LHDE0028023",name = "指标单位名称",eName = "SIGN_UNIT_NAME")
    private String signUnitName;

    @MetadataDescribe(id= "LHDE0028023",name = "实际测量日期时间",eName = "MEASURE_DATE_TIME")
    private String measureDateTime;

    @MetadataDescribe(id= "LHDE0028023",name = "测量人ID",eName = "MEASURE_OPERA_ID")
    private String measureOperaId;

    @MetadataDescribe(id= "LHDE0028023",name = "测量人姓名",eName = "MEASURE_OPERA_NAME")
    private String measureOperaName;

    @MetadataDescribe(id= "LHDE0028023",name = "年龄-分",eName = "AGE_MINUTE")
    private String ageMinute;

    @MetadataDescribe(id= "LHDE0028023",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;


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

    public String getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(String encounterId) {
        this.encounterId = encounterId;
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

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getVisitNo() {
        return visitNo;
    }

    public void setVisitNo(String visitNo) {
        this.visitNo = visitNo;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
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

    public String getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(String visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getWardId() {
        return wardId;
    }

    public void setWardId(String wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getVitalSignsId() {
        return vitalSignsId;
    }

    public void setVitalSignsId(String vitalSignsId) {
        this.vitalSignsId = vitalSignsId;
    }

    public String getVitalSignsNo() {
        return vitalSignsNo;
    }

    public void setVitalSignsNo(String vitalSignsNo) {
        this.vitalSignsNo = vitalSignsNo;
    }

    public String getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(String enterDateTime) {
        this.enterDateTime = enterDateTime;
    }

    public String getSignCode() {
        return signCode;
    }

    public void setSignCode(String signCode) {
        this.signCode = signCode;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getMeasureMethodCode() {
        return measureMethodCode;
    }

    public void setMeasureMethodCode(String measureMethodCode) {
        this.measureMethodCode = measureMethodCode;
    }

    public String getMeasureMethodName() {
        return measureMethodName;
    }

    public void setMeasureMethodName(String measureMethodName) {
        this.measureMethodName = measureMethodName;
    }

    public String getMeasureValue() {
        return measureValue;
    }

    public void setMeasureValue(String measureValue) {
        this.measureValue = measureValue;
    }

    public String getSignUnitCode() {
        return signUnitCode;
    }

    public void setSignUnitCode(String signUnitCode) {
        this.signUnitCode = signUnitCode;
    }

    public String getSignUnitName() {
        return signUnitName;
    }

    public void setSignUnitName(String signUnitName) {
        this.signUnitName = signUnitName;
    }

    public String getMeasureDateTime() {
        return measureDateTime;
    }

    public void setMeasureDateTime(String measureDateTime) {
        this.measureDateTime = measureDateTime;
    }

    public String getMeasureOperaId() {
        return measureOperaId;
    }

    public void setMeasureOperaId(String measureOperaId) {
        this.measureOperaId = measureOperaId;
    }

    public String getMeasureOperaName() {
        return measureOperaName;
    }

    public void setMeasureOperaName(String measureOperaName) {
        this.measureOperaName = measureOperaName;
    }

    public String getAgeMinute() {
        return ageMinute;
    }

    public void setAgeMinute(String ageMinute) {
        this.ageMinute = ageMinute;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }
}
