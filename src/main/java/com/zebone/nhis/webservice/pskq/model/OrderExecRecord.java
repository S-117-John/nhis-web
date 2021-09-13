package com.zebone.nhis.webservice.pskq.model;


import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 医嘱执行记录
 */
public class OrderExecRecord {

    /**
     * 患者主索引号码
     */
    @MetadataDescribe(id= "LHDE0033001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    /**
     * 患者主键
     */
    @MetadataDescribe(id= "LHDE0033002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    /**
     * 就诊ID
     */
    @MetadataDescribe(id= "LHDE0033003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    /**
     * 机构/院部代码
     */
    @MetadataDescribe(id= "LHDE0033004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    /**
     * 机构/院部名称
     */
    @MetadataDescribe(id= "LHDE0033005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    /**
     * 就诊类别代码
     */
    @MetadataDescribe(id= "LHDE0033006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    /**
     * 就诊类别名称
     */
    @MetadataDescribe(id= "LHDE0033007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    /**
     * 就诊流水号
     */
    @MetadataDescribe(id= "LHDE0033008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    /**
     * 就诊次数
     */
    @MetadataDescribe(id= "LHDE0033009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    /**
     * 病人姓名
     */
    @MetadataDescribe(id= "LHDE0055010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    /**
     * 性别代码
     */
    @MetadataDescribe(id= "LHDE0033011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    /**
     * 性别名称
     */
    @MetadataDescribe(id= "LHDE0033012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    /**
     * 出生日期
     */
    @MetadataDescribe(id= "LHDE0033013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    /**
     * 年龄-年
     */
    @MetadataDescribe(id= "LHDE0033014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    /**
     * 年龄-月
     */
    @MetadataDescribe(id= "LHDE0033015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    /**
     * 年龄-天
     */
    @MetadataDescribe(id= "LHDE0033016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    /**
     * 年龄-时
     */
    @MetadataDescribe(id= "LHDE0033017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    /**
     * 就诊日期时间
     */
    @MetadataDescribe(id= "LHDE0033018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    /**
     * 科室ID
     */
    @MetadataDescribe(id= "LHDE0033019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    /**
     * 科室名称
     */
    @MetadataDescribe(id= "LHDE0033020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    /**
     * 病区ID
     */
    @MetadataDescribe(id= "LHDE0033021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    /**
     * 病区名称
     */
    @MetadataDescribe(id= "LHDE0033022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    /**
     * 床号
     */
    @MetadataDescribe(id= "LHDE0033023",name = "床号",eName = "BED_NO")
    private String bedNo;

    /**
     * 医嘱ID
     */

    private String orderId;

    /**
     * 医嘱执行记录ID
     */
    @MetadataDescribe(id= "LHDE0033024",name = "医嘱执行记录ID",eName = "ORDER_EXEC_RECORD_ID")
    private String execId;

    /**
     * 医嘱执行流水号
     */
    @MetadataDescribe(id= "LHDE0033025",name = "医嘱ID",eName = "ORDER_ID")
    private String execSqn;

    @MetadataDescribe(id= "LHDE0033026",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    /**
     * 医嘱号
     */
    @MetadataDescribe(id= "LHDE0033027",name = "医嘱组号",eName = "ORDER_GROUP_NO")
    private String orderGroupNo;



    @MetadataDescribe(id= "LHDE0033028",name = "医嘱项目排序号",eName = "ORDER_ITEM_SEQ_NO")
    private String orderItemSeqNo;

    /**
     * 父医嘱标志
     */
    @MetadataDescribe(id= "LHDE0033029",name = "父医嘱标志",eName = "PARENT_FLAG")
    private String parentFlag;

    @MetadataDescribe(id= "LHDE0033030",name = "医嘱分类代码",eName = "ORDER_CATEGORY_CODE")
    private String orderCategoryCode;

    @MetadataDescribe(id= "LHDE0033031",name = "医嘱分类名称",eName = "ORDER_CATEGORY_NAME")
    private String orderCategoryName;

    @MetadataDescribe(id= "LHDE0033032",name = "医嘱类型代码",eName = "ORDER_TYPE_CODE")
    private String orderTypeCode;

    @MetadataDescribe(id= "LHDE0033033",name = "医嘱类型名称",eName = "ORDER_TYPE_NAME")
    private String orderTypeName;

    @MetadataDescribe(id= "LHDE0033034",name = "医嘱项目代码",eName = "ORDER_ITEM_CODE")
    private String orderItemCode;

    @MetadataDescribe(id= "LHDE0033035",name = "医嘱项目名称",eName = "ORDER_ITEM_NAME")
    private String orderItemName;

    @MetadataDescribe(id= "LHDE0033036",name = "医嘱描述",eName = "ORDER_DESC")
    private String orderDesc;

    @MetadataDescribe(id= "LHDE0033037",name = "医嘱嘱托",eName = "ORDER_ENTRUST")
    private String orderEntrust;

    @MetadataDescribe(id= "LHDE0033038",name = "药物单次剂量",eName = "DOSE")
    private String dose;

    @MetadataDescribe(id= "LHDE0033039",name = "剂量单位代码",eName = "DOSE_UNIT_CODE")
    private String doseUnitCode;

    @MetadataDescribe(id= "LHDE0033040",name = "剂量单位名称",eName = "DOSE_UNIT_NAME")
    private String DOSE_UNIT_NAME;

    @MetadataDescribe(id= "LHDE0033041",name = "药物剂型代码",eName = "DRUG_FORM_CODE")
    private String drugFormCode;

    @MetadataDescribe(id= "LHDE0033042",name = "药物剂型名称",eName = "DRUG_FORM_NAME")
    private String drugFormName;

    @MetadataDescribe(id= "LHDE0033043",name = "药物规格",eName = "DRUG_SPEC")
    private String drugSpec;

    @MetadataDescribe(id= "LHDE0033044",name = "药物总剂量",eName = "DRUG_TOTAL_DOSE")
    private String drugTotalDose;

    @MetadataDescribe(id= "LHDE0033045",name = "草药副数",eName = "HERBAL_QUANTITY")
    private String herbalQuantity;

    @MetadataDescribe(id= "LHDE0033046",name = "草药用药方法",eName = "CHM_USAGE")
    private String chmUsage;

    @MetadataDescribe(id= "LHDE0033047",name = "用量",eName = "USE_QUANTITY")
    private String useQuantity;

    @MetadataDescribe(id= "LHDE0033048",name = "用量单位代码",eName = "USE_UNIT_CODE")
    private String useUnitCode;

    @MetadataDescribe(id= "LHDE0033049",name = "用量单位名称",eName = "USE_UNIT_NAME")
    private String useUnitName;

    @MetadataDescribe(id= "LHDE0033050",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;

    @MetadataDescribe(id= "LHDE0033051",name = "库房代码",eName = "STORAGE_CODE")
    private String storageCode;

    @MetadataDescribe(id= "LHDE0033052",name = "库房名称",eName = "STORAGE_NAME")
    private String storageName;

    @MetadataDescribe(id= "LHDE0033053",name = "药物频率代码",eName = "DRUG_FREQ_CODE")
    private String drugFreqCode;

    @MetadataDescribe(id= "LHDE0033054",name = "药物频率名称",eName = "DRUG_FREQ_NAME")
    private String drugFreqName;

    @MetadataDescribe(id= "LHDE0033055",name = "用药途径代码",eName = "USAGE_CODE")
    private String usageCode;

    @MetadataDescribe(id= "LHDE0033056",name = "用药途径名称",eName = "USAGE_NAME")
    private String usageName;

    @MetadataDescribe(id= "LHDE0033057",name = "执行次数",eName = "EXEC_TIMES")
    private String execTimes;

    @MetadataDescribe(id= "LHDE0033058",name = "执行天数",eName = "EXEC_DAYS")
    private String execDays;

    @MetadataDescribe(id= "LHDE0033059",name = "滴速",eName = "DROP_SPEED")
    private String dropSpeed;

    @MetadataDescribe(id= "LHDE0033060",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0033061",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0033062",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0033063",name = "开立医师ID",eName = "ORDER_DOCTOR_ID")
    private String orderDoctorId;

    @MetadataDescribe(id= "LHDE0033064",name = "开立医师姓名",eName = "ORDER_DOCTOR_NAME")
    private String orderDoctorName;

    @MetadataDescribe(id= "LHDE0033065",name = "医嘱开始日期时间",eName = "ORDER_START_DATE_TIME")
    private String orderStartDateTime;

    @MetadataDescribe(id= "LHDE0033066",name = "医嘱结束日期时间",eName = "ORDER_END_DATE_TIME")
    private String orderEndDateTime;

    @MetadataDescribe(id= "LHDE0033067",name = "核对日期时间",eName = "CONFIRM_DATE_TIME")
    private String confirmDateTime;

    @MetadataDescribe(id= "LHDE0033068",name = "核对日期时间",eName = "CONFIRM_NURSE_ID")
    private String confirmNurseId;

    @MetadataDescribe(id= "LHDE0033069",name = "核对护士姓名",eName = "CONFIRM_NURSE_NAME")
    private String confirmNurseName;

    @MetadataDescribe(id= "LHDE0033070",name = "审核日期时间",eName = "CHECK_DATE_TIME")
    private String checkDateTime;

    @MetadataDescribe(id= "LHDE0033071",name = "审核人ID",eName = "CHECK_OPERA_ID")
    private String checkOperaId;

    @MetadataDescribe(id= "LHDE0033072",name = "审核人姓名",eName = "CHECK_OPERA_NAME")
    private String checkOperaName;

    @MetadataDescribe(id= "LHDE0033073",name = "停止日期时间",eName = "停止日期时间")
    private String stopDateTime;

    @MetadataDescribe(id= "LHDE0033074",name = "停止医师ID",eName = "STOP_OPERA_ID")
    private String stopOperaId;

    @MetadataDescribe(id= "LHDE0033075",name = "停止医师姓名",eName = "STOP_OPERA_NAME")
    private String stopOperaName;

    @MetadataDescribe(id= "LHDE0033076",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0033077",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0033078",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0033079",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0033080",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0033081",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0033082",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0033083",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0033084",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;

    @MetadataDescribe(id= "LHDE0033085",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;

    @MetadataDescribe(id= "LHDE0033086",name = "自备标志",eName = "SELF_OWNED_FLAG")
    private String selfOwnedFlag;

    @MetadataDescribe(id= "LHDE0033087",name = "婴儿标志",eName = "INFANT_FLAG")
    private String infantFlag;

    @MetadataDescribe(id= "LHDE0033088",name = "手术标志",eName = "SURG_FLAG")
    private String surgFlag;

    @MetadataDescribe(id= "LHDE0033089",name = "皮试标志",eName = "SKIN_TEST_FLAG")
    private String skinTestFlag;

    @MetadataDescribe(id= "LHDE0033090",name = "适应症标志",eName = "INDICATION_FLAG")
    private String indicationFlag;

    @MetadataDescribe(id= "LHDE0033091",name = "出院带药",eName = "DISCHARGE_FLAG")
    private String dischargeFlag;

    @MetadataDescribe(id= "LHDE0033092",name = "医嘱状态代码",eName = "ORDER_STATUS_CODE")
    private String orderStatusCode;

    @MetadataDescribe(id= "LHDE0033093",name = "医嘱状态名称",eName = "ORDER_STATUS_NAME")
    private String orderStatusName;

    @MetadataDescribe(id= "LHDE0033094",name = "医嘱规格",eName = "ORDER_ITEM_SPEC")
    private String orderItemSpec;

    @MetadataDescribe(id= "LHDE0033095",name = "皮试结果",eName = "SKIN_RESULT")
    private String skinResult;

    @MetadataDescribe(id= "LHDE0033096",name = "预计执行时间",eName = "SCHEDULE_DATE_TIME")
    private String scheduleDateTime;

    @MetadataDescribe(id= "LHDE0033097",name = "医嘱执行卡代码",eName = "ORDER_CARD_CODE")
    private String orderCardCode;

    @MetadataDescribe(id= "LHDE0033098",name = "医嘱执行卡代码",eName = "ORDER_CARD_NAME")
    private String orderCardName;

    public String getOrderCardCode() {
        return orderCardCode;
    }

    public void setOrderCardCode(String orderCardCode) {
        this.orderCardCode = orderCardCode;
    }

    public String getOrderCardName() {
        return orderCardName;
    }

    public void setOrderCardName(String orderCardName) {
        this.orderCardName = orderCardName;
    }

    public String getScheduleDateTime() {
        return scheduleDateTime;
    }

    public void setScheduleDateTime(String scheduleDateTime) {
        this.scheduleDateTime = scheduleDateTime;
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getExecId() {
        return execId;
    }

    public void setExecId(String execId) {
        this.execId = execId;
    }

    public String getExecSqn() {
        return execSqn;
    }

    public void setExecSqn(String execSqn) {
        this.execSqn = execSqn;
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

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
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

    public String getDOSE_UNIT_NAME() {
        return DOSE_UNIT_NAME;
    }

    public void setDOSE_UNIT_NAME(String DOSE_UNIT_NAME) {
        this.DOSE_UNIT_NAME = DOSE_UNIT_NAME;
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

    public String getOrderStartDateTime() {
        return orderStartDateTime;
    }

    public void setOrderStartDateTime(String orderStartDateTime) {
        this.orderStartDateTime = orderStartDateTime;
    }

    public String getOrderEndDateTime() {
        return orderEndDateTime;
    }

    public void setOrderEndDateTime(String orderEndDateTime) {
        this.orderEndDateTime = orderEndDateTime;
    }

    public String getConfirmDateTime() {
        return confirmDateTime;
    }

    public void setConfirmDateTime(String confirmDateTime) {
        this.confirmDateTime = confirmDateTime;
    }

    public String getConfirmNurseId() {
        return confirmNurseId;
    }

    public void setConfirmNurseId(String confirmNurseId) {
        this.confirmNurseId = confirmNurseId;
    }

    public String getConfirmNurseName() {
        return confirmNurseName;
    }

    public void setConfirmNurseName(String confirmNurseName) {
        this.confirmNurseName = confirmNurseName;
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

    public String getStopDateTime() {
        return stopDateTime;
    }

    public void setStopDateTime(String stopDateTime) {
        this.stopDateTime = stopDateTime;
    }

    public String getStopOperaId() {
        return stopOperaId;
    }

    public void setStopOperaId(String stopOperaId) {
        this.stopOperaId = stopOperaId;
    }

    public String getStopOperaName() {
        return stopOperaName;
    }

    public void setStopOperaName(String stopOperaName) {
        this.stopOperaName = stopOperaName;
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

    public String getSelfOwnedFlag() {
        return selfOwnedFlag;
    }

    public void setSelfOwnedFlag(String selfOwnedFlag) {
        this.selfOwnedFlag = selfOwnedFlag;
    }

    public String getInfantFlag() {
        return infantFlag;
    }

    public void setInfantFlag(String infantFlag) {
        this.infantFlag = infantFlag;
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

    public String getDischargeFlag() {
        return dischargeFlag;
    }

    public void setDischargeFlag(String dischargeFlag) {
        this.dischargeFlag = dischargeFlag;
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

    public String getSkinResult() {
        return skinResult;
    }

    public void setSkinResult(String skinResult) {
        this.skinResult = skinResult;
    }
}
