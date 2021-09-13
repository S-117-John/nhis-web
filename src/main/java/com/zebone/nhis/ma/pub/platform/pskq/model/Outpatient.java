package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;
import com.zebone.nhis.ma.pub.platform.pskq.dictionary.DictionaryUtil;
import com.zebone.nhis.ma.pub.platform.pskq.dictionary.RegisterAmpm;

/**
 * @author tjq
 *  OUTPATIENT
 */
public class Outpatient {
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
    @JSONField(format="yyyy-MM-dd")
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

    @MetadataDescribe(id= "LHDE0020024",name = "排班表ID",eName = "SCHEDULE_ID")
    private String scheduleId;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020025",name = "排班表日期",eName = "SCHEDULE_DATE")
    private String scheduleDate;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020026",name = "挂号时间",eName = "REGISTER_DATE_TIME")
    private String registerDateTime;
    @MetadataDescribe(id= "LHDE0020027",name = "挂号人ID",eName = "REGISTER_OPERA_ID")
    private String registerOperaId;
    @MetadataDescribe(id= "LHDE0020028",name = "挂号人姓名",eName = "REGISTER_OPERA_NAME")
    private String registerOperaName;
    @MetadataDescribe(id= "LHDE0020029",name = "挂号科室代码",eName = "REGISTER_DEPT_ID")
    private String registerDeptId;
    @MetadataDescribe(id= "LHDE0020030",name = "挂号科室名称",eName = "REGISTER_DEPT_NAME")
    private String registerDeptName;
    @MetadataDescribe(id= "LHDE0020031",name = "挂号医师ID",eName = "REGISTER_DOCTOR_ID")
    private String registerDoctorId;
    @MetadataDescribe(id= "LHDE0020032",name = "挂号医师姓名",eName = "REGISTER_DOCTOR_NAME")
    private String registerDoctorName;
    @MetadataDescribe(id= "LHDE0020033",name = "挂号上下午代码",eName = "REGISTER_AMPM_CODE")
    private String registerAmpmCode;
    @MetadataDescribe(id= "LHDE0020034",name = "挂号上下午名称",eName = "REGISTER_AMPM_NAME")
    private String registerAmpmName;
    @MetadataDescribe(id= "LHDE0020035",name = "挂号号别代码",eName = "REGISTER_CLINIC_TYPE_CODE")
    private String registerClinicTypeCode;
    @MetadataDescribe(id= "LHDE0020036",name = "挂号号别名称",eName = "REGISTER_CLINIC_TYPE_NAME")
    private String registerClinicTypeName;
    @MetadataDescribe(id= "LHDE0020037",name = "挂号号类代码",eName = "REGISTER_REQ_TYPE_CODE")
    private String registerReqTypeCode;
    @MetadataDescribe(id= "LHDE0020038",name = "挂号号类名称",eName = "REGISTER_REQ_TYPE_NAME")
    private String registerReqTypeName;
    @MetadataDescribe(id= "LHDE0020039",name = "挂号序号",eName = "REGISTER_SEQ_NO")
    private String registerSeqNo;
    @MetadataDescribe(id= "LHDE0020040",name = "预约标志",eName = "RESERVE_FLAG")
    private String reserveFlag;
    @MetadataDescribe(id= "LHDE0020041",name = "预约序号",eName = "RESERVE_ID")
    private String reserveId;
    @MetadataDescribe(id= "LHDE0020042",name = "挂号渠道代码",eName = "REGISTER_CHANNEL_CODE")
    private String registerChannelCode;
    @MetadataDescribe(id= "LHDE0020043",name = "挂号渠道名称",eName = "REGISTER_CHANNEL_NAME")
    private String registerChannelName;
    @MetadataDescribe(id= "LHDE0020044",name = "退号标志",eName = "RETURN_FLAG")
    private String returnFlag;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020045",name = "退号时间",eName = "RETURN_DATE_TIME")
    private String returnDateTime;
    @MetadataDescribe(id= "LHDE0020046",name = "退号人ID",eName = "RETURN_OPERA_ID")
    private String returnOperaId;
    @MetadataDescribe(id= "LHDE0020047",name = "退号人姓名",eName = "RETURN_OPERA_NAME")
    private String returnOperaName;
    @MetadataDescribe(id= "LHDE0020048",name = "初诊标志代码",eName = "VISIT_TYPE_CODE")
    private String visitTypeCode;
    @MetadataDescribe(id= "LHDE0020049",name = "初诊标志名称",eName = "VISIT_TYPE_NAME")
    private String visitTypeName;
    @MetadataDescribe(id= "LHDE0020050",name = "就诊科室代码",eName = "VISIT_DEPT_ID")
    private String visitDeptId;
    @MetadataDescribe(id= "LHDE0020051",name = "就诊科室名称",eName = "VISIT_DEPT_NAME")
    private String visitDeptName;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020052",name = "就诊开始时间",eName = "VISIT_START_DATE_TIME")
    private String visitStartDateTime;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020053",name = "就诊结束时间",eName = "VISIT_END_DATE_TIME")
    private String visitEndDateTime;
    @MetadataDescribe(id= "LHDE0020054",name = "就诊医师ID",eName = "DOCTOR_ID")
    private String doctorId;
    @MetadataDescribe(id= "LHDE0020055",name = "就诊医师姓名",eName = "DOCTOR_NAME")
    private String doctorName;
    @MetadataDescribe(id= "LHDE0020056",name = "疗法代码",eName = "THERAPY_CODE")
    private String therapyCode;
    @MetadataDescribe(id= "LHDE0020057",name = "疗法名称（西医、中医、中西医结合）",eName = "THERAPY_NAME")
    private String therapyName;
    @JSONField(format="yyyy-MM-dd")
    @MetadataDescribe(id= "LHDE0020058",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;
    @MetadataDescribe(id= "LHDE0020059",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;
    @MetadataDescribe(id= "LHDE0020060",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;
    @MetadataDescribe(id= "LHDE0020061",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;
    @MetadataDescribe(id= "LHDE0020062",name = "就诊诊室代码",eName = "CLINIC_ROOM_CODE")
    private String clinicRoomCode;
    @MetadataDescribe(id= "LHDE0020063",name = "就诊诊室名称",eName = "CLINIC_ROOM_NAME")
    private String clinicRoomName;
    @MetadataDescribe(id= "LHDE0020064",name = "就诊状态代码",eName = "VISIT_STATUS_CODE")
    private String visitStatusCode;
    @MetadataDescribe(id= "LHDE0020065",name = "就诊状态名称",eName = "VISIT_STATUS_NAME")
    private String visitStatusName;
    @MetadataDescribe(id= "LHDE0020066",name = "结算类型代码",eName = "ACCOUNT_TYPE_CODE")
    private String accountTypeCode;
    @MetadataDescribe(id= "LHDE0020067",name = "结算类型名称",eName = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;
    
    @MetadataDescribe(id= "LHDE0020068",name = "挂号发票明细ID",eName = "REGISTERED_INVOICE_ID")
    private String registeredInvoiceId;
    
    @MetadataDescribe(id= "LHDE0020069",name = "挂号电子发票PDF路径",eName = "REGISTERED_INVOICE_PDF_URL")
    private String registeredInvoicePdfUrl;
    
    @MetadataDescribe(id= "LHDE0020070",name = "原始挂号发票明细ID",eName = "OLD_REGISTERED_INVOICE_ID")
    private String oldRegisteredInvoiceId;
    
    @MetadataDescribe(id= "LHDE0020071",name = "挂号电子发票状态",eName = "REGISTERED_INVOICE_PDF_STATUS")
    private String registeredInvoicePdfStatus;
    
    @MetadataDescribe(id= "LHDE0020072",name = "门诊号",eName = "OUTPATIENT_NO")
    private String outpatientNo;

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
        String id = "10_1_2000_"+encounterId;
        this.encounterId = id;
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
    	String code = "9";
        switch(genderCode){
            case "02":
                code = "1";
                break;
            case "03":
                code = "2";
                break;
        }
        this.genderCode = code;
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
        String code = DictionaryUtil.getCode(RegisterAmpm.registerAmpmMap,registerAmpmCode);
        this.registerAmpmCode = code;
    }

    public String getRegisterAmpmName() {
        return registerAmpmName;
    }

    public void setRegisterAmpmName(String registerAmpmName) {
        String name = DictionaryUtil.getName(RegisterAmpm.registerAmpmMap,registerAmpmName);
        this.registerAmpmName = name;
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

	public String getRegisteredInvoiceId() {
		return registeredInvoiceId;
	}

	public void setRegisteredInvoiceId(String registeredInvoiceId) {
		this.registeredInvoiceId = registeredInvoiceId;
	}

	public String getRegisteredInvoicePdfUrl() {
		return registeredInvoicePdfUrl;
	}

	public void setRegisteredInvoicePdfUrl(String registeredInvoicePdfUrl) {
		this.registeredInvoicePdfUrl = registeredInvoicePdfUrl;
	}

	public String getOldRegisteredInvoiceId() {
		return oldRegisteredInvoiceId;
	}

	public void setOldRegisteredInvoiceId(String oldRegisteredInvoiceId) {
		this.oldRegisteredInvoiceId = oldRegisteredInvoiceId;
	}

	public String getRegisteredInvoicePdfStatus() {
		return registeredInvoicePdfStatus;
	}

	public void setRegisteredInvoicePdfStatus(String registeredInvoicePdfStatus) {
		this.registeredInvoicePdfStatus = registeredInvoicePdfStatus;
	}

	public String getOutpatientNo() {
		return outpatientNo;
	}

	public void setOutpatientNo(String outpatientNo) {
		this.outpatientNo = outpatientNo;
	}
    
}
