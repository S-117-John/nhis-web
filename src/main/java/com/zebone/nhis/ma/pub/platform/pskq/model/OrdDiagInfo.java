package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

import java.util.Date;

public class OrdDiagInfo {


    @MetadataDescribe(id= "LHDE0021001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0021002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0021003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0021004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0021005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0021006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0021007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0021008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0021009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0021010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0021011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0021012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0021013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private Date dateOfBirth;

    @MetadataDescribe(id= "LHDE0021014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0021015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0021016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0021017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0021018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0021019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0021020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0021021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0021022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0021023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0021024",name = "诊断ID",eName = "DIAGNOSIS_ID")
    private String diagnosisId;

    @MetadataDescribe(id= "LHDE0021025",name = "诊断流水号",eName = "DIAGNOSIS_NO")
    private String diagnosisNo;

    @JSONField(format="yyyyMMdd'T'HHmmss")
    @MetadataDescribe(id= "LHDE0021026",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;

    @MetadataDescribe(id= "LHDE0021027",name = "诊断科室ID",eName = "DIAG_DEPT_ID")
    private String diagDeptId;

    @MetadataDescribe(id= "LHDE0021028",name = "诊断科室名称",eName = "DIAG_DEPT_NAME")
    private String diagDeptName;

    @MetadataDescribe(id= "LHDE0021029",name = "诊断病区ID",eName = "DIAG_WARD_ID")
    private String diagWardId;

    @MetadataDescribe(id= "LHDE0021030",name = "诊断病区名称",eName = "DIAG_WARD_NAME")
    private String diagWardName;

    @MetadataDescribe(id= "LHDE0021031",name = "诊断医生ID",eName = "DIAG_DOCTOR_ID")
    private String diagDoctorId;

    @MetadataDescribe(id= "LHDE0021032",name = "诊断医生名称",eName = "DIAG_DOCTOR_NAME")
    private String diagDoctorName;

    @MetadataDescribe(id= "LHDE0021033",name = "诊断分类代码",eName = "DIAG_CATEGORY_CODE")
    private String diagCategoryCode;

    @MetadataDescribe(id= "LHDE0021034",name = "诊断分类名称",eName = "DIAG_CATEGORY_NAME")
    private String diagCategoryName;

    @MetadataDescribe(id= "LHDE0021035",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;

    @MetadataDescribe(id= "LHDE0021036",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;

    @MetadataDescribe(id= "LHDE0021037",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;

    @MetadataDescribe(id= "LHDE0021038",name = "主诊标志",eName = "PRIMARY_DIAG_FLAG")
    private String primaryDiagFlag;

    @MetadataDescribe(id= "LHDE0021039",name = "待查标志",eName = "UNCERTAIN_FLAG")
    private String uncertainFlag;

    @MetadataDescribe(id= "LHDE0021040",name = "院感标志",eName = "INFECTION_FLAG")
    private String infectionFlag;

    @MetadataDescribe(id= "LHDE0021041",name = "院感上报标志",eName = "INFECTION_REPORT_FLAG")
    private String infectionReportFlag;

    @MetadataDescribe(id= "LHDE0021042",name = "入院病情代码",eName = "ADMISSION_CONDITION_CODE")
    private String admissionConditionCode;

    @MetadataDescribe(id= "LHDE0021043",name = "入院病情名称",eName = "ADMISSION_CONDITION_NAME")
    private String admissionConditionName;

    @MetadataDescribe(id= "LHDE0021044",name = "出院转归代码",eName = "DISCHARGE_STATUS_CODE")
    private String dischargeStatusCode;

    @MetadataDescribe(id= "LHDE0021045",name = "出院转归名称",eName = "DISCHARGE_STATUS_NAME")
    private String dischargeStatusName;

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

    public String getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(String diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public String getDiagnosisNo() {
        return diagnosisNo;
    }

    public void setDiagnosisNo(String diagnosisNo) {
        this.diagnosisNo = diagnosisNo;
    }

    public String getDiagDateTime() {
        return diagDateTime;
    }

    public void setDiagDateTime(String diagDateTime) {
        this.diagDateTime = diagDateTime;
    }

    public String getDiagDeptId() {
        return diagDeptId;
    }

    public void setDiagDeptId(String diagDeptId) {
        this.diagDeptId = diagDeptId;
    }

    public String getDiagDeptName() {
        return diagDeptName;
    }

    public void setDiagDeptName(String diagDeptName) {
        this.diagDeptName = diagDeptName;
    }

    public String getDiagWardId() {
        return diagWardId;
    }

    public void setDiagWardId(String diagWardId) {
        this.diagWardId = diagWardId;
    }

    public String getDiagWardName() {
        return diagWardName;
    }

    public void setDiagWardName(String diagWardName) {
        this.diagWardName = diagWardName;
    }

    public String getDiagDoctorId() {
        return diagDoctorId;
    }

    public void setDiagDoctorId(String diagDoctorId) {
        this.diagDoctorId = diagDoctorId;
    }

    public String getDiagDoctorName() {
        return diagDoctorName;
    }

    public void setDiagDoctorName(String diagDoctorName) {
        this.diagDoctorName = diagDoctorName;
    }

    public String getDiagCategoryCode() {
        return diagCategoryCode;
    }

    public void setDiagCategoryCode(String diagCategoryCode) {
        this.diagCategoryCode = diagCategoryCode;
    }

    public String getDiagCategoryName() {
        return diagCategoryName;
    }

    public void setDiagCategoryName(String diagCategoryName) {
        this.diagCategoryName = diagCategoryName;
    }

    public String getDiagCode() {
        return diagCode;
    }

    public void setDiagCode(String diagCode) {
        this.diagCode = diagCode;
    }

    public String getDiagName() {
        return diagName;
    }

    public void setDiagName(String diagName) {
        this.diagName = diagName;
    }

    public String getDiagDesc() {
        return diagDesc;
    }

    public void setDiagDesc(String diagDesc) {
        this.diagDesc = diagDesc;
    }

    public String getPrimaryDiagFlag() {
        return primaryDiagFlag;
    }

    public void setPrimaryDiagFlag(String primaryDiagFlag) {
        this.primaryDiagFlag = primaryDiagFlag;
    }

    public String getUncertainFlag() {
        return uncertainFlag;
    }

    public void setUncertainFlag(String uncertainFlag) {
        this.uncertainFlag = uncertainFlag;
    }

    public String getInfectionFlag() {
        return infectionFlag;
    }

    public void setInfectionFlag(String infectionFlag) {
        this.infectionFlag = infectionFlag;
    }

    public String getInfectionReportFlag() {
        return infectionReportFlag;
    }

    public void setInfectionReportFlag(String infectionReportFlag) {
        this.infectionReportFlag = infectionReportFlag;
    }

    public String getAdmissionConditionCode() {
        return admissionConditionCode;
    }

    public void setAdmissionConditionCode(String admissionConditionCode) {
        this.admissionConditionCode = admissionConditionCode;
    }

    public String getAdmissionConditionName() {
        return admissionConditionName;
    }

    public void setAdmissionConditionName(String admissionConditionName) {
        this.admissionConditionName = admissionConditionName;
    }

    public String getDischargeStatusCode() {
        return dischargeStatusCode;
    }

    public void setDischargeStatusCode(String dischargeStatusCode) {
        this.dischargeStatusCode = dischargeStatusCode;
    }

    public String getDischargeStatusName() {
        return dischargeStatusName;
    }

    public void setDischargeStatusName(String dischargeStatusName) {
        this.dischargeStatusName = dischargeStatusName;
    }
}
