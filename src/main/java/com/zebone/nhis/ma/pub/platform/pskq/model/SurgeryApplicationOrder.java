package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MapKey;
import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

import java.util.Date;

/**
 * @author lijin
 * 手术申请单消息模型
 */
public class SurgeryApplicationOrder {

    @MetadataDescribe(id= "LHDE0028001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0028002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MapKey("pkPv")
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

    //无
    @MetadataDescribe(id= "LHDE0028024",name = "手术申请ID",eName = "SURGERY_APPLY_ID")
    private String surgeryApplyId;

    @MapKey("codeApply")
    @MetadataDescribe(id= "LHDE0028025",name = "手术申请单号",eName = "SURGERY_APPLY_NO")
    private String surgeryApplyNo;

    @MetadataDescribe(id= "LHDE0028026",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    @MetadataDescribe(id= "LHDE0028027",name = "申请日期时间",eName = "APPLY_DATE_TIME")
    private String applyDateTime;

    @MetadataDescribe(id= "LHDE0028028",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0028029",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0028030",name = "申请医师ID",eName = "APPLY_DOCTOR_ID")
    private String applyDoctorId;

    @MetadataDescribe(id= "LHDE0028031",name = "申请医师姓名",eName = "APPLY_DOCTOR_NAME")
    private String applyDoctorName;

    @MetadataDescribe(id= "LHDE0028032",name = "术前诊断代码",eName = "DIAG_CODE_BEFORE_SURGERY")
    private String diagCodeBeforeSurgery;

    @MetadataDescribe(id= "LHDE0028033",name = "术前诊断名称",eName = "DIAG_NAME_BEFORE_SURGERY")
    private String diagNameBeforeSurgery;

    @MetadataDescribe(id= "LHDE0028034",name = "术前诊断描述",eName = "DIAG_DESC_BEFORE_SURGERY")
    private String diagDescBeforeSurgery;

    @MetadataDescribe(id= "LHDE0028035",name = "手术级别代码",eName = "SURGERY_GRADE_CODE")
    private String surgeryGradeCode;

    @MetadataDescribe(id= "LHDE0028036",name = "手术级别名称",eName = "SURGERY_GRADE_NAME")
    private String surgeryGradeName;

    @MetadataDescribe(id= "LHDE0028037",name = "手术/操作代码",eName = "SURGERY_CODE")
    private String surgeryCode;

    @MetadataDescribe(id= "LHDE0028038",name = "手术/操作名称",eName = "SURGERY_NAME")
    private String surgeryName;

    @MetadataDescribe(id= "LHDE0028039",name = "手术描述",eName = "SURGERY_DESC")
    private String surgeryDesc;

    @MetadataDescribe(id= "LHDE0028040",name = "麻醉方式代码",eName = "ANESTH_METHOD_CODE")
    private String anesthMethodCode;

    @MetadataDescribe(id= "LHDE0028041",name = "麻醉方式名称",eName = "ANESTH_METHOD_NAME")
    private String anesthMethodName;

    @MetadataDescribe(id= "LHDE0028042",name = "切口分类代码",eName = "INCISION_CLASS_CODE")
    private String incisionClassCode;

    @MetadataDescribe(id= "LHDE0028043",name = "切口分类名称",eName = "INCISION_CLASS_NAME")
    private String incisionClassName;

    @MetadataDescribe(id= "LHDE0028044",name = "手术部位代码",eName = "SURGERY_PART_CODE")
    private String surgeryPartCode;

    @MetadataDescribe(id= "LHDE0028045",name = "手术部位名称",eName = "SURGERY_PART_NAME")
    private String surgeryPartName;

    //无
    @MetadataDescribe(id= "LHDE0028046",name = "手术体位代码",eName = "SURGERY_POSITION_CODE")
    private String surgeryPositionCode;

    //无
    @MetadataDescribe(id= "LHDE0028047",name = "手术体位名称",eName = "SURGERY_POSITION_NAME")
    private String surgeryPositionName;

    @MetadataDescribe(id= "LHDE0028048",name = "计划开始日期时间",eName = "SCHEDULE_START_DATE_TIME")
    private String scheduleStartDateTime;

    //无
    @MetadataDescribe(id= "LHDE0028049",name = "计划结束日期时间",eName = "SCHEDULE_END_DATE_TIME")
    private String scheduleEndDateTime;

    @MetadataDescribe(id= "LHDE0028050",name = "手术室代码",eName = "ROOM_CODE")
    private String roomCode;

    @MetadataDescribe(id= "LHDE0028051",name = "手术室名称",eName = "ROOM_NAME")
    private String roomName;

    @MetadataDescribe(id= "LHDE0028052",name = "手术台次",eName = "SURGERY_SEQ_NO")
    private String surgerySeqNo;

    @MetadataDescribe(id= "LHDE0028053",name = "手术用物描述",eName = "SURGERY_USE_DESC")
    private String surgeryUseDesc;

    @MetadataDescribe(id= "LHDE0028054",name = "手术医师ID",eName = "SURGEON_ID")
    private String surgeonId;

    @MetadataDescribe(id= "LHDE0028055",name = "手术医师姓名",eName = "SURGEON_NAME")
    private String surgeonName;

    @MetadataDescribe(id= "LHDE0028056",name = "手术I助ID",eName = "ASSISTANT_FIRST_ID")
    private String assistantFirstId;

    @MetadataDescribe(id= "LHDE0028057",name = "手术I助姓名",eName = "ASSISTANT_FIRST_NAME")
    private String assistantFirstName;

    @MetadataDescribe(id= "LHDE0028058",name = "手术II助ID",eName = "ASSISTANT_SECOND_ID")
    private String assistantSecondId;

    @MetadataDescribe(id= "LHDE0028059",name = "手术II助姓名",eName = "ASSISTANT_SECOND_NAME")
    private String assistantSecondName;

    @MetadataDescribe(id= "LHDE0028060",name = "手术III助ID",eName = "ASSISTANT_THIRD_ID")
    private String assistantThirdId;

    @MetadataDescribe(id= "LHDE0028061",name = "手术III助姓名",eName = "ASSISTANT_THIRD_NAME")
    private String assistantThirdName;

    @MetadataDescribe(id= "LHDE0028062",name = "麻醉医师ID",eName = "ANESTHETIST_ID")
    private String anesthetistId;

    @MetadataDescribe(id= "LHDE0028063",name = "麻醉医师姓名",eName = "ANESTHETIST_NAME")
    private String anesthetistName;

    @MetadataDescribe(id= "LHDE0028064",name = "洗手护士ID",eName = "HAND_WASHING_NURSE_ID")
    private String handWashingNurseId;

    @MetadataDescribe(id= "LHDE0028065",name = "洗手护士姓名",eName = "HAND_WASHING_NURSE_NAME")
    private String handWashingNurseName;

    @MetadataDescribe(id= "LHDE0028066",name = "巡回护士ID",eName = "TOUR_NURSE_ID")
    private String tourNurseId;

    @MetadataDescribe(id= "LHDE0028067",name = "巡回护士姓名",eName = "TOUR_NURSE_NAME")
    private String tourNurseName;

    @MetadataDescribe(id= "LHDE0028068",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;

    @MetadataDescribe(id= "LHDE0028069",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    //无
    @MetadataDescribe(id= "LHDE0028070",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0028071",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MapKey("nameEmpInput")
    @MetadataDescribe(id= "LHDE0028072",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0028073",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0028074",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0028075",name = "主要手术标志",eName = "PRIMARY_SURGERY_FLAG")
    private String primarySurgeryFlag;

    //无
    @MetadataDescribe(id= "LHDE0028076",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;

    //无
    @MetadataDescribe(id= "LHDE0028077",name = "隔离标志",eName = "ISOLATION_FLAG")
    private String isolationFlag;

    @MetadataDescribe(id= "LHDE0028078",name = "申请单状态代码",eName = "APPLY_STATUS_CODE")
    private String applyStatusCode;

    @MetadataDescribe(id= "LHDE0028079",name = "申请单状态名称",eName = "APPLY_STATUS_NAME")
    private String applyStatusName;

    @MetadataDescribe(id= "LHDE0028081",name = "手术类型",eName = "OPERATION_TYPE")
    private String operationType;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

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

    public String getSurgeryApplyId() {
        return surgeryApplyId;
    }

    public void setSurgeryApplyId(String surgeryApplyId) {
        this.surgeryApplyId = surgeryApplyId;
    }

    public String getSurgeryApplyNo() {
        return surgeryApplyNo;
    }

    public void setSurgeryApplyNo(String surgeryApplyNo) {
        this.surgeryApplyNo = surgeryApplyNo;
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

    public String getDiagCodeBeforeSurgery() {
        return diagCodeBeforeSurgery;
    }

    public void setDiagCodeBeforeSurgery(String diagCodeBeforeSurgery) {
        this.diagCodeBeforeSurgery = diagCodeBeforeSurgery;
    }

    public String getDiagNameBeforeSurgery() {
        return diagNameBeforeSurgery;
    }

    public void setDiagNameBeforeSurgery(String diagNameBeforeSurgery) {
        this.diagNameBeforeSurgery = diagNameBeforeSurgery;
    }

    public String getDiagDescBeforeSurgery() {
        return diagDescBeforeSurgery;
    }

    public void setDiagDescBeforeSurgery(String diagDescBeforeSurgery) {
        this.diagDescBeforeSurgery = diagDescBeforeSurgery;
    }

    public String getSurgeryGradeCode() {
        return surgeryGradeCode;
    }

    public void setSurgeryGradeCode(String surgeryGradeCode) {
        this.surgeryGradeCode = surgeryGradeCode;
    }

    public String getSurgeryGradeName() {
        return surgeryGradeName;
    }

    public void setSurgeryGradeName(String surgeryGradeName) {
        this.surgeryGradeName = surgeryGradeName;
    }

    public String getSurgeryCode() {
        return surgeryCode;
    }

    public void setSurgeryCode(String surgeryCode) {
        this.surgeryCode = surgeryCode;
    }

    public String getSurgeryName() {
        return surgeryName;
    }

    public void setSurgeryName(String surgeryName) {
        this.surgeryName = surgeryName;
    }

    public String getSurgeryDesc() {
        return surgeryDesc;
    }

    public void setSurgeryDesc(String surgeryDesc) {
        this.surgeryDesc = surgeryDesc;
    }

    public String getAnesthMethodCode() {
        return anesthMethodCode;
    }

    public void setAnesthMethodCode(String anesthMethodCode) {
        this.anesthMethodCode = anesthMethodCode;
    }

    public String getAnesthMethodName() {
        return anesthMethodName;
    }

    public void setAnesthMethodName(String anesthMethodName) {
        this.anesthMethodName = anesthMethodName;
    }

    public String getIncisionClassCode() {
        return incisionClassCode;
    }

    public void setIncisionClassCode(String incisionClassCode) {
        this.incisionClassCode = incisionClassCode;
    }

    public String getIncisionClassName() {
        return incisionClassName;
    }

    public void setIncisionClassName(String incisionClassName) {
        this.incisionClassName = incisionClassName;
    }

    public String getSurgeryPartCode() {
        return surgeryPartCode;
    }

    public void setSurgeryPartCode(String surgeryPartCode) {
        this.surgeryPartCode = surgeryPartCode;
    }

    public String getSurgeryPartName() {
        return surgeryPartName;
    }

    public void setSurgeryPartName(String surgeryPartName) {
        this.surgeryPartName = surgeryPartName;
    }

    public String getSurgeryPositionCode() {
        return surgeryPositionCode;
    }

    public void setSurgeryPositionCode(String surgeryPositionCode) {
        this.surgeryPositionCode = surgeryPositionCode;
    }

    public String getSurgeryPositionName() {
        return surgeryPositionName;
    }

    public void setSurgeryPositionName(String surgeryPositionName) {
        this.surgeryPositionName = surgeryPositionName;
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

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSurgerySeqNo() {
        return surgerySeqNo;
    }

    public void setSurgerySeqNo(String surgerySeqNo) {
        this.surgerySeqNo = surgerySeqNo;
    }

    public String getSurgeryUseDesc() {
        return surgeryUseDesc;
    }

    public void setSurgeryUseDesc(String surgeryUseDesc) {
        this.surgeryUseDesc = surgeryUseDesc;
    }

    public String getSurgeonId() {
        return surgeonId;
    }

    public void setSurgeonId(String surgeonId) {
        this.surgeonId = surgeonId;
    }

    public String getSurgeonName() {
        return surgeonName;
    }

    public void setSurgeonName(String surgeonName) {
        this.surgeonName = surgeonName;
    }

    public String getAssistantFirstId() {
        return assistantFirstId;
    }

    public void setAssistantFirstId(String assistantFirstId) {
        this.assistantFirstId = assistantFirstId;
    }

    public String getAssistantFirstName() {
        return assistantFirstName;
    }

    public void setAssistantFirstName(String assistantFirstName) {
        this.assistantFirstName = assistantFirstName;
    }

    public String getAssistantSecondId() {
        return assistantSecondId;
    }

    public void setAssistantSecondId(String assistantSecondId) {
        this.assistantSecondId = assistantSecondId;
    }

    public String getAssistantSecondName() {
        return assistantSecondName;
    }

    public void setAssistantSecondName(String assistantSecondName) {
        this.assistantSecondName = assistantSecondName;
    }

    public String getAssistantThirdId() {
        return assistantThirdId;
    }

    public void setAssistantThirdId(String assistantThirdId) {
        this.assistantThirdId = assistantThirdId;
    }

    public String getAssistantThirdName() {
        return assistantThirdName;
    }

    public void setAssistantThirdName(String assistantThirdName) {
        this.assistantThirdName = assistantThirdName;
    }

    public String getAnesthetistId() {
        return anesthetistId;
    }

    public void setAnesthetistId(String anesthetistId) {
        this.anesthetistId = anesthetistId;
    }

    public String getAnesthetistName() {
        return anesthetistName;
    }

    public void setAnesthetistName(String anesthetistName) {
        this.anesthetistName = anesthetistName;
    }

    public String getHandWashingNurseId() {
        return handWashingNurseId;
    }

    public void setHandWashingNurseId(String handWashingNurseId) {
        this.handWashingNurseId = handWashingNurseId;
    }

    public String getHandWashingNurseName() {
        return handWashingNurseName;
    }

    public void setHandWashingNurseName(String handWashingNurseName) {
        this.handWashingNurseName = handWashingNurseName;
    }

    public String getTourNurseId() {
        return tourNurseId;
    }

    public void setTourNurseId(String tourNurseId) {
        this.tourNurseId = tourNurseId;
    }

    public String getTourNurseName() {
        return tourNurseName;
    }

    public void setTourNurseName(String tourNurseName) {
        this.tourNurseName = tourNurseName;
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

    public String getPrimarySurgeryFlag() {
        return primarySurgeryFlag;
    }

    public void setPrimarySurgeryFlag(String primarySurgeryFlag) {
        this.primarySurgeryFlag = primarySurgeryFlag;
    }

    public String getEmerFlag() {
        return emerFlag;
    }

    public void setEmerFlag(String emerFlag) {
        this.emerFlag = emerFlag;
    }

    public String getIsolationFlag() {
        return isolationFlag;
    }

    public void setIsolationFlag(String isolationFlag) {
        this.isolationFlag = isolationFlag;
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
}
