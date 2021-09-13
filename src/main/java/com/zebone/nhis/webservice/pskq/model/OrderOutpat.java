package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 门诊医嘱
 */
public class OrderOutpat {

    @MetadataDescribe(id= "LHDE0032001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0032002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0032003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0032004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0032005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0032006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0032007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0032008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0032009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0032010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0032011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0032012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0032013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0032014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0032015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0032016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0032017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0032018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0032019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0032020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0032021",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;

    @MetadataDescribe(id= "LHDE0032022",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;

    @MetadataDescribe(id= "LHDE0032023",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;

    @MetadataDescribe(id= "LHDE0032024",name = "医嘱ID",eName = "ORDER_ID")
    private String orderId;

    @MetadataDescribe(id= "LHDE0032025",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    @MetadataDescribe(id= "LHDE0032026",name = "医嘱组号",eName = "ORDER_GROUP_NO")
    private String orderGroupNo;

    @MetadataDescribe(id= "LHDE0032027",name = "医嘱项目排序号",eName = "ORDER_ITEM_SEQ_NO")
    private String orderItemSeqNo;

    @MetadataDescribe(id= "LHDE0032028",name = "父医嘱标志",eName = "PARENT_FLAG")
    private String parentFlag;

    @MetadataDescribe(id= "LHDE0032029",name = "医嘱分类代码",eName = "ORDER_CATEGORY_CODE")
    private String orderCategoryCode;

    @MetadataDescribe(id= "LHDE0032030",name = "医嘱分类名称",eName = "ORDER_CATEGORY_NAME")
    private String orderCategoryName;

    @MetadataDescribe(id= "LHDE0032031",name = "医嘱项目代码",eName = "ORDER_ITEM_CODE")
    private String orderItemCode;

    @MetadataDescribe(id= "LHDE0032032",name = "医嘱项目名称",eName = "ORDER_ITEM_NAME")
    private String orderItemName;

    @MetadataDescribe(id= "LHDE0032033",name = "医嘱描述",eName = "ORDER_DESC")
    private String orderDesc;

    @MetadataDescribe(id= "LHDE0032034",name = "医嘱嘱托",eName = "ORDER_ENTRUST")
    private String orderEntrust;

    @MetadataDescribe(id= "LHDE0032035",name = "处方号码",eName = "PRES_NO")
    private String presNo;

    @MetadataDescribe(id= "LHDE0032036",name = "处方药品组号",eName = "PRES_GROUP_NO")
    private String presGroupNo;

    @MetadataDescribe(id= "LHDE0032037",name = "处方类型代码",eName = "PRES_TYPE_CODE")
    private String presTypeCode;

    @MetadataDescribe(id= "LHDE0032038",name = "处方类型名称",eName = "PRES_TYPE_NAME")
    private String presTypeName;

    @MetadataDescribe(id= "LHDE0032039",name = "处方有效天数",eName = "PRES_SUSTAINED_DAYS")
    private String presSustainedDays;

    @MetadataDescribe(id= "LHDE0032040",name = "处方药品金额",eName = "PRES_DRUG_AMOUNT")
    private String presDrugAmount;

    @MetadataDescribe(id= "LHDE0032041",name = "药物单次剂量",eName = "DOSE")
    private String dose;

    @MetadataDescribe(id= "LHDE0032042",name = "剂量单位代码",eName = "DOSE_UNIT_CODE")
    private String doseUnitCode;

    @MetadataDescribe(id= "LHDE0032043",name = "剂量单位名称",eName = "DOSE_UNIT_NAME")
    private String doseUnitName;

    @MetadataDescribe(id= "LHDE0032044",name = "药物剂型代码",eName = "DRUG_FORM_CODE")
    private String drugFormCode;

    @MetadataDescribe(id= "LHDE0032045",name = "药物剂型名称",eName = "DRUG_FORM_NAME")
    private String drugFormName;

    @MetadataDescribe(id= "LHDE0032046",name = "药物规格",eName = "DRUG_SPEC")
    private String drugSpec;

    @MetadataDescribe(id= "LHDE0032047",name = "药物总剂量",eName = "DRUG_TOTAL_DOSE")
    private String drugTotalDose;

    @MetadataDescribe(id= "LHDE0032048",name = "草药副数",eName = "HERBAL_QUANTITY")
    private String herbalQuantity;

    @MetadataDescribe(id= "LHDE0032049",name = "草药用药方法",eName = "CHM_USAGE")
    private String chmUsage;

    @MetadataDescribe(id= "LHDE0032050",name = "用量",eName = "USE_QUANTITY")
    private String useQuantity;

    @MetadataDescribe(id= "LHDE0032051",name = "用量单位代码",eName = "USE_UNIT_CODE")
    private String useUnitCode;

    @MetadataDescribe(id= "LHDE0032052",name = "用量单位名称",eName = "USE_UNIT_NAME")
    private String useUnitName;

    @MetadataDescribe(id= "LHDE0032053",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;

    @MetadataDescribe(id= "LHDE0032054",name = "库房代码",eName = "STORAGE_CODE")
    private String storageCode;

    @MetadataDescribe(id= "LHDE0032055",name = "库房名称",eName = "STORAGE_NAME")
    private String storageName;

    @MetadataDescribe(id= "LHDE0032056",name = "药物频率代码",eName = "DRUG_FREQ_CODE")
    private String drugFreqCode;

    @MetadataDescribe(id= "LHDE0032057",name = "药物频率名称",eName = "DRUG_FREQ_NAME")
    private String drugFreqName;

    @MetadataDescribe(id= "LHDE0032058",name = "用药途径代码",eName = "USAGE_CODE")
    private String usageCode;

    @MetadataDescribe(id= "LHDE0032059",name = "用药途径名称",eName = "USAGE_NAME")
    private String usageName;


    @MetadataDescribe(id= "LHDE0032060",name = "执行次数",eName = "EXEC_TIMES")
    private String execTimes;


    @MetadataDescribe(id= "LHDE0032061",name = "执行天数",eName = "EXEC_DAYS")
    private String execDays;

    @MetadataDescribe(id= "LHDE0032062",name = "滴速",eName = "DROP_SPEED")
    private String dropSpeed;

    @MetadataDescribe(id= "LHDE0032063",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0032064",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0032065",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0032066",name = "开立医师ID",eName = "ORDER_DOCTOR_ID")
    private String orderDoctorId;

    @MetadataDescribe(id= "LHDE0032067",name = "开立医师姓名",eName = "ORDER_DOCTOR_NAME")
    private String orderDoctorName;

    @MetadataDescribe(id= "LHDE0032068",name = "收费日期时间",eName = "PRICE_DATE_TIME")
    private String priceDateTime;

    @MetadataDescribe(id= "LHDE0032069",name = "收费人ID",eName = "PRICE_OPERA_ID")
    private String priceOperaId;

    @MetadataDescribe(id= "LHDE0032070",name = "收费人姓名",eName = "PRICE_OPERA_NAME")
    private String priceOperaName;

    @MetadataDescribe(id= "LHDE0032071",name = "审核日期时间",eName = "CHECK_DATE_TIME")
    private String checkDateTime;

    @MetadataDescribe(id= "LHDE0032072",name = "审核人ID",eName = "CHECK_OPERA_ID")
    private String checkOperaId;

    @MetadataDescribe(id= "LHDE0032073",name = "审核人姓名",eName = "CHECK_OPERA_NAME")
    private String checkOperaName;

    @MetadataDescribe(id= "LHDE0032074",name = "配药日期时间",eName = "FILL_DATE_TIME")
    private String fillDateTime;

    @MetadataDescribe(id= "LHDE0032075",name = "配药医师ID",eName = "FILL_OPERA_ID")
    private String fillOperaId;

    @MetadataDescribe(id= "LHDE0032076",name = "配药医师姓名",eName = "FILL_OPERA_NAME")
    private String fillOperaName;

    @MetadataDescribe(id= "LHDE0032077",name = "发药日期时间",eName = "DISPENSE_DATE_TIME")
    private String dispenseDateTime;

    @MetadataDescribe(id= "LHDE0032078",name = "发药医师ID",eName = "DISPENSE_OPERA_ID")
    private String dispenseOperaId;

    @MetadataDescribe(id= "LHDE0032079",name = "发药医师姓名",eName = "DISPENSE_OPERA_NAME")
    private String dispenseOperaName;

    @MetadataDescribe(id= "LHDE0032080",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;

    @MetadataDescribe(id= "LHDE0032081",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0032082",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0032083",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0032084",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0032085",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0032086",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0032087",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0032088",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0032089",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;

    @MetadataDescribe(id= "LHDE0032090",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;

    @MetadataDescribe(id= "LHDE0032091",name = "手术标志",eName = "SURG_FLAG")
    private String surgFlag;

    @MetadataDescribe(id= "LHDE0032092",name = "皮试标志",eName = "SKIN_TEST_FLAG")
    private String skinTestFlag;

    @MetadataDescribe(id= "LHDE0032093",name = "适应症标志",eName = "INDICATION_FLAG")
    private String indicationFlag;

    @MetadataDescribe(id= "LHDE0032094",name = "医嘱状态代码",eName = "ORDER_STATUS_CODE")
    private String orderStatusCode;

    @MetadataDescribe(id= "LHDE0032095",name = "医嘱状态名称",eName = "ORDER_STATUS_NAME")
    private String orderStatusName;

    @MetadataDescribe(id= "LHDE0032096",name = "医嘱规格",eName = "ORDER_ITEM_SPEC")
    private String orderItemSpec;

    @MetadataDescribe(id= "LHDE0032097",name = "医保卡号",eName = "MED_INSURANCE_NO")
    private String medInsuranceNo;

    @MetadataDescribe(id= "LHDE0032098",name = "是否医保",eName = "IS_INSURANCE")
    private String isInsurance;

    public String getMedInsuranceNo() {
        return medInsuranceNo;
    }

    public void setMedInsuranceNo(String medInsuranceNo) {
        this.medInsuranceNo = medInsuranceNo;
    }

    public String getIsInsurance() {
        return isInsurance;
    }

    public void setIsInsurance(String isInsurance) {
        this.isInsurance = isInsurance;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPlacerOrderNo() {
        return placerOrderNo;
    }

    public void setPlacerOrderNo(String placerOrderNo) {
        this.placerOrderNo = placerOrderNo;
    }

    public String getOrderGroupNo() {
        return orderGroupNo;
    }

    public void setOrderGroupNo(String orderGroupNo) {
        this.orderGroupNo = orderGroupNo;
    }

    public String getOrderItemSeqNo() {
        return orderItemSeqNo;
    }

    public void setOrderItemSeqNo(String orderItemSeqNo) {
        this.orderItemSeqNo = orderItemSeqNo;
    }

    public String getParentFlag() {
        return parentFlag;
    }

    public void setParentFlag(String parentFlag) {
        this.parentFlag = parentFlag;
    }

    public String getOrderCategoryCode() {
        return orderCategoryCode;
    }

    public void setOrderCategoryCode(String orderCategoryCode) {
        this.orderCategoryCode = orderCategoryCode;
    }

    public String getOrderCategoryName() {
        return orderCategoryName;
    }

    public void setOrderCategoryName(String orderCategoryName) {
        this.orderCategoryName = orderCategoryName;
    }

    public String getOrderItemCode() {
        return orderItemCode;
    }

    public void setOrderItemCode(String orderItemCode) {
        this.orderItemCode = orderItemCode;
    }

    public String getOrderItemName() {
        return orderItemName;
    }

    public void setOrderItemName(String orderItemName) {
        this.orderItemName = orderItemName;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderEntrust() {
        return orderEntrust;
    }

    public void setOrderEntrust(String orderEntrust) {
        this.orderEntrust = orderEntrust;
    }

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getPresGroupNo() {
        return presGroupNo;
    }

    public void setPresGroupNo(String presGroupNo) {
        this.presGroupNo = presGroupNo;
    }

    public String getPresTypeCode() {
        return presTypeCode;
    }

    public void setPresTypeCode(String presTypeCode) {
        this.presTypeCode = presTypeCode;
    }

    public String getPresTypeName() {
        return presTypeName;
    }

    public void setPresTypeName(String presTypeName) {
        this.presTypeName = presTypeName;
    }

    public String getPresSustainedDays() {
        return presSustainedDays;
    }

    public void setPresSustainedDays(String presSustainedDays) {
        this.presSustainedDays = presSustainedDays;
    }

    public String getPresDrugAmount() {
        return presDrugAmount;
    }

    public void setPresDrugAmount(String presDrugAmount) {
        this.presDrugAmount = presDrugAmount;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getDoseUnitCode() {
        return doseUnitCode;
    }

    public void setDoseUnitCode(String doseUnitCode) {
        this.doseUnitCode = doseUnitCode;
    }

    public String getDoseUnitName() {
        return doseUnitName;
    }

    public void setDoseUnitName(String doseUnitName) {
        this.doseUnitName = doseUnitName;
    }

    public String getDrugFormCode() {
        return drugFormCode;
    }

    public void setDrugFormCode(String drugFormCode) {
        this.drugFormCode = drugFormCode;
    }

    public String getDrugFormName() {
        return drugFormName;
    }

    public void setDrugFormName(String drugFormName) {
        this.drugFormName = drugFormName;
    }

    public String getDrugSpec() {
        return drugSpec;
    }

    public void setDrugSpec(String drugSpec) {
        this.drugSpec = drugSpec;
    }

    public String getDrugTotalDose() {
        return drugTotalDose;
    }

    public void setDrugTotalDose(String drugTotalDose) {
        this.drugTotalDose = drugTotalDose;
    }

    public String getHerbalQuantity() {
        return herbalQuantity;
    }

    public void setHerbalQuantity(String herbalQuantity) {
        this.herbalQuantity = herbalQuantity;
    }

    public String getChmUsage() {
        return chmUsage;
    }

    public void setChmUsage(String chmUsage) {
        this.chmUsage = chmUsage;
    }

    public String getUseQuantity() {
        return useQuantity;
    }

    public void setUseQuantity(String useQuantity) {
        this.useQuantity = useQuantity;
    }

    public String getUseUnitCode() {
        return useUnitCode;
    }

    public void setUseUnitCode(String useUnitCode) {
        this.useUnitCode = useUnitCode;
    }

    public String getUseUnitName() {
        return useUnitName;
    }

    public void setUseUnitName(String useUnitName) {
        this.useUnitName = useUnitName;
    }

    public String getChargeQuantity() {
        return chargeQuantity;
    }

    public void setChargeQuantity(String chargeQuantity) {
        this.chargeQuantity = chargeQuantity;
    }

    public String getStorageCode() {
        return storageCode;
    }

    public void setStorageCode(String storageCode) {
        this.storageCode = storageCode;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getDrugFreqCode() {
        return drugFreqCode;
    }

    public void setDrugFreqCode(String drugFreqCode) {
        this.drugFreqCode = drugFreqCode;
    }

    public String getDrugFreqName() {
        return drugFreqName;
    }

    public void setDrugFreqName(String drugFreqName) {
        this.drugFreqName = drugFreqName;
    }

    public String getUsageCode() {
        return usageCode;
    }

    public void setUsageCode(String usageCode) {
        this.usageCode = usageCode;
    }

    public String getUsageName() {
        return usageName;
    }

    public void setUsageName(String usageName) {
        this.usageName = usageName;
    }

    public String getExecTimes() {
        return execTimes;
    }

    public void setExecTimes(String execTimes) {
        this.execTimes = execTimes;
    }

    public String getExecDays() {
        return execDays;
    }

    public void setExecDays(String execDays) {
        this.execDays = execDays;
    }

    public String getDropSpeed() {
        return dropSpeed;
    }

    public void setDropSpeed(String dropSpeed) {
        this.dropSpeed = dropSpeed;
    }

    public String getEnterDateTime() {
        return enterDateTime;
    }

    public void setEnterDateTime(String enterDateTime) {
        this.enterDateTime = enterDateTime;
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

    public String getOrderDoctorId() {
        return orderDoctorId;
    }

    public void setOrderDoctorId(String orderDoctorId) {
        this.orderDoctorId = orderDoctorId;
    }

    public String getOrderDoctorName() {
        return orderDoctorName;
    }

    public void setOrderDoctorName(String orderDoctorName) {
        this.orderDoctorName = orderDoctorName;
    }

    public String getPriceDateTime() {
        return priceDateTime;
    }

    public void setPriceDateTime(String priceDateTime) {
        this.priceDateTime = priceDateTime;
    }

    public String getPriceOperaId() {
        return priceOperaId;
    }

    public void setPriceOperaId(String priceOperaId) {
        this.priceOperaId = priceOperaId;
    }

    public String getPriceOperaName() {
        return priceOperaName;
    }

    public void setPriceOperaName(String priceOperaName) {
        this.priceOperaName = priceOperaName;
    }

    public String getCheckDateTime() {
        return checkDateTime;
    }

    public void setCheckDateTime(String checkDateTime) {
        this.checkDateTime = checkDateTime;
    }

    public String getCheckOperaId() {
        return checkOperaId;
    }

    public void setCheckOperaId(String checkOperaId) {
        this.checkOperaId = checkOperaId;
    }

    public String getCheckOperaName() {
        return checkOperaName;
    }

    public void setCheckOperaName(String checkOperaName) {
        this.checkOperaName = checkOperaName;
    }

    public String getFillDateTime() {
        return fillDateTime;
    }

    public void setFillDateTime(String fillDateTime) {
        this.fillDateTime = fillDateTime;
    }

    public String getFillOperaId() {
        return fillOperaId;
    }

    public void setFillOperaId(String fillOperaId) {
        this.fillOperaId = fillOperaId;
    }

    public String getFillOperaName() {
        return fillOperaName;
    }

    public void setFillOperaName(String fillOperaName) {
        this.fillOperaName = fillOperaName;
    }

    public String getDispenseDateTime() {
        return dispenseDateTime;
    }

    public void setDispenseDateTime(String dispenseDateTime) {
        this.dispenseDateTime = dispenseDateTime;
    }

    public String getDispenseOperaId() {
        return dispenseOperaId;
    }

    public void setDispenseOperaId(String dispenseOperaId) {
        this.dispenseOperaId = dispenseOperaId;
    }

    public String getDispenseOperaName() {
        return dispenseOperaName;
    }

    public void setDispenseOperaName(String dispenseOperaName) {
        this.dispenseOperaName = dispenseOperaName;
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

    public String getExecDateTime() {
        return execDateTime;
    }

    public void setExecDateTime(String execDateTime) {
        this.execDateTime = execDateTime;
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

    public String getEmerFlag() {
        return emerFlag;
    }

    public void setEmerFlag(String emerFlag) {
        this.emerFlag = emerFlag;
    }

    public String getSurgFlag() {
        return surgFlag;
    }

    public void setSurgFlag(String surgFlag) {
        this.surgFlag = surgFlag;
    }

    public String getSkinTestFlag() {
        return skinTestFlag;
    }

    public void setSkinTestFlag(String skinTestFlag) {
        this.skinTestFlag = skinTestFlag;
    }

    public String getIndicationFlag() {
        return indicationFlag;
    }

    public void setIndicationFlag(String indicationFlag) {
        this.indicationFlag = indicationFlag;
    }

    public String getOrderStatusCode() {
        return orderStatusCode;
    }

    public void setOrderStatusCode(String orderStatusCode) {
        this.orderStatusCode = orderStatusCode;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public String getOrderItemSpec() {
        return orderItemSpec;
    }

    public void setOrderItemSpec(String orderItemSpec) {
        this.orderItemSpec = orderItemSpec;
    }
}
