package com.zebone.nhis.webservice.pskq.model;


import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * COST_DETAIL_OUTPAT
 */
public class OpSettlement {


    @MetadataDescribe(id= "LHDE0049001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0049002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0049003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0049004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0049005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0049006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0049007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0049008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0049009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0049010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0049011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0049012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0049013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0049014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0049015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0049016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0049017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0049018",name = "病人类别代码",eName = "PATIENT_TYPE_CODE")
    private String patientTypeCode;

    @MetadataDescribe(id= "LHDE0049019",name = "病人类别名称",eName = "PATIENT_TYPE_NAME")
    private String patientTypeName;

    @MetadataDescribe(id= "LHDE0049020",name = "费用明细ID",eName = "COST_DETAIL_ID")
    private String costDetailId;

    @MetadataDescribe(id= "LHDE0049021",name = "结算发票明细ID",eName = "SETTLEMENT_DETAIL_ID")
    private String ettlementDetailId;

    @MetadataDescribe(id= "LHDE0049022",name = "结算类型代码",eName = "ACCOUNT_TYPE_CODE")
    private String accountTypeCode;

    @MetadataDescribe(id= "LHDE0049023",name = "结算类型名称",eName = "ACCOUNT_TYPE_NAME")
    private String accountTypeName;

    @MetadataDescribe(id= "LHDE0049024",name = "收费项目代码",eName = "CHARGE_ITEM_CODE")
    private String chargeItemCode;

    @MetadataDescribe(id= "LHDE0049025",name = "收费项目名称",eName = "CHARGE_ITEM_NAME")
    private String chargeItemName;

    @MetadataDescribe(id= "LHDE0049026",name = "收费项目单价",eName = "CHARGE_ITEM_PRICE")
    private String chargeItemPrice;

    @MetadataDescribe(id= "LHDE0049027",name = "收费项目原价",eName = "CHARGE_ITEM_ORIG_PRICE")
    private String chargeItemOrigPrice;

