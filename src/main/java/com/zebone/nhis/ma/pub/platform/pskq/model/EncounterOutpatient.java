package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 *
 *  就诊信息(门诊)消息模型
 */
public class EncounterOutpatient {
    @MetadataDescribe(id= "LHSD0005009",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHSD0016028",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHSD0005015",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHSD0015034",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHSD0015035",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHSD0005016",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHSD0005017",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHSD0022007",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHSD0022008",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHSD0016014",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHSD0007001",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHSD0007002",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHSD0004005",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHSD0001030",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHSD0001029",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHSD0001027",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHSD0001028",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHSD0013001",name = "婚姻状况代码",eName = "MARITAL_STATUS_CODE")
    private String maritalStatusCode;

    @MetadataDescribe(id= "LHSD0013002",name = "婚姻状况名称",eName = "MARITAL_STATUS_NAME")
    private String maritalStatusName;

    @MetadataDescribe(id= "LHSD0016015",name = "病人来源代码",eName = "PATIENT_RESOURCE_CODE")
    private String patientResourceCode;

    @MetadataDescribe(id= "LHSD0016016",name = "病人来源名称",eName = "PATIENT_RESOURCE_NAME")
    private String patientResourceName;

    @MetadataDescribe(id= "LHSD0016017",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;

    @MetadataDescribe(id= "LHSD0016018",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;

    @MetadataDescribe(id= "LHSD0019009",name = "排班表ID",eName = "SCHEDULE_ID")
    private String scheduleId;

    @MetadataDescribe(id= "LHSD0019001",name = "排班表日期",eName = "SCHEDULE_DATE")
    private String scheduleDate;
    @MetadataDescribe(id= "LHSD0018013",name = "挂号时间",eName = "REGISTER_DATE_TIME")
    private String registerDateTime;
    @MetadataDescribe(id= "LHSD0018019",name = "挂号人ID",eName = "REGISTER_OPERA_ID")
    private String registerOperaId;
    @MetadataDescribe(id= "LHSD0018020",name = "挂号人姓名",eName = "REGISTER_OPERA_NAME")
    private String registerOperaName;
    @MetadataDescribe(id= "LHSD0018014",name = "挂号科室代码",eName = "REGISTER_DEPT_ID")
    private String registerDeptId;
    @MetadataDescribe(id= "LHSD0018015",name = "挂号科室名称",eName = "REGISTER_DEPT_NAME")
    private String registerDeptName;
    @MetadataDescribe(id= "LHSD0018016",name = "挂号医师ID",eName = "REGISTER_DOCTOR_ID")
    private String registerDoctorId;
    @MetadataDescribe(id= "LHSD0018017",name = "挂号医师姓名",eName = "REGISTER_DOCTOR_NAME")
    private String registerDoctorName;
    @MetadataDescribe(id= "LHSD0018007",name = "挂号上下午代码",eName = "REGISTER_AMPM_CODE")
    private String registerAmpmCode;
    @MetadataDescribe(id= "LHSD0018008",name = "挂号上下午名称",eName = "REGISTER_AMPM_NAME")
    private String registerAmpmName;
    @MetadataDescribe(id= "LHSD0018011",name = "挂号号别代码",eName = "REGISTER_CLINIC_TYPE_CODE")
    private String registerClinicTypeCode;
    @MetadataDescribe(id= "LHSD0018012",name = "挂号号别名称",eName = "REGISTER_CLINIC_TYPE_NAME")
    private String registerClinicTypeName;
    @MetadataDescribe(id= "LHSD0018021",name = "挂号号类代码",eName = "REGISTER_REQ_TYPE_CODE")
    private String registerReqTypeCode;
    @MetadataDescribe(id= "LHSD0018022",name = "挂号号类名称",eName = "REGISTER_REQ_TYPE_NAME")
    private String registerReqTypeName;
    @MetadataDescribe(id= "LHSD0018023",name = "挂号序号",eName = "REGISTER_SEQ_NO")
    private String registerSeqNo;
    @MetadataDescribe(id= "LHSD0018033",name = "预约标志",eName = "RESERVE_FLAG")
    private String reserveFlag;
    @MetadataDescribe(id= "LHSD0018034",name = "预约序号",eName = "RESERVE_ID")
    private String reserveId;
    @MetadataDescribe(id= "LHSD0018009",name = "挂号渠道代码",eName = "REGISTER_CHANNEL_CODE")
    private String registerChannelCode;
    @MetadataDescribe(id= "LHSD0018010",name = "挂号渠道名称",eName = "REGISTER_CHANNEL_NAME")
    private String registerChannelName;
    @MetadataDescribe(id= "LHSD0018046",name = "退号标志",eName = "RETURN_FLAG")
    private String returnFlag;
    @MetadataDescribe(id= "LHSD0018045",name = "退号时间",eName = "RETURN_DATE_TIME")
    private String returnDateTime;
    @MetadataDescribe(id= "LHSD0018047",name = "退号人ID",eName = "RETURN_OPERA_ID")
    private String returnOperaId;
    @MetadataDescribe(id= "LHSD0018048",name = "退号人姓名",eName = "RETURN_OPERA_NAME")
    private String returnOperaName;
    @MetadataDescribe(id= "LHSD0022012",name = "初诊标志代码",eName = "VISIT_TYPE_CODE")
    private String visitTypeCode;
    @MetadataDescribe(id= "LHSD0022013",name = "初诊标志名称",eName = "VISIT_TYPE_NAME")
    private String visitTypeName;
    @MetadataDescribe(id= "LHSD0022004",name = "就诊科室代码",eName = "VISIT_DEPT_ID")
    private String visitDeptId;
    @MetadataDescribe(id= "LHSD0022005",name = "就诊科室名称",eName = "VISIT_DEPT_NAME")
    private String visitDeptName;
    @MetadataDescribe(id= "LHSD0022009",name = "就诊开始时间",eName = "VISIT_START_DATE_TIME")
    private String visitStartDateTime;
    @MetadataDescribe(id= "LHSD0022006",name = "就诊结束时间",eName = "VISIT_END_DATE_TIME")
    private String visitEndDateTime;
    @MetadataDescribe(id= "LHSD0004064",name = "就诊医师ID",eName = "DOCTOR_ID")
    private String doctorId;
    @MetadataDescribe(id= "LHSD0004065",name = "就诊医师姓名",eName = "DOCTOR_NAME")
    private String doctorName;
    @MetadataDescribe(id= "LHSD0020008",name = "疗法代码",eName = "THERAPY_CODE")
    private String therapyCode;
    @MetadataDescribe(id= "LHSD0020009",name = "疗法名称（西医、中医、中西医结合）",eName = "THERAPY_NAME")
    private String therapyName;
    @MetadataDescribe(id= "LHSD0004027",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;
    @MetadataDescribe(id= "LHSD0004024",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;
    @MetadataDescribe(id= "LHSD0004035",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;
    @MetadataDescribe(id= "LHSD0004030",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;
    @MetadataDescribe(id= "LHSD0003019",name = "就诊诊室代码",eName = "CLINIC_ROOM_CODE")
    private String clinicRoomCode;
    @MetadataDescribe(id= "LHSD0003020",name = "就诊诊室名称",eName = "CLINIC_ROOM_NAME")
    private String clinicRoomName;
    @MetadataDescribe(id= "LHSD0022010",name = "就诊状态代码",eName = "VISIT_STATUS_CODE")
    private String visitStatusCode;
    @MetadataDescribe(id= "LHSD0022011",name = "就诊状态名称",eName = "VISIT_STATUS_NAME")
    private String visitStatusName;
    @MetadataDescribe(id= "LHSD0001005",name = "结算类型代码",eName = "ACCOUNT_TYPE_CODE")
    private String accountTypeCode;
    @MetadataDescribe(id= "LHSD0001006",name = "结算类型名称",eName = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;

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

    public String getMaritalStatusCode() {
        return maritalStatusCode;
    }

    public void setMaritalStatusCode(String maritalStatusCode) {
        this.maritalStatusCode = maritalStatusCode;
    }

    public String getMaritalStatusName() {
        return maritalStatusName;
    }

    public void setMaritalStatusName(String maritalStatusName) {
        this.maritalStatusName = maritalStatusName;
    }

    public String getPatientResourceCode() {
        return patientResourceCode;
    }

    public void setPatientResourceCode(String patientResourceCode) {
        this.patientResourceCode = patientResourceCode;
    }

    public String getPatientResourceName() {
        return patientResourceName;
    }

    public void setPatientResourceName(String patientResourceName) {
        this.patientResourceName = patientResourceName;
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

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getRegisterDateTime() {
        return registerDateTime;
    }

    public void setRegisterDateTime(String registerDateTime) {
        this.registerDateTime = registerDateTime;
    }

    public String getRegisterOperaId() {
        return registerOperaId;
    }

    public void setRegisterOperaId(String registerOperaId) {
        this.registerOperaId = registerOperaId;
    }

    public String getRegisterOperaName() {
        return registerOperaName;
    }

    public void setRegisterOperaName(String registerOperaName) {
        this.registerOperaName = registerOperaName;
    }

    public String getRegisterDeptId() {
        return registerDeptId;
    }

    public void setRegisterDeptId(String registerDeptId) {
        this.registerDeptId = registerDeptId;
    }

    public String getRegisterDeptName() {
        return registerDeptName;
    }

    public void setRegisterDeptName(String registerDeptName) {
        this.registerDeptName = registerDeptName;
    }

    public String getRegisterDoctorId() {
        return registerDoctorId;
    }

    public void setRegisterDoctorId(String registerDoctorId) {
        this.registerDoctorId = registerDoctorId;
    }

    public String getRegisterDoctorName() {
        return registerDoctorName;
    }

    public void setRegisterDoctorName(String registerDoctorName) {
        this.registerDoctorName = registerDoctorName;
    }

    public String getRegisterAmpmCode() {
        return registerAmpmCode;
    }

    public void setRegisterAmpmCode(String registerAmpmCode) {
        this.registerAmpmCode = registerAmpmCode;
    }

    public String getRegisterAmpmName() {
        return registerAmpmName;
    }

    public void setRegisterAmpmName(String registerAmpmName) {
        this.registerAmpmName = registerAmpmName;
    }

    public String getRegisterClinicTypeCode() {
        return registerClinicTypeCode;
    }

    public void setRegisterClinicTypeCode(String registerClinicTypeCode) {
        this.registerClinicTypeCode = registerClinicTypeCode;
    }

    public String getRegisterClinicTypeName() {
        return registerClinicTypeName;
    }

    public void setRegisterClinicTypeName(String registerClinicTypeName) {
        this.registerClinicTypeName = registerClinicTypeName;
    }

    public String getRegisterReqTypeCode() {
        return registerReqTypeCode;
    }

    public void setRegisterReqTypeCode(String registerReqTypeCode) {
        this.registerReqTypeCode = registerReqTypeCode;
    }

    public String getRegisterReqTypeName() {
        return registerReqTypeName;
    }

    public void setRegisterReqTypeName(String registerReqTypeName) {
        this.registerReqTypeName = registerReqTypeName;
    }

    public String getRegisterSeqNo() {
        return registerSeqNo;
    }

    public void setRegisterSeqNo(String registerSeqNo) {
        this.registerSeqNo = registerSeqNo;
    }

    public String getReserveFlag() {
        return reserveFlag;
    }

    public void setReserveFlag(String reserveFlag) {
        this.reserveFlag = reserveFlag;
    }

    public String getReserveId() {
        return reserveId;
    }

    public void setReserveId(String reserveId) {
        this.reserveId = reserveId;
    }

    public String getRegisterChannelCode() {
        return registerChannelCode;
    }

    public void setRegisterChannelCode(String registerChannelCode) {
        this.registerChannelCode = registerChannelCode;
    }

    public String getRegisterChannelName() {
        return registerChannelName;
    }

    public void setRegisterChannelName(String registerChannelName) {
        this.registerChannelName = registerChannelName;
    }

    public String getReturnFlag() {
        return returnFlag;
    }

    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }

    public String getReturnDateTime() {
        return returnDateTime;
    }

    public void setReturnDateTime(String returnDateTime) {
        this.returnDateTime = returnDateTime;
    }

    public String getReturnOperaId() {
        return returnOperaId;
    }

    public void setReturnOperaId(String returnOperaId) {
        this.returnOperaId = returnOperaId;
    }

    public String getReturnOperaName() {
        return returnOperaName;
    }

    public void setReturnOperaName(String returnOperaName) {
        this.returnOperaName = returnOperaName;
    }

    public String getVisitTypeCode() {
        return visitTypeCode;
    }

    public void setVisitTypeCode(String visitTypeCode) {
        this.visitTypeCode = visitTypeCode;
    }

    public String getVisitTypeName() {
        return visitTypeName;
    }

    public void setVisitTypeName(String visitTypeName) {
        this.visitTypeName = visitTypeName;
    }

    public String getVisitDeptId() {
        return visitDeptId;
    }

    public void setVisitDeptId(String visitDeptId) {
        this.visitDeptId = visitDeptId;
    }

    public String getVisitDeptName() {
        return visitDeptName;
    }

    public void setVisitDeptName(String visitDeptName) {
        this.visitDeptName = visitDeptName;
    }

    public String getVisitStartDateTime() {
        return visitStartDateTime;
    }

    public void setVisitStartDateTime(String visitStartDateTime) {
        this.visitStartDateTime = visitStartDateTime;
    }

    public String getVisitEndDateTime() {
        return visitEndDateTime;
    }

    public void setVisitEndDateTime(String visitEndDateTime) {
        this.visitEndDateTime = visitEndDateTime;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getTherapyCode() {
        return therapyCode;
    }

    public void setTherapyCode(String therapyCode) {
        this.therapyCode = therapyCode;
    }

    public String getTherapyName() {
        return therapyName;
    }

    public void setTherapyName(String therapyName) {
        this.therapyName = therapyName;
    }

    public String getDiagDateTime() {
        return diagDateTime;
    }

    public void setDiagDateTime(String diagDateTime) {
        this.diagDateTime = diagDateTime;
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

    public String getClinicRoomCode() {
        return clinicRoomCode;
    }

    public void setClinicRoomCode(String clinicRoomCode) {
        this.clinicRoomCode = clinicRoomCode;
    }

    public String getClinicRoomName() {
        return clinicRoomName;
    }

    public void setClinicRoomName(String clinicRoomName) {
        this.clinicRoomName = clinicRoomName;
    }

    public String getVisitStatusCode() {
        return visitStatusCode;
    }

    public void setVisitStatusCode(String visitStatusCode) {
        this.visitStatusCode = visitStatusCode;
    }

    public String getVisitStatusName() {
        return visitStatusName;
    }

    public void setVisitStatusName(String visitStatusName) {
        this.visitStatusName = visitStatusName;
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
}
