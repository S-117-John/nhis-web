package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;

/**
 * 门诊医嘱
 */
public class OrderOutpat {

    @MetadataDescribe(id= "",name = "",eName = "EMPI_ID")//LHDE0032001 患者主索引号码
    private String empiId;

    @MetadataDescribe(id= "",name = "",eName = "PK_PATIENT")//LHDE0032002 患者主键 
    private String pkPatient;

    @MetadataDescribe(id= "",name = "",eName = "ENCOUNTER_ID")//LHDE0032003 就诊ID
    private String encounterId;

    @MetadataDescribe(id= "",name = "",eName = "ORG_CODE")//LHDE0032004 机构/院部代码
    private String orgCode;

    @MetadataDescribe(id= "",name = "",eName = "ORG_NAME")//LHDE0032005 机构/院部名称
    private String orgName;

    @MetadataDescribe(id= "",name = "",eName = "ENCOUNTER_TYPE_CODE")//LHDE0032006 就诊类别代码
    private String encounterTypeCode;

    @MetadataDescribe(id= "",name = "",eName = "ENCOUNTER_TYPE_NAME")//LHDE0032007 就诊类别名称
    private String encounterTypeName;

    @MetadataDescribe(id= "",name = "",eName = "VISIT_ID")//LHDE0032008 就诊流水号
    private String visitId;

    @MetadataDescribe(id= "",name = "",eName = "VISIT_NO")//LHDE0032009 就诊次数
    private String visitNo;

    @MetadataDescribe(id= "",name = "",eName = "PATIENT_NAME")//LHDE0032010 病人姓名
    private String patientName;

    @MetadataDescribe(id= "",name = "",eName = "GENDER_CODE")//LHDE0032011 性别代码
    private String genderCode;

    @MetadataDescribe(id= "",name = "",eName = "GENDER_NAME")//LHDE0032012 性别名称
    private String genderName;

    @MetadataDescribe(id= "",name = "",eName = "DATE_OF_BIRTH")//LHDE0032013 出生日期
    private String dateOfBirth;

    @MetadataDescribe(id= "",name = "",eName = "AGE_YEAR")//LHDE0032014 年龄-年
    private String ageYear;

    @MetadataDescribe(id= "",name = "",eName = "AGE_MONTH")//LHDE0032015 年龄-月
    private String ageMonth;

    @MetadataDescribe(id= "",name = "",eName = "AGE_DAY")//LHDE0032016  年龄-天
    private String ageDay;

    @MetadataDescribe(id= "",name = "",eName = "AGE_HOUR")//LHDE0032017 年龄-时
    private String ageHour;

    @MetadataDescribe(id= "",name = "",eName = "VISIT_DATE_TIME")//LHDE0032018 就诊日期时间
    private String visitDateTime;

    @MetadataDescribe(id= "",name = "",eName = "DEPT_ID")//LHDE0032019 科室ID
    private String deptId;

    @MetadataDescribe(id= "",name = "",eName = "DEPT_NAME")//LHDE0032020 科室名称
    private String deptName;

    @MetadataDescribe(id= "",name = "",eName = "DIAG_CODE")//LHDE0032021 疾病诊断编码
    private String diagCode;

    @MetadataDescribe(id= "",name = "",eName = "DIAG_NAME")//LHDE0032022 诊断名称
    private String diagName;

    @MetadataDescribe(id= "",name = "",eName = "DIAG_DESC")//LHDE0032023 诊断描述
    private String diagDesc;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_ID")//LHDE0032024 医嘱ID
    private String orderId;

    @MetadataDescribe(id= "",name = "",eName = "PLACER_ORDER_NO")//LHDE0032025 医嘱号
    private String placerOrderNo;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_GROUP_NO")//LHDE0032026 医嘱组号
    private String orderGroupNo;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_ITEM_SEQ_NO")//LHDE0032027 医嘱项目排序号
    private String orderItemSeqNo;

    @MetadataDescribe(id= "",name = "",eName = "PARENT_FLAG")//LHDE0032028 父医嘱标志
    private String parentFlag;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_CATEGORY_CODE")//LHDE0032029 医嘱分类代码
    private String orderCategoryCode;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_CATEGORY_NAME")//LHDE0032030 医嘱分类名称
    private String orderCategoryName;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_ITEM_CODE")//LHDE0032031 医嘱项目代码
    private String orderItemCode;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_ITEM_NAME")//LHDE0032032 医嘱项目名称
    private String orderItemName;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_DESC")//LHDE0032033 医嘱描述
    private String orderDesc;

    @MetadataDescribe(id= "",name = "",eName = "ORDER_ENTRUST")//LHDE0032034 医嘱嘱托
    private String orderEntrust;

    @MetadataDescribe(id= "",name = "",eName = "PRES_NO")//LHDE0032035 处方号码
    private String presNo;

    @MetadataDescribe(id= "",name = "",eName = "PRES_GROUP_NO")//LHDE0032036 处方药品组号
    private String presGroupNo;

    @MetadataDescribe(id= "",name = "",eName = "PRES_TYPE_CODE")//LHDE0032037 处方类型代码
    private String presTypeCode;

    @MetadataDescribe(id= "",name = "",eName = "PRES_TYPE_NAME")//LHDE0032038 处方类型名称
    private String presTypeName;

    @MetadataDescribe(id= "",name = "",eName = "PRES_SUSTAINED_DAYS")//LHDE0032039 处方有效天数
    private String presSustainedDays;

    @MetadataDescribe(id= "",name = "",eName = "PRES_DRUG_AMOUNT")//LHDE0032040 处方药品金额
    private String presDrugAmount;

    @MetadataDescribe(id= "",name = "",eName = "DOSE")//LHDE0032041 药物单次剂量
    private String dose;

    @MetadataDescribe(id= "",name = "",eName = "DOSE_UNIT_CODE")//LHDE0032042 剂量单位代码
    private String doseUnitCode;

    @MetadataDescribe(id= "",name = "",eName = "DOSE_UNIT_NAME")//LHDE0032043 剂量单位名称
    private String doseUnitName;

    @MetadataDescribe(id= "",name = "",eName = "DRUG_FORM_CODE")//LHDE0032044 药物剂型代码
    private String drugFormCode;

    @MetadataDescribe(id= "",name = "",eName = "DRUG_FORM_NAME")//LHDE0032045 药物剂型名称
    private String drugFormName;

    @MetadataDescribe(id= "",name = "",eName = "DRUG_SPEC")//LHDE0032046 药物规格
    private String drugSpec;

    @MetadataDescribe(id= "",name = "",eName = "DRUG_TOTAL_DOSE")//LHDE0032047 药物总剂量
    private String drugTotalDose;

    @MetadataDescribe(id= "",name = "",eName = "HERBAL_QUANTITY")//LHDE0032048 草药副数
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