    @MetadataDescribe(id= "LHDE0049028",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;

    @MetadataDescribe(id= "LHDE0049029",name = "草药副数",eName = "HERBAL_QUANTITY")
    private String herbalQuantity;

    @MetadataDescribe(id= "LHDE0049030",name = "收费总额",eName = "CHARGE_TOTAL")
    private String chargeTotal;

    @MetadataDescribe(id= "LHDE0049031",name = "收费原价总额",eName = "ORIG_CHARGE_TOTAL")
    private String origChargeTotal;

    @MetadataDescribe(id= "LHDE0049032",name = "录入日期时间",eName = "INPUT_DATE_TIME")
    private String inputDateTime;


    @MetadataDescribe(id= "LHDE0049033",name = "录入医生ID",eName = "INPUT_DOCTOR_ID")
    private String inputDoctorId;

    @MetadataDescribe(id= "LHDE0049034",name = "录入医生姓名",eName = "INPUT_DOCTOR_NAME")
    private String inputDoctorName;

    @MetadataDescribe(id= "LHDE0049035",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0049036",name = "开立医生ID",eName = "ENTER_DOCTOR_ID")
    private String enterDoctorId;

    @MetadataDescribe(id= "LHDE0049037",name = "开立医生姓名",eName = "ENTER_DOCTOR_NAME")
    private String enterDoctorName;

    @MetadataDescribe(id= "LHDE0049038",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0049039",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0049040",name = "开立医生核算科室ID",eName = "ACCOUNT_DEPT_ID")
    private String accountDeptId;

    @MetadataDescribe(id= "LHDE0049041",name = "开立医生核算科室名称",eName = "ACCOUNT_DEPT_NAME")
    private String accountDeptName;

    @MetadataDescribe(id= "LHDE0049042",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0049043",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0049044",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0049045",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0049046",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;

    @MetadataDescribe(id= "LHDE0049047",name = "执行窗口号",eName = "EXEC_WIN_NO")
    private String execWinNo;

    @MetadataDescribe(id= "LHDE0049048",name = "执行设备号",eName = "EXEC_EQUIPMENT_NO")
    private String execEquipmentNo;

    @MetadataDescribe(id= "LHDE0049049",name = "库房代码",eName = "STORAGE_CODE")
    private String storageCode;

    @MetadataDescribe(id= "LHDE0049050",name = "库房名称",eName = "STORAGE_NAME")
    private String StorageName;

    @MetadataDescribe(id= "LHDE0049051",name = "费用状态",eName = "CHARGE_STATUS")
    private String chargeStatus;

    @MetadataDescribe(id= "LHDE0049052",name = "费用分类代码",eName = "CHARGE_ITEM_CLASS_CODE")
    private String chargeItemClassCode;

    @MetadataDescribe(id= "LHDE0049053",name = "费用分类名称",eName = "CHARGE_ITEM_CLASS_NAME")
    private String chargeItemClassName;

    @MetadataDescribe(id= "LHDE0049054",name = "医保类型代码",eName = "ITEM_MI_TYPE_CODE")
    private String itemMiTypeCode;

    @MetadataDescribe(id= "LHDE0049055",name = "医保类型名称",eName = "ITEM_MI_TYPE_NAME")
    private String itemMiTypeName;

    @MetadataDescribe(id= "LHDE0049056",name = "发票分类代码",eName = "RCPT_CLASS_CODE")
    private String rcptClassCode;

    @MetadataDescribe(id= "LHDE0049057",name = "发票分类名称",eName = "RCPT_CLASS_NAME")
    private String rcptClassName;

    @MetadataDescribe(id= "LHDE0049058",name = "核算分类代码",eName = "AUDIT_CLASS_CODE")
    private String auditClassCode;

    @MetadataDescribe(id= "LHDE0049059",name = "核算分类名称",eName = "AUDIT_CLASS_NAME")
    private String auditClassName;

    @MetadataDescribe(id= "LHDE0049060",name = "财务分类代码",eName = "ACCOUNT_CLASS_CODE")
    private String accountClassCode;

    @MetadataDescribe(id= "LHDE0049061",name = "财务分类名称",eName = "ACCOUNT_CLASS_NAME")
    private String accountClassName;

    @MetadataDescribe(id= "LHDE0049062",name = "病案费用分类代码",eName = "MR_CLASS_CODE")
    private String mrClassCode;

    @MetadataDescribe(id= "LHDE0049063",name = "病案费用分类名称",eName = "MR_CLASS_NAME")
    private String mrClassName;

    @MetadataDescribe(id= "LHDE0049064",name = "高质耗材标志",eName = "HQ_MAT_FLAG")
    private String hqMatFlag;

    @MetadataDescribe(id= "LHDE0049065",name = "辅助用药标志",eName = "ASSIST_DRUG_FLAG")
    private String assistDrugFlag;

    @MetadataDescribe(id= "LHDE0049066",name = "特殊药品类型代码",eName = "SPECIAL_DRUG_FLAG")
    private String specialDrugFlag;

    @MetadataDescribe(id= "LHDE0049067",name = "结账日期时间",eName = "ACCOUNT_DATE_TIME")
    private String accountDateTime;

    @MetadataDescribe(id= "LHDE0049068",name = "记账日期时间",eName = "CHARGE_DATE_TIME")
    private String chargeDateTime;

    @MetadataDescribe(id= "LHDE0049069",name = "记账人ID",eName = "CHARGE_OPERA_ID")
    private String chargeOperaId;

    @MetadataDescribe(id= "LHDE0049070",name = "记账人姓名",eName = "CHARGE_OPERA_NAME")
    private String chargeOperaName;

    @MetadataDescribe(id= "LHDE0049071",name = "结算单号",eName = "SETTLEMENT_NO")
    private String settlementNo;

    @MetadataDescribe(id= "LHDE0049072",name = "结算次数",eName = "SETTLEMENT_TIMES")
    private String settlementTimes;

    @MetadataDescribe(id= "LHDE0049073",name = "结算日期时间",eName = "SETTLEMENT_DATE_TIME")
    private String settlementDateTime;

    @MetadataDescribe(id= "LHDE0049074",name = "结算人ID",eName = "SETTLEMENT_OPERA_ID")
    private String settlementOperaId;

    @MetadataDescribe(id= "LHDE0049075",name = "结算人姓名",eName = "SETTLEMENT_OPERA_NAME")
    private String settlementOperaName;

    @MetadataDescribe(id= "LHDE0049076",name = "医保限额",eName = "MI_LIMIT_FEE")
    private String miLimitFee;

    @MetadataDescribe(id= "LHDE0049077",name = "医保内金额",eName = "IN_MI_FEE")
    private String inMiFee;

    @MetadataDescribe(id= "LHDE0049078",name = "医保外金额",eName = "OUT_MI_FEE")
    private String outMiFee;

    @MetadataDescribe(id= "LHDE0049079",name = "个人支付费用",eName = "SELF_PAYMENT_FEE")
    private String selfPaymentFee;

    @MetadataDescribe(id= "LHDE0049080",name = "医保分解状态",eName = "MI_STATUS")
    private String miStatus;

    @MetadataDescribe(id= "LHDE0049081",name = "医保费用分类代码",eName = "MI_CLASS_CODE")
    private String miClassCode;

    @MetadataDescribe(id= "LHDE0049082",name = "医保费用分类名称",eName = "MI_CLASS_NAME")
    private String miClassName;

    @MetadataDescribe(id= "LHDE0049083",name = "社区优惠金额",eName = "PREFERENTIAL_FEE")
    private String preferentialFee;

    @MetadataDescribe(id= "LHDE0049084",name = "社区优惠比例",eName = "PREFERENTIAL_RATE")
    private String preferentialRate;

    @MetadataDescribe(id= "LHDE0049085",name = "子系统",eName = "SUB_SYSTEM_CODE")
    private String subSystemCode;

    @MetadataDescribe(id= "LHDE0049086",name = "费用来源",eName = "INPUT_SOURCE_CODE")
    private String inputSourceCode;

    @MetadataDescribe(id= "LHDE0049087",name = "医嘱ID",eName = "ORDER_ID")
    private String orderId;

    @MetadataDescribe(id= "LHDE0049088",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String sourceSystemCode;


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

    public String getCostDetailId() {
        return costDetailId;
    }

    public void setCostDetailId(String costDetailId) {
        this.costDetailId = costDetailId;
    }

    public String getEttlementDetailId() {
        return ettlementDetailId;
    }

    public void setEttlementDetailId(String ettlementDetailId) {
        this.ettlementDetailId = ettlementDetailId;
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

    public String getChargeItemCode() {
        return chargeItemCode;
    }

    public void setChargeItemCode(String chargeItemCode) {
        this.chargeItemCode = chargeItemCode;
    }

    public String getChargeItemName() {
        return chargeItemName;
    }

    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    public String getChargeItemPrice() {
        return chargeItemPrice;
    }

    public void setChargeItemPrice(String chargeItemPrice) {
        this.chargeItemPrice = chargeItemPrice;
    }

    public String getChargeItemOrigPrice() {
        return chargeItemOrigPrice;
    }

    public void setChargeItemOrigPrice(String chargeItemOrigPrice) {
        this.chargeItemOrigPrice = chargeItemOrigPrice;
    }

    public String getChargeQuantity() {
        return chargeQuantity;
    }

    public void setChargeQuantity(String chargeQuantity) {
        this.chargeQuantity = chargeQuantity;
    }

    public String getHerbalQuantity() {
        return herbalQuantity;
    }

    public void setHerbalQuantity(String herbalQuantity) {
        this.herbalQuantity = herbalQuantity;
    }

    public String getChargeTotal() {
        return chargeTotal;
    }

    public void setChargeTotal(String chargeTotal) {
        this.chargeTotal = chargeTotal;
    }

    public String getOrigChargeTotal() {
        return origChargeTotal;
    }

    public void setOrigChargeTotal(String origChargeTotal) {
        this.origChargeTotal = origChargeTotal;
    }

    public String getInputDateTime() {
        return inputDateTime;
    }

    public void setInputDateTime(String inputDateTime) {
        this.inputDateTime = inputDateTime;
    }

    public String getInputDoctorId() {
        return inputDoctorId;
    }

    public void setInputDoctorId(String inputDoctorId) {
        this.inputDoctorId = inputDoctorId;
    }

    public String getInputDoctorName() {
        return inputDoctorName;
    }

    public void setInputDoctorName(String inputDoctorName) {
        this.inputDoctorName = inputDoctorName;
    }

    public String getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(String enterDateTime) {
        this.enterDateTime = enterDateTime;
    }

    public String getEnterDoctorId() {
        return enterDoctorId;
    }

    public void setEnterDoctorId(String enterDoctorId) {
        this.enterDoctorId = enterDoctorId;
    }

    public String getEnterDoctorName() {
        return enterDoctorName;
    }

    public void setEnterDoctorName(String enterDoctorName) {
        this.enterDoctorName = enterDoctorName;
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

    public String getAccountDeptId() {
        return accountDeptId;
    }

    public void setAccountDeptId(String accountDeptId) {
        this.accountDeptId = accountDeptId;
    }

    public String getAccountDeptName() {
        return accountDeptName;
    }

    public void setAccountDeptName(String accountDeptName) {
        this.accountDeptName = accountDeptName;
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

    public String getExecWinNo() {
        return execWinNo;
    }

    public void setExecWinNo(String execWinNo) {
        this.execWinNo = execWinNo;
    }

    public String getExecEquipmentNo() {
        return execEquipmentNo;
    }

    public void setExecEquipmentNo(String execEquipmentNo) {
        this.execEquipmentNo = execEquipmentNo;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageName() {
        return StorageName;
    }

    public void setStorageName(String storageName) {
        StorageName = storageName;
    }

    public String getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(String chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public String getChargeItemClassCode() {
        return chargeItemClassCode;
    }

    public void setChargeItemClassCode(String chargeItemClassCode) {
        this.chargeItemClassCode = chargeItemClassCode;
    }

    public String getChargeItemClassName() {
        return chargeItemClassName;
    }

    public void setChargeItemClassName(String chargeItemClassName) {
        this.chargeItemClassName = chargeItemClassName;
    }

    public String getItemMiTypeCode() {
        return itemMiTypeCode;
    }

    public void setItemMiTypeCode(String itemMiTypeCode) {
        this.itemMiTypeCode = itemMiTypeCode;
    }

    public String getItemMiTypeName() {
        return itemMiTypeName;
    }

    public void setItemMiTypeName(String itemMiTypeName) {
        this.itemMiTypeName = itemMiTypeName;
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

    public String getAuditClassCode() {
        return auditClassCode;
    }

    public void setAuditClassCode(String auditClassCode) {
        this.auditClassCode = auditClassCode;
    }

    public String getAuditClassName() {
        return auditClassName;
    }

    public void setAuditClassName(String auditClassName) {
        this.auditClassName = auditClassName;
    }

    public String getAccountClassCode() {
        return accountClassCode;
    }

    public void setAccountClassCode(String accountClassCode) {
        this.accountClassCode = accountClassCode;
    }

    public String getAccountClassName() {
        return accountClassName;
    }

    public void setAccountClassName(String accountClassName) {
        this.accountClassName = accountClassName;
    }

    public String getMrClassCode() {
        return mrClassCode;
    }

    public void setMrClassCode(String mrClassCode) {
        this.mrClassCode = mrClassCode;
    }

    public String getMrClassName() {
        return mrClassName;
    }

    public void setMrClassName(String mrClassName) {
        this.mrClassName = mrClassName;
    }

    public String getHqMatFlag() {
        return hqMatFlag;
    }

    public void setHqMatFlag(String hqMatFlag) {
        this.hqMatFlag = hqMatFlag;
    }

    public String getAssistDrugFlag() {
        return assistDrugFlag;
    }

    public void setAssistDrugFlag(String assistDrugFlag) {
        this.assistDrugFlag = assistDrugFlag;
    }

    public String getSpecialDrugFlag() {
        return specialDrugFlag;
    }

    public void setSpecialDrugFlag(String specialDrugFlag) {
        this.specialDrugFlag = specialDrugFlag;
    }

    public String getAccountDateTime() {
        return accountDateTime;
    }

    public void setAccountDateTime(String accountDateTime) {
        this.accountDateTime = accountDateTime;
    }

    public String getChargeDateTime() {
        return chargeDateTime;
    }

    public void setChargeDateTime(String chargeDateTime) {
        this.chargeDateTime = chargeDateTime;
    }

    public String getChargeOperaId() {
        return chargeOperaId;
    }

    public void setChargeOperaId(String chargeOperaId) {
        this.chargeOperaId = chargeOperaId;
    }

    public String getChargeOperaName() {
        return chargeOperaName;
    }

    public void setChargeOperaName(String chargeOperaName) {
        this.chargeOperaName = chargeOperaName;
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

    public String getMiLimitFee() {
        return miLimitFee;
    }

    public void setMiLimitFee(String miLimitFee) {
        this.miLimitFee = miLimitFee;
    }

    public String getInMiFee() {
        return inMiFee;
    }

    public void setInMiFee(String inMiFee) {
        this.inMiFee = inMiFee;
    }

    public String getOutMiFee() {
        return outMiFee;
    }

    public void setOutMiFee(String outMiFee) {
        this.outMiFee = outMiFee;
    }

    public String getSelfPaymentFee() {
        return selfPaymentFee;
    }

    public void setSelfPaymentFee(String selfPaymentFee) {
        this.selfPaymentFee = selfPaymentFee;
    }

    public String getMiStatus() {
        return miStatus;
    }

    public void setMiStatus(String miStatus) {
        this.miStatus = miStatus;
    }

    public String getMiClassCode() {
        return miClassCode;
    }

    public void setMiClassCode(String miClassCode) {
        this.miClassCode = miClassCode;
    }

    public String getMiClassName() {
        return miClassName;
    }

    public void setMiClassName(String miClassName) {
        this.miClassName = miClassName;
    }

    public String getPreferentialFee() {
        return preferentialFee;
    }

    public void setPreferentialFee(String preferentialFee) {
        this.preferentialFee = preferentialFee;
    }

    public String getPreferentialRate() {
        return preferentialRate;
    }

    public void setPreferentialRate(String preferentialRate) {
        this.preferentialRate = preferentialRate;
    }

    public String getSubSystemCode() {
        return subSystemCode;
    }

    public void setSubSystemCode(String subSystemCode) {
        this.subSystemCode = subSystemCode;
    }

    public String getInputSourceCode() {
        return inputSourceCode;
    }

    public void setInputSourceCode(String inputSourceCode) {
        this.inputSourceCode = inputSourceCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }
}
