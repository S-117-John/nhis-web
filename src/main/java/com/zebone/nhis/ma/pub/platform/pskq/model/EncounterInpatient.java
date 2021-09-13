package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * @author ds
 * 就诊信息(住院)消息模型
 */
public class EncounterInpatient {
	@MetadataDescribe(id= "LHDE0020001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;
    
    @MetadataDescribe(id= "LHDE0020002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;
    
    @MetadataDescribe(id= "LHDE0020003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;
	
    @MetadataDescribe(id= "LHDE0020004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0020005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;
	
    @MetadataDescribe(id= "LHDE0020006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;
    
    @MetadataDescribe(id= "LHDE0020007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;
    
    @MetadataDescribe(id= "LHDE0020008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;
    
    @MetadataDescribe(id= "LHDE0020009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0020010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;
    
    @MetadataDescribe(id= "LHDE0020011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;
    
    @MetadataDescribe(id= "LHDE0020012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0020013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;
    
    @MetadataDescribe(id= "LHDE0020014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0020015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0020016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0020017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;
    
    @MetadataDescribe(id= "LHDE0020018",name = "婚姻状况代码",eName = "MARITAL_STATUS_CODE")
    private String maritalStatusCode;

    @MetadataDescribe(id= "LHDE0020019",name = "婚姻状况名称",eName = "MARITAL_STATUS_NAME")
    private String maritalStatusName;

    @MetadataDescribe(id= "LHDE0020020",name = "病人来源代码",eName = "PATIENT_RESOURCE_CODE")
    private String patientResourceCode;
    
    @MetadataDescribe(id= "LHDE0020021",name = "病人来源名称",eName = "PATIENT_RESOURCE_NAME")
    private String patientResourceName;
    
    @MetadataDescribe(id= "LHDE0020022",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;
	
    @MetadataDescribe(id= "LHDE0020023",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;
    
    @MetadataDescribe(id= "LHDE0020068",name = "住院在院标志",eName = "IN_HOSPITAL_FLAG")
    private String inHospitalFlag;
    
    @MetadataDescribe(id= "LHDE0020069",name = "入院方式",eName = "ADMISSION_MODE")
    private String admissionMode;
    
    @MetadataDescribe(id= "LHDE0020070",name = "入院途径代码",eName = "ADMISSION_WAY_CODE")
    private String admissionWayCode;
    
    @MetadataDescribe(id= "LHDE0020071",name = "入院途径名称",eName = "ADMISSION_WAY_NAME")
    private String admissionWayName;
    
    @MetadataDescribe(id= "LHDE0020072",name = "入院状态代码",eName = "ADMISSION_STATUS_CODE")
    private String admissionStatusCode;
    
    @MetadataDescribe(id= "LHDE0020073",name = "入院状态名称",eName = "ADMISSION_STATUS_NAME")
    private String admissionStatusName;
    
    @MetadataDescribe(id= "LHDE0020074",name = "入院时间",eName = "ADMISSION_DATE_TIME")
    private String admissionDateTime;
    
    @MetadataDescribe(id= "LHDE0020075",name = "入院科室代码",eName = "ADMISSION_DEPT_ID")
    private String admissionDeptId;
    
    @MetadataDescribe(id= "LHDE0020076",name = "入院科室名称",eName = "ADMISSION_DEPT_NAME")
    private String admissionDeptName;
    
    @MetadataDescribe(id= "LHDE0020077",name = "入院病区代码",eName = "ADMISSION_WARD_ID")
    private String admissionWardId;
    
    @MetadataDescribe(id= "LHDE0020078",name = "入院病区名称",eName = "ADMISSION_WARD_NAME")
    private String admissionWardName;
    
    @MetadataDescribe(id= "LHDE0020079",name = "当前病区代码",eName = "VISIT_WARD_ID")
    private String visitWardId;
    
    @MetadataDescribe(id= "LHDE0020080",name = "当前病区名称",eName = "VISIT_WARD_NAME")
    private String visitWardName;
    
    @MetadataDescribe(id= "LHDE0020081",name = "床号",eName = "BED_NO")
    private String bedNo;
    
    @MetadataDescribe(id= "LHDE0020082",name = "科主任医师ID",eName = "DIVISION_DIRECTOR_ID")
    private String divisionDirectorId;
    
    @MetadataDescribe(id= "LHDE0020083",name = "科主任医师名称",eName = "DIVISION_DIRECTOR_NAME")
    private String divisionDirectorName;
    
    @MetadataDescribe(id= "LHDE0020084",name = "主任医师ID",eName = "CHIEF_DOCTOR_ID")
    private String chiefDoctorId;
    
    @MetadataDescribe(id= "LHDE0020085",name = "主任医师名称",eName = "CHIEF_DOCTOR_NAME")
    private String chiefDoctorName;
    
    @MetadataDescribe(id= "LHDE0020086",name = "主治医师ID",eName = "ATTEND_DOCTOR_ID")
    private String attendDoctorId;
    
    @MetadataDescribe(id= "LHDE0020087",name = "主治医师姓名",eName = "ATTEND_DOCTOR_NAME")
    private String attendDoctorName;

    @MetadataDescribe(id= "LHDE0020088",name = "住院医师ID",eName = "RESIDENT_DOCTOR_ID")
    private String residentDoctorId;
    
    @MetadataDescribe(id= "LHDE0020089",name = "住院医师姓名",eName = "RESIDENT_DOCTOR_NAME")
    private String residentDoctorName;
    
    @MetadataDescribe(id= "LHDE0020090",name = "责任医师ID",eName = "RESPONSIBLE_DOCTOR_ID")
    private String responsibleDoctorId;
    
    @MetadataDescribe(id= "LHDE0020091",name = "责任医师姓名",eName = "RESPONSIBLE_DOCTOR_NAME")
    private String responsibleDoctorName;
    
    @MetadataDescribe(id= "LHDE0020092",name = "责任护士ID",eName = "RESPONSIBLE_NURSE_ID")
    private String responsibleNurseId;
    
    @MetadataDescribe(id= "LHDE0020093",name = "责任护士姓名",eName = "RESPONSIBLE_NURSE_NAME")
    private String responsibleNurseName;
    
    @MetadataDescribe(id= "LHDE0020094",name = "入院诊断代码",eName = "ADMISSION_DIAG_CODE")
    private String admissionDiagCode;
    
    @MetadataDescribe(id= "LHDE0020095",name = "入院诊断名称",eName = "ADMISSION_DIAG_NAME")
    private String admissionDiagName;
    
    @MetadataDescribe(id= "LHDE0020096",name = "入院诊断描述",eName = "ADMISSION_DIAG_DESC")
    private String admissionDiagDesc;
    
    @MetadataDescribe(id= "LHDE0020097",name = "出院诊断时间",eName = "DISCHARGE_DIAG_DATE_TIME")
    private String dischargeDiagDateTime;
    
    @MetadataDescribe(id= "LHDE0020098",name = "出院诊断代码",eName = "DISCHARGE_DIAG_CODE")
    private String dischargeDiagCode;
    
    @MetadataDescribe(id= "LHDE0020099",name = "出院诊断名称",eName = "DISCHARGE_DIAG_NAME")
    private String dischargeDiagName;
    
    @MetadataDescribe(id= "LHDE0020100",name = "出院诊断描述",eName = "DISCHARGE_DIAG_DESC")
    private String dischargeDiagDesc;
    
    @MetadataDescribe(id= "LHDE0020101",name = "出院时间",eName = "DISCHARGE_DATE_TIME")
    private String dischargeDateTime;
    
    @MetadataDescribe(id= "LHDE0020102",name = "出院科室代码",eName = "DISCHARGE_DEPT_ID")
    private String dischargeDeptId;
    
    @MetadataDescribe(id= "LHDE0020103",name = "出院科室名称",eName = "DISCHARGE_DEPT_NAME")
    private String dischargeDeptName;
    
    @MetadataDescribe(id= "LHDE0020104",name = "出院病区代码",eName = "DISCHARGE_WARD_ID")
    private String dischargeWardId;
    
    @MetadataDescribe(id= "LHDE0020105",name = "出院病区名称",eName = "DISCHARGE_WARD_NAME")
    private String dischargeWardName;
    
    @MetadataDescribe(id= "LHDE0020106",name = "离院方式代码",eName = "DISCHARGE_METHOD_CODE")
    private String dischargeMethodCode;
    
    @MetadataDescribe(id= "LHDE0020107",name = "离院方式名称",eName = "DISCHARGE_METHOD_NAME")
    private String dischargeMethodName;
    
    @MetadataDescribe(id= "LHDE0020108",name = "出院方式",eName = "DISCHARGE_MODE")
    private String dischargeMode;
    
    @MetadataDescribe(id= "LHDE0020109",name = "出院31天内再住院标志",eName = "REINPAT_31D_FLAG")
    private String reinpat31dFlag;
    
    @MetadataDescribe(id= "LHDE0020110",name = "实施临床路径标志代码",eName = "CP_TYPE_CODE")
    private String cpTypeCode;
    
    @MetadataDescribe(id= "LHDE0020111",name = "临床路径代码",eName = "CP_CODE")
    private String cpCode;
    
    @MetadataDescribe(id= "LHDE0020112",name = "临床路径名称",eName = "CP_NAME")
    private String cpName;
    
    @MetadataDescribe(id= "LHDE0020113",name = "DRG分组代码",eName = "DRG_CODE")
    private String drgCode;
    
    @MetadataDescribe(id= "LHDE0020114",name = "DRG分组名称",eName = "DRG_NAME")
    private String drgName;
    
    @MetadataDescribe(id= "LHDE0020115",name = "住院号",eName = "INPATIENT_NO")
    private String inpatientNo;

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

	public String getInHospitalFlag() {
		return inHospitalFlag;
	}

	public void setInHospitalFlag(String inHospitalFlag) {
		this.inHospitalFlag = inHospitalFlag;
	}

	public String getAdmissionMode() {
		return admissionMode;
	}

	public void setAdmissionMode(String admissionMode) {
		this.admissionMode = admissionMode;
	}

	public String getAdmissionWayCode() {
		return admissionWayCode;
	}

	public void setAdmissionWayCode(String admissionWayCode) {
		this.admissionWayCode = admissionWayCode;
	}

	public String getAdmissionWayName() {
		return admissionWayName;
	}

	public void setAdmissionWayName(String admissionWayName) {
		this.admissionWayName = admissionWayName;
	}

	public String getAdmissionStatusCode() {
		return admissionStatusCode;
	}

	public void setAdmissionStatusCode(String admissionStatusCode) {
		this.admissionStatusCode = admissionStatusCode;
	}

	public String getAdmissionStatusName() {
		return admissionStatusName;
	}

	public void setAdmissionStatusName(String admissionStatusName) {
		this.admissionStatusName = admissionStatusName;
	}

	public String getAdmissionDateTime() {
		return admissionDateTime;
	}

	public void setAdmissionDateTime(String admissionDateTime) {
		this.admissionDateTime = admissionDateTime;
	}

	public String getAdmissionDeptId() {
		return admissionDeptId;
	}

	public void setAdmissionDeptId(String admissionDeptId) {
		this.admissionDeptId = admissionDeptId;
	}

	public String getAdmissionDeptName() {
		return admissionDeptName;
	}

	public void setAdmissionDeptName(String admissionDeptName) {
		this.admissionDeptName = admissionDeptName;
	}

	public String getAdmissionWardId() {
		return admissionWardId;
	}

	public void setAdmissionWardId(String admissionWardId) {
		this.admissionWardId = admissionWardId;
	}

	public String getAdmissionWardName() {
		return admissionWardName;
	}

	public void setAdmissionWardName(String admissionWardName) {
		this.admissionWardName = admissionWardName;
	}

	public String getVisitWardId() {
		return visitWardId;
	}

	public void setVisitWardId(String visitWardId) {
		this.visitWardId = visitWardId;
	}

	public String getVisitWardName() {
		return visitWardName;
	}

	public void setVisitWardName(String visitWardName) {
		this.visitWardName = visitWardName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getDivisionDirectorId() {
		return divisionDirectorId;
	}

	public void setDivisionDirectorId(String divisionDirectorId) {
		this.divisionDirectorId = divisionDirectorId;
	}

	public String getDivisionDirectorName() {
		return divisionDirectorName;
	}

	public void setDivisionDirectorName(String divisionDirectorName) {
		this.divisionDirectorName = divisionDirectorName;
	}

	public String getChiefDoctorId() {
		return chiefDoctorId;
	}

	public void setChiefDoctorId(String chiefDoctorId) {
		this.chiefDoctorId = chiefDoctorId;
	}

	public String getChiefDoctorName() {
		return chiefDoctorName;
	}

	public void setChiefDoctorName(String chiefDoctorName) {
		this.chiefDoctorName = chiefDoctorName;
	}

	public String getAttendDoctorId() {
		return attendDoctorId;
	}

	public void setAttendDoctorId(String attendDoctorId) {
		this.attendDoctorId = attendDoctorId;
	}

	public String getAttendDoctorName() {
		return attendDoctorName;
	}

	public void setAttendDoctorName(String attendDoctorName) {
		this.attendDoctorName = attendDoctorName;
	}

	public String getResidentDoctorId() {
		return residentDoctorId;
	}

	public void setResidentDoctorId(String residentDoctorId) {
		this.residentDoctorId = residentDoctorId;
	}

	public String getResidentDoctorName() {
		return residentDoctorName;
	}

	public void setResidentDoctorName(String residentDoctorName) {
		this.residentDoctorName = residentDoctorName;
	}

	public String getResponsibleDoctorId() {
		return responsibleDoctorId;
	}

	public void setResponsibleDoctorId(String responsibleDoctorId) {
		this.responsibleDoctorId = responsibleDoctorId;
	}

	public String getResponsibleDoctorName() {
		return responsibleDoctorName;
	}

	public void setResponsibleDoctorName(String responsibleDoctorName) {
		this.responsibleDoctorName = responsibleDoctorName;
	}

	public String getResponsibleNurseId() {
		return responsibleNurseId;
	}

	public void setResponsibleNurseId(String responsibleNurseId) {
		this.responsibleNurseId = responsibleNurseId;
	}

	public String getResponsibleNurseName() {
		return responsibleNurseName;
	}

	public void setResponsibleNurseName(String responsibleNurseName) {
		this.responsibleNurseName = responsibleNurseName;
	}

	public String getAdmissionDiagCode() {
		return admissionDiagCode;
	}

	public void setAdmissionDiagCode(String admissionDiagCode) {
		this.admissionDiagCode = admissionDiagCode;
	}

	public String getAdmissionDiagName() {
		return admissionDiagName;
	}

	public void setAdmissionDiagName(String admissionDiagName) {
		this.admissionDiagName = admissionDiagName;
	}

	public String getAdmissionDiagDesc() {
		return admissionDiagDesc;
	}

	public void setAdmissionDiagDesc(String admissionDiagDesc) {
		this.admissionDiagDesc = admissionDiagDesc;
	}

	public String getDischargeDiagDateTime() {
		return dischargeDiagDateTime;
	}

	public void setDischargeDiagDateTime(String dischargeDiagDateTime) {
		this.dischargeDiagDateTime = dischargeDiagDateTime;
	}

	public String getDischargeDiagCode() {
		return dischargeDiagCode;
	}

	public void setDischargeDiagCode(String dischargeDiagCode) {
		this.dischargeDiagCode = dischargeDiagCode;
	}

	public String getDischargeDiagName() {
		return dischargeDiagName;
	}

	public void setDischargeDiagName(String dischargeDiagName) {
		this.dischargeDiagName = dischargeDiagName;
	}

	public String getDischargeDiagDesc() {
		return dischargeDiagDesc;
	}

	public void setDischargeDiagDesc(String dischargeDiagDesc) {
		this.dischargeDiagDesc = dischargeDiagDesc;
	}

	public String getDischargeDateTime() {
		return dischargeDateTime;
	}

	public void setDischargeDateTime(String dischargeDateTime) {
		this.dischargeDateTime = dischargeDateTime;
	}

	public String getDischargeDeptId() {
		return dischargeDeptId;
	}

	public void setDischargeDeptId(String dischargeDeptId) {
		this.dischargeDeptId = dischargeDeptId;
	}

	public String getDischargeDeptName() {
		return dischargeDeptName;
	}

	public void setDischargeDeptName(String dischargeDeptName) {
		this.dischargeDeptName = dischargeDeptName;
	}

	public String getDischargeWardId() {
		return dischargeWardId;
	}

	public void setDischargeWardId(String dischargeWardId) {
		this.dischargeWardId = dischargeWardId;
	}

	public String getDischargeWardName() {
		return dischargeWardName;
	}

	public void setDischargeWardName(String dischargeWardName) {
		this.dischargeWardName = dischargeWardName;
	}

	public String getDischargeMethodCode() {
		return dischargeMethodCode;
	}

	public void setDischargeMethodCode(String dischargeMethodCode) {
		this.dischargeMethodCode = dischargeMethodCode;
	}

	public String getDischargeMethodName() {
		return dischargeMethodName;
	}

	public void setDischargeMethodName(String dischargeMethodName) {
		this.dischargeMethodName = dischargeMethodName;
	}

	public String getDischargeMode() {
		return dischargeMode;
	}

	public void setDischargeMode(String dischargeMode) {
		this.dischargeMode = dischargeMode;
	}

	public String getReinpat31dFlag() {
		return reinpat31dFlag;
	}

	public void setReinpat31dFlag(String reinpat31dFlag) {
		this.reinpat31dFlag = reinpat31dFlag;
	}

	public String getCpTypeCode() {
		return cpTypeCode;
	}

	public void setCpTypeCode(String cpTypeCode) {
		this.cpTypeCode = cpTypeCode;
	}

	public String getCpCode() {
		return cpCode;
	}

	public void setCpCode(String cpCode) {
		this.cpCode = cpCode;
	}

	public String getCpName() {
		return cpName;
	}

	public void setCpName(String cpName) {
		this.cpName = cpName;
	}

	public String getDrgCode() {
		return drgCode;
	}

	public void setDrgCode(String drgCode) {
		this.drgCode = drgCode;
	}

	public String getDrgName() {
		return drgName;
	}

	public void setDrgName(String drgName) {
		this.drgName = drgName;
	}

	public String getInpatientNo() {
		return inpatientNo;
	}

	public void setInpatientNo(String inpatientNo) {
		this.inpatientNo = inpatientNo;
	}
    
    
    
}
