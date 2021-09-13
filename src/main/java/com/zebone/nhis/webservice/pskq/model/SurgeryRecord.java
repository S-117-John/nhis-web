package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

public class SurgeryRecord {
    @MetadataDescribe(id= "LHDE0029001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;
    @MetadataDescribe(id= "LHDE0029002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;
    @MetadataDescribe(id= "LHDE0029003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;
    @MetadataDescribe(id= "LHDE0029004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;
    @MetadataDescribe(id= "LHDE0029005",name = "机构/院部代码",eName = "ORG_NAME")
    private String orgName;
    @MetadataDescribe(id= "LHDE0029006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;
    @MetadataDescribe(id= "LHDE0029007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;
    @MetadataDescribe(id= "LHDE0029008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;
    @MetadataDescribe(id= "LHDE0029009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;
    @MetadataDescribe(id= "LHDE0029010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;
    @MetadataDescribe(id= "LHDE0029011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;
    @MetadataDescribe(id= "LHDE0029012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;
    @MetadataDescribe(id= "LHDE0029013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;
    @MetadataDescribe(id= "LHDE0029014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;
    @MetadataDescribe(id= "LHDE0029015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;
    @MetadataDescribe(id= "LHDE0029016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;
    @MetadataDescribe(id= "LHDE0029017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;
    @MetadataDescribe(id= "LHDE0029018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;
    @MetadataDescribe(id= "LHDE0029019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;
    @MetadataDescribe(id= "LHDE0029020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;
    @MetadataDescribe(id= "LHDE0029021",name = "病区ID",eName = "WARD_ID")
    private String wardId;
    @MetadataDescribe(id= "LHDE0029021",name = "病区名称",eName = "WARD_NAME")
    private String wardName;
    @MetadataDescribe(id= "LHDE0029023",name = "床号",eName = "BED_NO")
    private String bedNo;
    @MetadataDescribe(id= "LHDE0029024",name = "手术记录ID",eName = "SURGERY_RECORD_ID")
    private String surgeryRecordId;
    @MetadataDescribe(id= "LHDE0029025",name = "手术记录序号",eName = "SURGERY_RECORD_NO")
    private String surgeryRecordNo;
    @MetadataDescribe(id= "LHDE0029026",name = "手术申请ID",eName = "SURGERY_APPLY_ID")
    private String surgeryApplyId;
    @MetadataDescribe(id= "LHDE0029027",name = "手术申请单号",eName = "SURGERY_APPLY_NO")
    private String surgeryApplyNo;
    @MetadataDescribe(id= "LHDE0029028",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;
    @MetadataDescribe(id= "LHDE0029029",name = "申请日期时间",eName = "APPLY_DATE_TIME")
    private String applyDateTime;
    @MetadataDescribe(id= "LHDE0029030",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;
    @MetadataDescribe(id= "LHDE0029031",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;
    @MetadataDescribe(id= "LHDE0029032",name = "申请医师ID",eName = "APPLY_DOCTOR_ID")
    private String applyDoctorId;
    @MetadataDescribe(id= "LHDE0029033",name = "申请医师姓名",eName = "APPLY_DOCTOR_NAME")
    private String applyDoctorName;
    @MetadataDescribe(id= "LHDE0029034",name = "手术史标志",eName = "SURGERY_HISTORY_FLAG")
    private String surgeryHistoryFlag;
    @MetadataDescribe(id= "LHDE0029035",name = "术前诊断代码",eName = "DIAG_CODE_BEFORE_SURGERY")
    private String diagCodeBeforeSurgery;
    @MetadataDescribe(id= "LHDE0029036",name = "术前诊断名称",eName = "DIAG_NAME_BEFORE_SURGERY")
    private String diagNameBeforeSurgery;
    @MetadataDescribe(id= "LHDE0029037",name = "术前诊断描述",eName = "DIAG_DESC_BEFORE_SURGERY")
    private String diagDescBeforeSurgery;
    @MetadataDescribe(id= "LHDE0029038",name = "病情描述",eName = "DISEASE_DESC")
    private String diseaseDesc;
    @MetadataDescribe(id= "LHDE0029039",name = "术后诊断代码",eName = "DIAG_CODE_AFTER_SURGERY")
    private String diagCodeAfterSurgery;
    @MetadataDescribe(id= "LHDE0029040",name = "术后诊断名称",eName = "DIAG_NAME_AFTER_SURGERY")
    private String diagNameAfterSurgery;
    @MetadataDescribe(id= "LHDE0029041",name = "术后诊断描述",eName = "DIAG_DESC_AFTER_SURGERY")
    private String diagDescAfterSurgery;
    @MetadataDescribe(id= "LHDE0029042",name = "手术级别代码",eName = "SURGERY_GRADE_CODE")
    private String surgeryGradeCode;
    @MetadataDescribe(id= "LHDE0029043",name = "手术级别名称",eName = "SURGERY_GRADE_NAME")
    private String surgeryGradeName;
    @MetadataDescribe(id= "LHDE0029044",name = "手术/操作代码",eName = "SURGERY_CODE")
    private String surgeryCode;
    @MetadataDescribe(id= "LHDE0029045",name = "手术/操作名称",eName = "SURGERY_NAME")
    private String surgeryName;
    @MetadataDescribe(id= "LHDE0029046",name = "手术描述",eName = "SURGERY_DESC")
    private String surgeryDesc;
    @MetadataDescribe(id= "LHDE0029047",name = "麻醉方式代码",eName = "ANESTH_METHOD_CODE")
    private String anesthMethodCode;
    @MetadataDescribe(id= "LHDE0029048",name = "麻醉方式名称",eName = "ANESTH_METHOD_NAME")
    private String anesthMethodName;
    @MetadataDescribe(id= "LHDE0029049",name = "麻醉满意程度代码",eName = "ANESTH_SATISFACTION_CODE")
    private String anesthSatisfactionCode;
    @MetadataDescribe(id= "LHDE0029050",name = "麻醉满意程度名称",eName = "ANESTH_SATISFACTION_NAME")
    private String anesthSatisfactionName;
    @MetadataDescribe(id= "LHDE0029051",name = "切口分类代码",eName = "INCISION_CLASS_CODE")
    private String incisionClassCode;
    @MetadataDescribe(id= "LHDE0029052",name = "切口分类名称",eName = "INCISION_CLASS_NAME")
    private String incisionClassName;
    @MetadataDescribe(id= "LHDE0029053",name = "切口数量",eName = "INCISION_QUANTITY")
    private String incisionQuantity;
    @MetadataDescribe(id= "LHDE0029054",name = "愈合等级代码",eName = "HEALING_GRADE_CODE")
    private String healingGradeCode;
    @MetadataDescribe(id= "LHDE0029055",name = "愈合等级名称",eName = "HEALING_GRADE_NAME")
    private String healingGradeName;
    @MetadataDescribe(id= "LHDE0029056",name = "手术部位代码",eName = "SURGERY_PART_CODE")
    private String surgeryPartCode;
    @MetadataDescribe(id= "LHDE0029057",name = "手术部位名称",eName = "SURGERY_PART_NAME")
    private String SurgeryPartName;
    @MetadataDescribe(id= "LHDE0029058",name = "手术体位代码",eName = "SURGERY_POSITION_CODE")
    private String surgeryPositionCode;
    @MetadataDescribe(id= "LHDE0029059",name = "手术体位名称",eName = "SURGERY_POSITION_NAME")
    private String surgeryPositionName;
    @MetadataDescribe(id= "LHDE0029060",name = "计划日期时间",eName = "SCHEDULE_DATE_TIME")
    private String scheduleDateTime;
    @MetadataDescribe(id= "LHDE0029061",name = "手术开始日期时间",eName = "SURGERY_START_DATE_TIME")
    private String surgeryStartDateTime;
    @MetadataDescribe(id= "LHDE0029062",name = "手术结束日期时间",eName = "SURGERY_END_DATE_TIME")
    private String surgeryEndDateTime;
    @MetadataDescribe(id= "LHDE0029063",name = "进入手术室日期时间",eName = "IN_DATE_TIME")
    private String inDateTime;
    @MetadataDescribe(id= "LHDE0029064",name = "离开手术室日期时间",eName = "OUT_DATE_TIME")
    private String outDateTime;
    @MetadataDescribe(id= "LHDE0029065",name = "进入PACU日期时间",eName = "IN_PACU_DATE_TIME")
    private String inPacuDateTime;
    @MetadataDescribe(id= "LHDE0029066",name = "离开PACU日期时间",eName = "OUT_PACU_DATE_TIME")
    private String outPacuDateTime;
    @MetadataDescribe(id= "LHDE0029067",name = "麻醉开始日期时间",eName = "ANESTH_START_DATE_TIME")
    private String anesthStartDateTime;
    @MetadataDescribe(id= "LHDE0029068",name = "麻醉结束日期时间",eName = "ANESTH_END_DATE_TIME")
    private String anesthEndDateTime;
    @MetadataDescribe(id= "LHDE0029069",name = "手术室代码",eName = "ROOM_CODE")
    private String roomCode;
    @MetadataDescribe(id= "LHDE0029070",name = "手术室名称",eName = "ROOM_NAME")
    private String roomName;
    @MetadataDescribe(id= "LHDE0029071",name = "手术间号",eName = "ROOM_NO")
    private String roomNo;
    @MetadataDescribe(id= "LHDE0029072",name = "手术台次",eName = "SURGERY_SEQ_NO")
    private String surgerySeqNo;
    @MetadataDescribe(id= "LHDE0029073",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;
    @MetadataDescribe(id= "LHDE0029074",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;
    @MetadataDescribe(id= "LHDE0029075",name = "手术医师ID",eName = "SURGEON_ID")
    private String surgeonId;
    @MetadataDescribe(id= "LHDE0029076",name = "手术医师姓名",eName = "SURGEON_NAME")
    private String surgeonName;
    @MetadataDescribe(id= "LHDE0029077",name = "手术I助ID",eName = "ASSISTANT_FIRST_ID")
    private String assistantFirstId;
    @MetadataDescribe(id= "LHDE0029078",name = "手术I助姓名",eName = "ASSISTANT_FIRST_NAME")
    private String assistantFirstName;
    @MetadataDescribe(id= "LHDE0029079",name = "手术II助ID",eName = "ASSISTANT_SECOND_ID")
    private String assistantSecondId;
    @MetadataDescribe(id= "LHDE0029080",name = "手术II助姓名",eName = "ASSISTANT_SECOND_NAME")
    private String assistantSecondName;
    @MetadataDescribe(id= "LHDE0029081",name = "手术III助ID",eName = "ASSISTANT_THIRD_ID")
    private String assistantThirdId;
    @MetadataDescribe(id= "LHDE0029082",name = "手术III助姓名",eName = "ASSISTANT_THIRD_NAME")
    private String assistantThirdName;
    @MetadataDescribe(id= "LHDE0029083",name = "麻醉医师ID",eName = "ANESTHETIST_ID")
    private String anesthetistId;
    @MetadataDescribe(id= "LHDE0029084",name = "麻醉医师姓名",eName = "ANESTHETIST_NAME")
    private String anesthetistName;
    @MetadataDescribe(id= "LHDE0029085",name = "麻醉I助ID",eName = "ANESTH_ASSISTANT_FIRST_ID")
    private String anesthAssistantFirstId;
    @MetadataDescribe(id= "LHDE0029086",name = "麻醉I助姓名",eName = "ANESTH_ASSISTANT_FIRST_NAME")
    private String anesthAssistantFirstName;
    @MetadataDescribe(id= "LHDE0029087",name = "麻醉II助ID",eName = "ANESTH_ASSISTANT_SECOND_ID")
    private String anesthAssistantSecondId;
    @MetadataDescribe(id= "LHDE0029088",name = "麻醉II助姓名",eName = "ANESTH_ASSISTANT_SECOND_NAME")
    private String anesthAssistantSecondName;
    @MetadataDescribe(id= "LHDE0029089",name = "洗手护士ID",eName = "HAND_WASHING_NURSE_ID")
    private String handWashingNurseId;
    @MetadataDescribe(id= "LHDE0029090",name = "洗手护士姓名",eName = "HAND_WASHING_NURSE_NAME")
    private String handWashingNurseName;
    @MetadataDescribe(id= "LHDE0029091",name = "巡回护士ID",eName = "TOUR_NURSE_ID")
    private String tourNurseId;
    @MetadataDescribe(id= "LHDE0029092",name = "巡回护士姓名",eName = "TOUR_NURSE_NAME")
    private String tourNurseName;
    @MetadataDescribe(id= "LHDE0029093",name = "第一上台护士ID",eName = "NURSE_FIRST_ID")
    private String nurseFirstId;
    @MetadataDescribe(id= "LHDE0029094",name = "第一上台护士姓名",eName = "NURSE_FIRST_NAME")
    private String nurseFirstName;
    @MetadataDescribe(id= "LHDE0029095",name = "第二上台护士ID",eName = "NURSE_SECOND_ID")
    private String nurseSecondId;
    @MetadataDescribe(id= "LHDE0029096",name = "第二上台护士姓名",eName = "NURSE_SECOND_NAME")
    private String nurseSecondName;
    @MetadataDescribe(id= "LHDE0029097",name = "第一供应护士ID",eName = "SUPPLY_NURSE_FIRST_ID")
    private String supplyNurseFirstId;
    @MetadataDescribe(id= "LHDE0029098",name = "第一供应护士姓名",eName = "SUPPLY_NURSE_FIRST_NAME")
    private String supplyNurseFirstName;
    @MetadataDescribe(id= "LHDE0029099",name = "第二供应护士ID",eName = "SUPPLY_NURSE_SECOND_ID")
    private String supplyNurseSecondId;
    @MetadataDescribe(id= "LHDE0029100",name = "第二供应护士姓名",eName = "SUPPLY_NURSE_SECOND_NAME")
    private String supplyNurseSecondName;
    @MetadataDescribe(id= "LHDE0029101",name = "主要手术标志",eName = "PRIMARY_SURGERY_FLAG")
    private String primarySurgeryFlag;
    @MetadataDescribe(id= "LHDE0029102",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;
    @MetadataDescribe(id= "LHDE0029103",name = "隔离标志",eName = "ISOLATION_FLAG")
    private String isolationFlag;
    @MetadataDescribe(id= "LHDE0029104",name = "植入手术标志",eName = "IMPLANT_FLAG")
    private String implantFlag;
    @MetadataDescribe(id= "LHDE0029105",name = "院感标志",eName = "INFECTION_FLAG")
    private String infectionFlag;
    @MetadataDescribe(id= "LHDE0029106",name = "DSA手术标志",eName = "DSA_FLAG")
    private String dsaFlag;
    @MetadataDescribe(id= "LHDE0029107",name = "内镜手术标志",eName = "ENDOSCOPE_FLAG")
    private String endoscopeFlag;
    @MetadataDescribe(id= "LHDE0029108",name = "外科引流手术标志",eName = "SURGICAL_DRAINAGE_FLAG")
    private String surgicalDrainageFlag;
    @MetadataDescribe(id= "LHDE0029109",name = "备皮标志",eName = "SKIN_PREPARATION_FLAG")
    private String skinPreparationFlag;
    @MetadataDescribe(id= "LHDE0029110",name = "皮肤消毒描述",eName = "SKIN_DISINFECTION_DESC")
    private String skinDisinfectionDesc;
    @MetadataDescribe(id= "LHDE0029111",name = "接台手术标志",eName = "CONSECUTION_FLAG")
    private String consecutionFlag;
    @MetadataDescribe(id= "LHDE0029112",name = "术前ASA分级代码",eName = "ASA_GRADE_CODE")
    private String asaGradeCode;
    @MetadataDescribe(id= "LHDE0029113",name = "术前ASA分级名称",eName = "ASA_GRADE_NAME")
    private String asaGradeName;
    @MetadataDescribe(id= "LHDE0029114",name = "手术经过描述",eName = "SURGERY_PROCEDURE_DESC")
    private String surgeryProcedureDesc;
    @MetadataDescribe(id= "LHDE0029115",name = "手术并发症描述",eName = "COMPLICATION_DESC")
    private String complicationDesc;
    @MetadataDescribe(id= "LHDE0029116",name = "术前用药",eName = "PRE_SURGERY_DRUG")
    private String preSurgeryDrug;
    @MetadataDescribe(id= "LHDE0029117",name = "术中用药",eName = "SURGERY_DRUG")
    private String surgeryDrug;
    @MetadataDescribe(id= "LHDE0029118",name = "介入物名称",eName = "INTERVENTION")
    private String intervention;
    @MetadataDescribe(id= "LHDE0029119",name = "手术用物描述",eName = "SURGERY_USE_DESC")
    private String surgeryUseDesc;
    @MetadataDescribe(id= "LHDE0029120",name = "输液量",eName = "IN_FLUIDS_AMOUNT")
    private String inFluidsAmount;
    @MetadataDescribe(id= "LHDE0029121",name = "尿量（ML）",eName = "OUT_FLUIDS_AMOUNT")
    private String outFluidsAmount;
    @MetadataDescribe(id= "LHDE0029122",name = "出血量",eName = "BLOOD_LOSS_AMOUNT")
    private String bloodLossAmount;
    @MetadataDescribe(id= "LHDE0029123",name = "输血量（mL）",eName = "BLOOD_TRANSFER_AMOUNT")
    private String bloodTransferAmount;
    @MetadataDescribe(id= "LHDE0029124",name = "输血反应标志",eName = "BLOOD_TRANSFER_REACTION_FLAG")
    private String bloodTransferReactionFlag;
    @MetadataDescribe(id= "LHDE0029125",name = "术后送标本件数",eName = "SPECIMEN_QUANTITY")
    private String specimenQuantity;
    @MetadataDescribe(id= "LHDE0029126",name = "术后送标本去向科室ID",eName = "SPECIMEN_DEPT_ID")
    private String specimenDeptId;
    @MetadataDescribe(id= "LHDE0029127",name = "术后送标本去向科室名称",eName = "SPECIMEN_DEPT_NAME")
    private String specimenDeptName;
    @MetadataDescribe(id= "LHDE0029128",name = "手术状态代码",eName = "SURGERY_STATUS_CODE")
    private String surgeryStatusCode;
    @MetadataDescribe(id= "LHDE0029129",name = "手术状态名称",eName = "SURGERY_STATUS_NAME")
    private String surgeryStatusName;
    @MetadataDescribe(id= "LHDE0029130",name = "文书序号",eName = "DOCUMENT_NO")
    private String documentNo;
    @MetadataDescribe(id= "LHDE0029131",name = "文书内容_PDF",eName = "DOCUMENT_CONTENT_PDF")
    private String documentContentPdf;
    @MetadataDescribe(id= "LHDE0029132",name = "文书备注",eName = "DOCUMENT_CMMT")
    private String documentCmmt;
    @MetadataDescribe(id= "LHDE0029133",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;
    @MetadataDescribe(id= "LHDE0029134",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;
    @MetadataDescribe(id= "LHDE0029135",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;
    @MetadataDescribe(id= "LHDE0029136",name = "签署人ID",eName = "SIGN_OPERA_ID")
    private String signOperaId;
    @MetadataDescribe(id= "LHDE0029137",name = "签署人姓名",eName = "SIGN_OPERA_NAME")
    private String signOperaName;
    @MetadataDescribe(id= "LHDE0029138",name = "签名日期时间",eName = "SIGN_DATE_TIME")
    private String signDateTime;
    @MetadataDescribe(id= "LHDE0029139",name = "复审人ID",eName = "REVIEW_OPERA_ID")
    private String reviewOperaId;
    @MetadataDescribe(id= "LHDE0029140",name = "复审人姓名",eName = "REVIEW_OPERA_NAME")
    private String reviewOperaName;
    @MetadataDescribe(id= "LHDE0029141",name = "复审日期时间",eName = "REVIEW_DATE_TIME")
    private String reviewDateTime;
    @MetadataDescribe(id= "LHDE0029142",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;
    @MetadataDescribe(id= "LHDE0029143",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;
    @MetadataDescribe(id= "LHDE0029144",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;

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

    public String getSurgeryRecordId() {
        return surgeryRecordId;
    }

    public void setSurgeryRecordId(String surgeryRecordId) {
        this.surgeryRecordId = surgeryRecordId;
    }

    public String getSurgeryRecordNo() {
        return surgeryRecordNo;
    }

    public void setSurgeryRecordNo(String surgeryRecordNo) {
        this.surgeryRecordNo = surgeryRecordNo;
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

    public String getSurgeryHistoryFlag() {
        return surgeryHistoryFlag;
    }

    public void setSurgeryHistoryFlag(String surgeryHistoryFlag) {
        this.surgeryHistoryFlag = surgeryHistoryFlag;
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

    public String getDiseaseDesc() {
        return diseaseDesc;
    }

    public void setDiseaseDesc(String diseaseDesc) {
        this.diseaseDesc = diseaseDesc;
    }

    public String getDiagCodeAfterSurgery() {
        return diagCodeAfterSurgery;
    }

    public void setDiagCodeAfterSurgery(String diagCodeAfterSurgery) {
        this.diagCodeAfterSurgery = diagCodeAfterSurgery;
    }

    public String getDiagNameAfterSurgery() {
        return diagNameAfterSurgery;
    }

    public void setDiagNameAfterSurgery(String diagNameAfterSurgery) {
        this.diagNameAfterSurgery = diagNameAfterSurgery;
    }

    public String getDiagDescAfterSurgery() {
        return diagDescAfterSurgery;
    }

    public void setDiagDescAfterSurgery(String diagDescAfterSurgery) {
        this.diagDescAfterSurgery = diagDescAfterSurgery;
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

    public String getAnesthSatisfactionCode() {
        return anesthSatisfactionCode;
    }

    public void setAnesthSatisfactionCode(String anesthSatisfactionCode) {
        this.anesthSatisfactionCode = anesthSatisfactionCode;
    }

    public String getAnesthSatisfactionName() {
        return anesthSatisfactionName;
    }

    public void setAnesthSatisfactionName(String anesthSatisfactionName) {
        this.anesthSatisfactionName = anesthSatisfactionName;
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

    public String getIncisionQuantity() {
        return incisionQuantity;
    }

    public void setIncisionQuantity(String incisionQuantity) {
        this.incisionQuantity = incisionQuantity;
    }

    public String getHealingGradeCode() {
        return healingGradeCode;
    }

    public void setHealingGradeCode(String healingGradeCode) {
        this.healingGradeCode = healingGradeCode;
    }

    public String getHealingGradeName() {
        return healingGradeName;
    }

    public void setHealingGradeName(String healingGradeName) {
        this.healingGradeName = healingGradeName;
    }

    public String getSurgeryPartCode() {
        return surgeryPartCode;
    }

    public void setSurgeryPartCode(String surgeryPartCode) {
        this.surgeryPartCode = surgeryPartCode;
    }

    public String getSurgeryPartName() {
        return SurgeryPartName;
    }

    public void setSurgeryPartName(String surgeryPartName) {
        SurgeryPartName = surgeryPartName;
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

    public String getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
    }

    public String getSurgeryStartDateTime() {
        return surgeryStartDateTime;
    }

    public void setSurgeryStartDateTime(String surgeryStartDateTime) {
        this.surgeryStartDateTime = surgeryStartDateTime;
    }

    public String getSurgeryEndDateTime() {
        return surgeryEndDateTime;
    }

    public void setSurgeryEndDateTime(String surgeryEndDateTime) {
        this.surgeryEndDateTime = surgeryEndDateTime;
    }

    public String getInDateTime() {
        return inDateTime;
    }

    public void setInDateTime(String inDateTime) {
        this.inDateTime = inDateTime;
    }

    public String getOutDateTime() {
        return outDateTime;
    }

    public void setOutDateTime(String outDateTime) {
        this.outDateTime = outDateTime;
    }

    public String getInPacuDateTime() {
        return inPacuDateTime;
    }

    public void setInPacuDateTime(String inPacuDateTime) {
        this.inPacuDateTime = inPacuDateTime;
    }

    public String getOutPacuDateTime() {
        return outPacuDateTime;
    }

    public void setOutPacuDateTime(String outPacuDateTime) {
        this.outPacuDateTime = outPacuDateTime;
    }

    public String getAnesthStartDateTime() {
        return anesthStartDateTime;
    }

    public void setAnesthStartDateTime(String anesthStartDateTime) {
        this.anesthStartDateTime = anesthStartDateTime;
    }

    public String getAnesthEndDateTime() {
        return anesthEndDateTime;
    }

    public void setAnesthEndDateTime(String anesthEndDateTime) {
        this.anesthEndDateTime = anesthEndDateTime;
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

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getSurgerySeqNo() {
        return surgerySeqNo;
    }

    public void setSurgerySeqNo(String surgerySeqNo) {
        this.surgerySeqNo = surgerySeqNo;
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

    public String getAnesthAssistantFirstId() {
        return anesthAssistantFirstId;
    }

    public void setAnesthAssistantFirstId(String anesthAssistantFirstId) {
        this.anesthAssistantFirstId = anesthAssistantFirstId;
    }

    public String getAnesthAssistantFirstName() {
        return anesthAssistantFirstName;
    }

    public void setAnesthAssistantFirstName(String anesthAssistantFirstName) {
        this.anesthAssistantFirstName = anesthAssistantFirstName;
    }

    public String getAnesthAssistantSecondId() {
        return anesthAssistantSecondId;
    }

    public void setAnesthAssistantSecondId(String anesthAssistantSecondId) {
        this.anesthAssistantSecondId = anesthAssistantSecondId;
    }

    public String getAnesthAssistantSecondName() {
        return anesthAssistantSecondName;
    }

    public void setAnesthAssistantSecondName(String anesthAssistantSecondName) {
        this.anesthAssistantSecondName = anesthAssistantSecondName;
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

    public String getNurseFirstId() {
        return nurseFirstId;
    }

    public void setNurseFirstId(String nurseFirstId) {
        this.nurseFirstId = nurseFirstId;
    }

    public String getNurseFirstName() {
        return nurseFirstName;
    }

    public void setNurseFirstName(String nurseFirstName) {
        this.nurseFirstName = nurseFirstName;
    }

    public String getNurseSecondId() {
        return nurseSecondId;
    }

    public void setNurseSecondId(String nurseSecondId) {
        this.nurseSecondId = nurseSecondId;
    }

    public String getNurseSecondName() {
        return nurseSecondName;
    }

    public void setNurseSecondName(String nurseSecondName) {
        this.nurseSecondName = nurseSecondName;
    }

    public String getSupplyNurseFirstId() {
        return supplyNurseFirstId;
    }

    public void setSupplyNurseFirstId(String supplyNurseFirstId) {
        this.supplyNurseFirstId = supplyNurseFirstId;
    }

    public String getSupplyNurseFirstName() {
        return supplyNurseFirstName;
    }

    public void setSupplyNurseFirstName(String supplyNurseFirstName) {
        this.supplyNurseFirstName = supplyNurseFirstName;
    }

    public String getSupplyNurseSecondId() {
        return supplyNurseSecondId;
    }

    public void setSupplyNurseSecondId(String supplyNurseSecondId) {
        this.supplyNurseSecondId = supplyNurseSecondId;
    }

    public String getSupplyNurseSecondName() {
        return supplyNurseSecondName;
    }

    public void setSupplyNurseSecondName(String supplyNurseSecondName) {
        this.supplyNurseSecondName = supplyNurseSecondName;
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

    public String getImplantFlag() {
        return implantFlag;
    }

    public void setImplantFlag(String implantFlag) {
        this.implantFlag = implantFlag;
    }

    public String getInfectionFlag() {
        return infectionFlag;
    }

    public void setInfectionFlag(String infectionFlag) {
        this.infectionFlag = infectionFlag;
    }

    public String getDsaFlag() {
        return dsaFlag;
    }

    public void setDsaFlag(String dsaFlag) {
        this.dsaFlag = dsaFlag;
    }

    public String getEndoscopeFlag() {
        return endoscopeFlag;
    }

    public void setEndoscopeFlag(String endoscopeFlag) {
        this.endoscopeFlag = endoscopeFlag;
    }

    public String getSurgicalDrainageFlag() {
        return surgicalDrainageFlag;
    }

    public void setSurgicalDrainageFlag(String surgicalDrainageFlag) {
        this.surgicalDrainageFlag = surgicalDrainageFlag;
    }

    public String getSkinPreparationFlag() {
        return skinPreparationFlag;
    }

    public void setSkinPreparationFlag(String skinPreparationFlag) {
        this.skinPreparationFlag = skinPreparationFlag;
    }

    public String getSkinDisinfectionDesc() {
        return skinDisinfectionDesc;
    }

    public void setSkinDisinfectionDesc(String skinDisinfectionDesc) {
        this.skinDisinfectionDesc = skinDisinfectionDesc;
    }

    public String getConsecutionFlag() {
        return consecutionFlag;
    }

    public void setConsecutionFlag(String consecutionFlag) {
        this.consecutionFlag = consecutionFlag;
    }

    public String getAsaGradeCode() {
        return asaGradeCode;
    }

    public void setAsaGradeCode(String asaGradeCode) {
        this.asaGradeCode = asaGradeCode;
    }

    public String getAsaGradeName() {
        return asaGradeName;
    }

    public void setAsaGradeName(String asaGradeName) {
        this.asaGradeName = asaGradeName;
    }

    public String getSurgeryProcedureDesc() {
        return surgeryProcedureDesc;
    }

    public void setSurgeryProcedureDesc(String surgeryProcedureDesc) {
        this.surgeryProcedureDesc = surgeryProcedureDesc;
    }

    public String getComplicationDesc() {
        return complicationDesc;
    }

    public void setComplicationDesc(String complicationDesc) {
        this.complicationDesc = complicationDesc;
    }

    public String getPreSurgeryDrug() {
        return preSurgeryDrug;
    }

    public void setPreSurgeryDrug(String preSurgeryDrug) {
        this.preSurgeryDrug = preSurgeryDrug;
    }

    public String getSurgeryDrug() {
        return surgeryDrug;
    }

    public void setSurgeryDrug(String surgeryDrug) {
        this.surgeryDrug = surgeryDrug;
    }

    public String getIntervention() {
        return intervention;
    }

    public void setIntervention(String intervention) {
        this.intervention = intervention;
    }

    public String getSurgeryUseDesc() {
        return surgeryUseDesc;
    }

    public void setSurgeryUseDesc(String surgeryUseDesc) {
        this.surgeryUseDesc = surgeryUseDesc;
    }

    public String getInFluidsAmount() {
        return inFluidsAmount;
    }

    public void setInFluidsAmount(String inFluidsAmount) {
        this.inFluidsAmount = inFluidsAmount;
    }

    public String getOutFluidsAmount() {
        return outFluidsAmount;
    }

    public void setOutFluidsAmount(String outFluidsAmount) {
        this.outFluidsAmount = outFluidsAmount;
    }

    public String getBloodLossAmount() {
        return bloodLossAmount;
    }

    public void setBloodLossAmount(String bloodLossAmount) {
        this.bloodLossAmount = bloodLossAmount;
    }

    public String getBloodTransferAmount() {
        return bloodTransferAmount;
    }

    public void setBloodTransferAmount(String bloodTransferAmount) {
        this.bloodTransferAmount = bloodTransferAmount;
    }

    public String getBloodTransferReactionFlag() {
        return bloodTransferReactionFlag;
    }

    public void setBloodTransferReactionFlag(String bloodTransferReactionFlag) {
        this.bloodTransferReactionFlag = bloodTransferReactionFlag;
    }

    public String getSpecimenQuantity() {
        return specimenQuantity;
    }

    public void setSpecimenQuantity(String specimenQuantity) {
        this.specimenQuantity = specimenQuantity;
    }

    public String getSpecimenDeptId() {
        return specimenDeptId;
    }

    public void setSpecimenDeptId(String specimenDeptId) {
        this.specimenDeptId = specimenDeptId;
    }

    public String getSpecimenDeptName() {
        return specimenDeptName;
    }

    public void setSpecimenDeptName(String specimenDeptName) {
        this.specimenDeptName = specimenDeptName;
    }

    public String getSurgeryStatusCode() {
        return surgeryStatusCode;
    }

    public void setSurgeryStatusCode(String surgeryStatusCode) {
        this.surgeryStatusCode = surgeryStatusCode;
    }

    public String getSurgeryStatusName() {
        return surgeryStatusName;
    }

    public void setSurgeryStatusName(String surgeryStatusName) {
        this.surgeryStatusName = surgeryStatusName;
    }

    public String getDocumentNo() {
        return documentNo;
    }

    public void setDocumentNo(String documentNo) {
        this.documentNo = documentNo;
    }

    public String getDocumentContentPdf() {
        return documentContentPdf;
    }

    public void setDocumentContentPdf(String documentContentPdf) {
        this.documentContentPdf = documentContentPdf;
    }

    public String getDocumentCmmt() {
        return documentCmmt;
    }

    public void setDocumentCmmt(String documentCmmt) {
        this.documentCmmt = documentCmmt;
    }

    public String getEnterOperaId() {
        return enterOperaId;
    }

    public void setEnterOperaId(String enterOperaId) {
        this.enterOperaId = enterOperaId;
    }

    public String getEnterOperaName() {
        return enterOperaName;
    }

    public void setEnterOperaName(String enterOperaName) {
        this.enterOperaName = enterOperaName;
    }

    public String getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(String enterDateTime) {
        this.enterDateTime = enterDateTime;
    }

    public String getSignOperaId() {
        return signOperaId;
    }

    public void setSignOperaId(String signOperaId) {
        this.signOperaId = signOperaId;
    }

    public String getSignOperaName() {
        return signOperaName;
    }

    public void setSignOperaName(String signOperaName) {
        this.signOperaName = signOperaName;
    }

    public String getSignDateTime() {
        return signDateTime;
    }

    public void setSignDateTime(String signDateTime) {
        this.signDateTime = signDateTime;
    }

    public String getReviewOperaId() {
        return reviewOperaId;
    }

    public void setReviewOperaId(String reviewOperaId) {
        this.reviewOperaId = reviewOperaId;
    }

    public String getReviewOperaName() {
        return reviewOperaName;
    }

    public void setReviewOperaName(String reviewOperaName) {
        this.reviewOperaName = reviewOperaName;
    }

    public String getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(String reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
    }

    public String getModifyOperaId() {
        return modifyOperaId;
    }

    public void setModifyOperaId(String modifyOperaId) {
        this.modifyOperaId = modifyOperaId;
    }

    public String getModifyOperaName() {
        return modifyOperaName;
    }

    public void setModifyOperaName(String modifyOperaName) {
        this.modifyOperaName = modifyOperaName;
    }

    public String getModifyDateTime() {
        return modifyDateTime;
    }

    public void setModifyDateTime(String modifyDateTime) {
        this.modifyDateTime = modifyDateTime;
    }
}
