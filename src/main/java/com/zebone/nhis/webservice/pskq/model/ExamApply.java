package com.zebone.nhis.webservice.pskq.model;


import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 检查申请
 */
public class ExamApply {

    @MetadataDescribe(id= "LHDE0022001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0022002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0022003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0022004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0022005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0022006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0022007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0022008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0022009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0022010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0022011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0022012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0022013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0022014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0022015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0022016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0022017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0022018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0022019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0022020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0022021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0022022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0022023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0022024",name = "检查申请ID",eName = "EXAM_APPLY_ID")
    private String examApplyId;

    @MetadataDescribe(id= "LHDE0022025",name = "检查申请单号",eName = "EXAM_APPLY_NO")
    private String examApplyNo;

    @MetadataDescribe(id= "LHDE0022026",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    @MetadataDescribe(id= "LHDE0022027",name = "申请日期时间",eName = "APPLY_DATE_TIME")
    private String applyDateTime;

    @MetadataDescribe(id= "LHDE0022028",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0022029",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0022030",name = "申请医师ID",eName = "APPLY_DOCTOR_ID")
    private String applyDoctorId;

    @MetadataDescribe(id= "LHDE0022031",name = "申请医师姓名",eName = "APPLY_DOCTOR_NAME")
    private String applyDoctorName;

    @MetadataDescribe(id= "LHDE0022032",name = "病情描述",eName = "DISEASE_DESC")
    private String diseaseDesc;

    @MetadataDescribe(id= "LHDE0022033",name = "现病史描述",eName = "PRESENT_HISTORY_DESC")
    private String presentHistoryDesc;

    @MetadataDescribe(id= "LHDE0022034",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;

    @MetadataDescribe(id= "LHDE0022035",name = "疾病诊断编码",eName = "DIAG_CODE;")
    private String diagCode;

    @MetadataDescribe(id= "LHDE0022036",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;

    @MetadataDescribe(id= "LHDE0022037",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;

    @MetadataDescribe(id= "LHDE0022038",name = "检查目的描述",eName = "APPLY_PURPOSE_DESC")
    private String applyPurposeDezsc;

    @MetadataDescribe(id= "LHDE0022039",name = "检查分类代码",eName = "EXAM_CATEGORY_CODE")
    private String examCategoryCode;

    @MetadataDescribe(id= "LHDE0022040",name = "检查分类名称",eName = "EXAM_CATEGORY_NAME")
    private String examCategoryName;

    @MetadataDescribe(id= "LHDE0022041",name = "检查类别代码",eName = "EXAM_CLASS_CODE")
    private String examClassCode;

    @MetadataDescribe(id= "LHDE0022042",name = "检查类别名称",eName = "EXAM_CLASS_NAME")
    private String examClassName;

    @MetadataDescribe(id= "LHDE0022043",name = "医嘱项目代码",eName = "UNIVERSAL_SERVICE_CODE")
    private String universalServiceCode;

    @MetadataDescribe(id= "LHDE0022044",name = "医嘱项目名称",eName = "UNIVERSAL_SERVICE_NAME")
    private String universalServiceName;

    @MetadataDescribe(id= "LHDE0022045",name = "检查部位代码",eName = "EXAM_PART_CODE")
    private String examPartCode;

    @MetadataDescribe(id= "LHDE0022046",name = "检查部位名称",eName = "EXAM_PART_NAME")
    private String examPartName;

    @MetadataDescribe(id= "LHDE0022047",name = "申请单备注",eName = "APPLY_CMMT")
    private String applyCmmt;

    @MetadataDescribe(id= "LHDE0022048",name = "计划开始日期时间",eName = "SCHEDULE_START_DATE_TIME")
    private String scheduleStartDateTime;

    @MetadataDescribe(id= "LHDE0022049",name = "计划结束日期时间",eName = "SCHEDULE_END_DATE_TIME")
    private String scheduleEndDateTime;

    @MetadataDescribe(id= "LHDE0022050",name = "登记日期时间",eName = "REGISTRANT_DATE_TIME")
    private String registrantDateTime;

    @MetadataDescribe(id= "LHDE0022051",name = "登记人ID",eName = "REGISTRANT_ID")
    private String registrantOperaId;

    @MetadataDescribe(id= "LHDE0022052",name = "登记人姓名",eName = "REGISTRANT_OPERA_NAME")
    private String registrantOperaName;

    @MetadataDescribe(id= "LHDE0022053",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;

    @MetadataDescribe(id= "LHDE0022054",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0022055",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0022056",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0022057",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0022058",name = "执行机构/院部代码",eName = "EXEC_ORG_CODE")
    private String execOrgCode;

    @MetadataDescribe(id= "LHDE0022059",name = "执行机构/院部名称",eName = "EXEC_ORG_NAME")
    private String execOrgName;

    @MetadataDescribe(id= "LHDE0022060",name = "执行系统代码",eName = "EXEC_SYSTEM_CODE")
    private String execSystemCode;

    @MetadataDescribe(id= "LHDE0022061",name = "执行系统名称",eName = "EXEC_SYSTEM_NAME")
    private String execSystemName;

    @MetadataDescribe(id= "LHDE0022062",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0022063",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0022064",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;

    @MetadataDescribe(id= "LHDE0022065",name = "绿色通道标志",eName = "GREEN_CHANNEL_FLAG")
    private String greenChannelFlag;

    @MetadataDescribe(id= "LHDE0022066",name = "费用金额",eName = "FEE_AMOUNT")
    private String feeAmount;

    @MetadataDescribe(id= "LHDE0022067",name = "申请单状态代码",eName = "APPLY_STATUS_CODE")
    private String applyStatusCode;

    @MetadataDescribe(id= "LHDE0022068",name = "申请单状态名称",eName = "APPLY_STATUS_NAME")
    private String applyStatusName;

    @MetadataDescribe(id= "LHDE0022069",name = "孕周-周",eName = "GESTATION_WEEK")
    private String gestationWeek;

    @MetadataDescribe(id= "LHDE0022070",name = "孕周-天",eName = "GESTATION_DAY")
    private String gestationDay;

    @MetadataDescribe(id= "LHDE0022071",name = "末次月经",eName = "LAST_MENSTRUAL_PERIOD")
    private String lastMenstrualPeriod;

    @MetadataDescribe(id= "LHDE0022072",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0022073",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0022074",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;

    @MetadataDescribe(id= "LHDE0022075",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;


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

    public String getExamApplyId() {
        return examApplyId;
    }

    public void setExamApplyId(String examApplyId) {
        this.examApplyId = examApplyId;
    }

    public String getExamApplyNo() {
        return examApplyNo;
    }

    public void setExamApplyNo(String examApplyNo) {
        this.examApplyNo = examApplyNo;
    }

    public String getPlacerOrderNo() {
        return placerOrderNo;
    }

    public void setPlacerOrderNo(String placerOrderNo) {
        this.placerOrderNo = placerOrderNo;
    }

    public String getApplyDateTime() {
        return applyDateTime;
    }

    public void setApplyDateTime(String applyDateTime) {
        this.applyDateTime = applyDateTime;
    }

    public String getApplyDeptId() {
        return applyDeptId;
    }

    public void setApplyDeptId(String applyDeptId) {
        this.applyDeptId = applyDeptId;
    }

    public String getApplyDeptName() {
        return applyDeptName;
    }

    public void setApplyDeptName(String applyDeptName) {
        this.applyDeptName = applyDeptName;
    }

    public String getApplyDoctorId() {
        return applyDoctorId;
    }

    public void setApplyDoctorId(String applyDoctorId) {
        this.applyDoctorId = applyDoctorId;
    }

    public String getApplyDoctorName() {
        return applyDoctorName;
    }

    public void setApplyDoctorName(String applyDoctorName) {
        this.applyDoctorName = applyDoctorName;
    }

    public String getDiseaseDesc() {
        return diseaseDesc;
    }

    public void setDiseaseDesc(String diseaseDesc) {
        this.diseaseDesc = diseaseDesc;
    }

    public String getPresentHistoryDesc() {
        return presentHistoryDesc;
    }

    public void setPresentHistoryDesc(String presentHistoryDesc) {
        this.presentHistoryDesc = presentHistoryDesc;
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

    public String getApplyPurposeDezsc() {
        return applyPurposeDezsc;
    }

    public void setApplyPurposeDezsc(String applyPurposeDezsc) {
        this.applyPurposeDezsc = applyPurposeDezsc;
    }

    public String getExamCategoryCode() {
        return examCategoryCode;
    }

    public void setExamCategoryCode(String examCategoryCode) {
        this.examCategoryCode = examCategoryCode;
    }

    public String getExamCategoryName() {
        return examCategoryName;
    }

    public void setExamCategoryName(String examCategoryName) {
        this.examCategoryName = examCategoryName;
    }

    public String getExamClassCode() {
        return examClassCode;
    }

    public void setExamClassCode(String examClassCode) {
        this.examClassCode = examClassCode;
    }

    public String getExamClassName() {
        return examClassName;
    }

    public void setExamClassName(String examClassName) {
        this.examClassName = examClassName;
    }

    public String getUniversalServiceCode() {
        return universalServiceCode;
    }

    public void setUniversalServiceCode(String universalServiceCode) {
        this.universalServiceCode = universalServiceCode;
    }

    public String getUniversalServiceName() {
        return universalServiceName;
    }

    public void setUniversalServiceName(String universalServiceName) {
        this.universalServiceName = universalServiceName;
    }

    public String getExamPartCode() {
        return examPartCode;
    }

    public void setExamPartCode(String examPartCode) {
        this.examPartCode = examPartCode;
    }

    public String getExamPartName() {
        return examPartName;
    }

    public void setExamPartName(String examPartName) {
        this.examPartName = examPartName;
    }

    public String getApplyCmmt() {
        return applyCmmt;
    }

    public void setApplyCmmt(String applyCmmt) {
        this.applyCmmt = applyCmmt;
    }

    public String getScheduleStartDateTime() {
        return scheduleStartDateTime;
    }

    public void setScheduleStartDateTime(String scheduleStartDateTime) {
        this.scheduleStartDateTime = scheduleStartDateTime;
    }

    public String getScheduleEndDateTime() {
        return scheduleEndDateTime;
    }

    public void setScheduleEndDateTime(String scheduleEndDateTime) {
        this.scheduleEndDateTime = scheduleEndDateTime;
    }

    public String getRegistrantDateTime() {
        return registrantDateTime;
    }

    public void setRegistrantDateTime(String registrantDateTime) {
        this.registrantDateTime = registrantDateTime;
    }

    public String getRegistrantOperaId() {
        return registrantOperaId;
    }

    public void setRegistrantOperaId(String registrantOperaId) {
        this.registrantOperaId = registrantOperaId;
    }

    public String getRegistrantOperaName() {
        return registrantOperaName;
    }

    public void setRegistrantOperaName(String registrantOperaName) {
        this.registrantOperaName = registrantOperaName;
    }

    public String getCancelFlag() {
        return cancelFlag;
    }

    public void setCancelFlag(String cancelFlag) {
        this.cancelFlag = cancelFlag;
    }

    public String getCancelDateTime() {
        return cancelDateTime;
    }

    public void setCancelDateTime(String cancelDateTime) {
        this.cancelDateTime = cancelDateTime;
    }

    public String getCancelReasonDesc() {
        return cancelReasonDesc;
    }

    public void setCancelReasonDesc(String cancelReasonDesc) {
        this.cancelReasonDesc = cancelReasonDesc;
    }

    public String getCancelOperaId() {
        return cancelOperaId;
    }

    public void setCancelOperaId(String cancelOperaId) {
        this.cancelOperaId = cancelOperaId;
    }

    public String getCancelOperaName() {
        return cancelOperaName;
    }

    public void setCancelOperaName(String cancelOperaName) {
        this.cancelOperaName = cancelOperaName;
    }

    public String getExecOrgCode() {
        return execOrgCode;
    }

    public void setExecOrgCode(String execOrgCode) {
        this.execOrgCode = execOrgCode;
    }

    public String getExecOrgName() {
        return execOrgName;
    }

    public void setExecOrgName(String execOrgName) {
        this.execOrgName = execOrgName;
    }

    public String getExecSystemCode() {
        return execSystemCode;
    }

    public void setExecSystemCode(String execSystemCode) {
        this.execSystemCode = execSystemCode;
    }

    public String getExecSystemName() {
        return execSystemName;
    }

    public void setExecSystemName(String execSystemName) {
        this.execSystemName = execSystemName;
    }

    public String getExecDeptId() {
        return execDeptId;
    }

    public void setExecDeptId(String execDeptId) {
        this.execDeptId = execDeptId;
    }

    public String getExecDeptName() {
        return execDeptName;
    }

    public void setExecDeptName(String execDeptName) {
        this.execDeptName = execDeptName;
    }

    public String getEmerFlag() {
        return emerFlag;
    }

    public void setEmerFlag(String emerFlag) {
        this.emerFlag = emerFlag;
    }

    public String getGreenChannelFlag() {
        return greenChannelFlag;
    }

    public void setGreenChannelFlag(String greenChannelFlag) {
        this.greenChannelFlag = greenChannelFlag;
    }

    public String getFeeAmount() {
        return feeAmount;
    }

    public void setFeeAmount(String feeAmount) {
        this.feeAmount = feeAmount;
    }

    public String getApplyStatusCode() {
        return applyStatusCode;
    }

    public void setApplyStatusCode(String applyStatusCode) {
        this.applyStatusCode = applyStatusCode;
    }

    public String getApplyStatusName() {
        return applyStatusName;
    }

    public void setApplyStatusName(String applyStatusName) {
        this.applyStatusName = applyStatusName;
    }

    public String getGestationWeek() {
        return gestationWeek;
    }

    public void setGestationWeek(String gestationWeek) {
        this.gestationWeek = gestationWeek;
    }

    public String getGestationDay() {
        return gestationDay;
    }

    public void setGestationDay(String gestationDay) {
        this.gestationDay = gestationDay;
    }

    public String getLastMenstrualPeriod() {
        return lastMenstrualPeriod;
    }

    public void setLastMenstrualPeriod(String lastMenstrualPeriod) {
        this.lastMenstrualPeriod = lastMenstrualPeriod;
    }

    public String getExecDateTime() {
        return execDateTime;
    }

    public void setExecDateTime(String execDateTime) {
        this.execDateTime = execDateTime;
    }

    public String getExecOperaId() {
        return execOperaId;
    }

    public void setExecOperaId(String execOperaId) {
        this.execOperaId = execOperaId;
    }

    public String getExecOperaName() {
        return execOperaName;
    }

    public void setExecOperaName(String execOperaName) {
        this.execOperaName = execOperaName;
    }

    public String getChargeQuantity() {
        return chargeQuantity;
    }

    public void setChargeQuantity(String chargeQuantity) {
        this.chargeQuantity = chargeQuantity;
    }
}
