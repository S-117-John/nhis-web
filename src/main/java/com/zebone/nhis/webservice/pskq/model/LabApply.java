package com.zebone.nhis.webservice.pskq.model;


import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 检验申请
 */
public class LabApply {

    @MetadataDescribe(id= "LHDE0024001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0024002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0024003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0024004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0024005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0024006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0024007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0024008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0024009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0024010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0024011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0024012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0024013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0024014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0024015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0024016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0024017",name = "年龄-时",eName = "AGE_HOUR;")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0024018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0024019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0024020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0024021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0024022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0024023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0024024",name = "检验申请ID",eName = "LAB_APPLY_ID")
    private String labApplyId;

    @MetadataDescribe(id= "LHDE0024024",name = "检验申请单号",eName = "LAB_APPLY_NO")
    private String labApplyNo;

    @MetadataDescribe(id= "LHDE0024025",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    @MetadataDescribe(id= "LHDE0024026",name = "申请日期时间",eName = "APPLY_DATE_TIME")
    private String applyDateTime;

    @MetadataDescribe(id= "LHDE0024027",name = "机构/申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0024028",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0024029",name = "申请医师ID",eName = "APPLY_DOCTOR_ID")
    private String applyDoctorId;

    @MetadataDescribe(id= "LHDE0024030",name = "申请医师姓名",eName = "APPLY_DOCTOR_NAME")
    private String applyDoctorName;

    @MetadataDescribe(id= "LHDE0024031",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;

    @MetadataDescribe(id= "LHDE0024032",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;

    @MetadataDescribe(id= "LHDE0024033",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;

    @MetadataDescribe(id= "LHDE0024034",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;

    @MetadataDescribe(id= "LHDE0024035",name = "申请单备注",eName = "APPLY_CMMT")
    private String applyCmmt;

    @MetadataDescribe(id= "LHDE0024036",name = "检验分类代码",eName = "LAB_CATEGORY_CODE")
    private String labCategoryCode;

    @MetadataDescribe(id= "LHDE0024037",name = "检验分类名称",eName = "LAB_CATEGORY_NAME")
    private String labCategoryName;

    @MetadataDescribe(id= "LHDE0024038",name = "医嘱项目代码",eName = "EMPI_ID")
    private String universalServiceCode;

    @MetadataDescribe(id= "LHDE0024040",name = "医嘱项目名称",eName = "UNIVERSAL_SERVICE_NAME")
    private String universalServiceName;

    @MetadataDescribe(id= "LHDE0024041",name = "标本类型代码",eName = "SPECIMEN_TYPE_CODE")
    private String specimenTypeCode;

    @MetadataDescribe(id= "LHDE0024042",name = "标本类型名称",eName = "SPECIMEN_TYPE_NAME")
    private String specimenTypeName;

    @MetadataDescribe(id= "LHDE0024043",name = "标本号",eName = "SPECIMEN_NO")
    private String specimenNo;

    @MetadataDescribe(id= "LHDE0024044",name = "采集方式代码",eName = "COLLECT_METHOD_CODE")
    private String collectMethodCode;

    @MetadataDescribe(id= "LHDE0024045",name = "采集方式名称",eName = "COLLECT_METHOD_NAME")
    private String collectMethodName;

    @MetadataDescribe(id= "LHDE0024046",name = "试管类型代码",eName = "CONTAINER_TYPE_CODE")
    private String containerTypeCode;

    @MetadataDescribe(id= "LHDE0024047",name = "试管类型名称",eName = "CONTAINER_TYPE_NAME")
    private String containerTypeName;

    @MetadataDescribe(id= "LHDE0024048",name = "采样日期时间",eName = "COLLECT_DATE_TIME")
    private String collectDateTime;

    @MetadataDescribe(id= "LHDE0024049",name = "采样科室ID",eName = "COLLECT_DEPT_ID")
    private String collectDeptId;

    @MetadataDescribe(id= "LHDE0024050",name = "采样科室名称",eName = "COLLECT_DEPT_NAME")
    private String collectDeptName;

    @MetadataDescribe(id= "LHDE0024051",name = "采样医师ID",eName = "COLLECT_OPERA_ID")
    private String collectOperaId;

    @MetadataDescribe(id= "LHDE0024052",name = "采样医师姓名",eName = "COLLECT_OPERA_NAME")
    private String collectOperaName;

    @MetadataDescribe(id= "LHDE0024053",name = "送检日期时间",eName = "DELIVERY_DATE_TIME")
    private String deliveryDateTime;

    @MetadataDescribe(id= "LHDE0024054",name = "送检人ID",eName = "DELIVERY_OPERA_ID")
    private String deliveryOperaId;

    @MetadataDescribe(id= "LHDE0024055",name = "送检医师姓名",eName = "DELIVERY_OPERA_NAME")
    private String deliveryOperaName;

    @MetadataDescribe(id= "LHDE0024056",name = "接收日期时间",eName = "RECEIVE_DATE_TIME")
    private String receiveDateTime;

    @MetadataDescribe(id= "LHDE0024057",name = "接收人ID",eName = "RECEIVE_OPERA_ID")
    private String receiveOperaId;

    @MetadataDescribe(id= "LHDE0024058",name = "接收医师姓名",eName = "RECEIVE_OPERA_NAME")
    private String receiveOperaName;

    @MetadataDescribe(id= "LHDE0024059",name = "作废标志",eName = "CANCEL_FLAG")
    private String cancelFlag;

    @MetadataDescribe(id= "LHDE0024060",name = "取消日期时间",eName = "CANCEL_DATE_TIME")
    private String cancelDateTime;

    @MetadataDescribe(id= "LHDE0024061",name = "撤销原因描述",eName = "CANCEL_REASON_DESC")
    private String cancelReasonDesc;

    @MetadataDescribe(id= "LHDE0024062",name = "撤销人ID",eName = "CANCEL_OPERA_ID")
    private String cancelOperaId;

    @MetadataDescribe(id= "LHDE0024063",name = "撤销人姓名",eName = "CANCEL_OPERA_NAME")
    private String cancelOperaName;

    @MetadataDescribe(id= "LHDE0024064",name = "执行机构/院部代码",eName = "EXEC_ORG_CODE")
    private String execOrgCode;

    @MetadataDescribe(id= "LHDE0024065",name = "执行机构/院部名称",eName = "EXEC_ORG_NAME")
    private String execOrgName;

    @MetadataDescribe(id= "LHDE0024066",name = "执行系统代码",eName = "EXEC_SYSTEM_CODE")
    private String execSystemCode;

    @MetadataDescribe(id= "LHDE0024067",name = "执行系统名称",eName = "EXEC_SYSTEM_NAME")
    private String execSystemName;

    @MetadataDescribe(id= "LHDE0024068",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0024069",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0024070",name = "紧急标志（'1'紧急'0'普通）",eName = "EMER_FLAG")
    private String emerFlag;

    @MetadataDescribe(id= "LHDE0024071",name = "绿色通道标志",eName = "GREEN_CHANNEL_FLAG")
    private String greenChannelFlag;

    @MetadataDescribe(id= "LHDE0024072",name = "费用金额",eName = "FEE_AMOUNT")
    private String feeAmount;

    @MetadataDescribe(id= "LHDE0024073",name = "申请单状态代码",eName = "APPLY_STATUS_CODE")
    private String applyStatusCode;

    @MetadataDescribe(id= "LHDE0024074",name = "申请单状态名称",eName = "APPLY_STATUS_NAME")
    private String applyStatusName;

    @MetadataDescribe(id= "LHDE0024075",name = "检验类型代码",eName = "LAB_TYPE_CODE")
    private String labTypeCode;

    @MetadataDescribe(id= "LHDE0024076",name = "检验类型名称",eName = "LAB_TYPE_NAME")
    private String labTypeName;

    @MetadataDescribe(id= "LHDE0024077",name = "打印时间",eName = "PRINT_DATE_TIME")
    private String printDateTime;

    @MetadataDescribe(id= "LHDE0024078",name = "打印人ID",eName = "PRINT_OPERA_ID")
    private String printOperaId;

    @MetadataDescribe(id= "LHDE0024079",name = "打印人姓名",eName = "PRINT_OPERA_NAME")
    private String printOperaName;

    @MetadataDescribe(id= "LHDE0024080",name = "收费日期时间",eName = "PRICE_DATE_TIME")
    private String priceDateTime;

    @MetadataDescribe(id= "LHDE0024081",name = "收费人ID",eName = "PRICE_OPERA_ID")
    private String priceOperaId;

    @MetadataDescribe(id= "LHDE0024082",name = "收费人姓名",eName = "PRICE_OPERA_NAME")
    private String priceOperaName;

    @MetadataDescribe(id= "LHDE0024083",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0024084",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0024085",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;


    @MetadataDescribe(id= "LHDE0024086",name = "收费数量",eName = "CHARGE_QUANTITY")
    private String chargeQuantity;

    @MetadataDescribe(id= "LHDE0024087",name = "条形码",eName = "BAR_CODE")
    private String barCode;

    @MetadataDescribe(id= "LHDE0024088",name = "采样地点",eName = "COLLECT_ADDRESS")
    private String collectAddress;


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

    public String getLabApplyId() {
        return labApplyId;
    }

    public void setLabApplyId(String labApplyId) {
        this.labApplyId = labApplyId;
    }

    public String getLabApplyNo() {
        return labApplyNo;
    }

    public void setLabApplyNo(String labApplyNo) {
        this.labApplyNo = labApplyNo;
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

    public String getApplyCmmt() {
        return applyCmmt;
    }

    public void setApplyCmmt(String applyCmmt) {
        this.applyCmmt = applyCmmt;
    }

    public String getLabCategoryCode() {
        return labCategoryCode;
    }

    public void setLabCategoryCode(String labCategoryCode) {
        this.labCategoryCode = labCategoryCode;
    }

    public String getLabCategoryName() {
        return labCategoryName;
    }

    public void setLabCategoryName(String labCategoryName) {
        this.labCategoryName = labCategoryName;
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

    public String getSpecimenTypeCode() {
        return specimenTypeCode;
    }

    public void setSpecimenTypeCode(String specimenTypeCode) {
        this.specimenTypeCode = specimenTypeCode;
    }

    public String getSpecimenTypeName() {
        return specimenTypeName;
    }

    public void setSpecimenTypeName(String specimenTypeName) {
        this.specimenTypeName = specimenTypeName;
    }

    public String getSpecimenNo() {
        return specimenNo;
    }

    public void setSpecimenNo(String specimenNo) {
        this.specimenNo = specimenNo;
    }

    public String getCollectMethodCode() {
        return collectMethodCode;
    }

    public void setCollectMethodCode(String collectMethodCode) {
        this.collectMethodCode = collectMethodCode;
    }

    public String getCollectMethodName() {
        return collectMethodName;
    }

    public void setCollectMethodName(String collectMethodName) {
        this.collectMethodName = collectMethodName;
    }

    public String getContainerTypeCode() {
        return containerTypeCode;
    }

    public void setContainerTypeCode(String containerTypeCode) {
        this.containerTypeCode = containerTypeCode;
    }

    public String getContainerTypeName() {
        return containerTypeName;
    }

    public void setContainerTypeName(String containerTypeName) {
        this.containerTypeName = containerTypeName;
    }

    public String getCollectDateTime() {
        return collectDateTime;
    }

    public void setCollectDateTime(String collectDateTime) {
        this.collectDateTime = collectDateTime;
    }

    public String getCollectDeptId() {
        return collectDeptId;
    }

    public void setCollectDeptId(String collectDeptId) {
        this.collectDeptId = collectDeptId;
    }

    public String getCollectDeptName() {
        return collectDeptName;
    }

    public void setCollectDeptName(String collectDeptName) {
        this.collectDeptName = collectDeptName;
    }

    public String getCollectOperaId() {
        return collectOperaId;
    }

    public void setCollectOperaId(String collectOperaId) {
        this.collectOperaId = collectOperaId;
    }

    public String getCollectOperaName() {
        return collectOperaName;
    }

    public void setCollectOperaName(String collectOperaName) {
        this.collectOperaName = collectOperaName;
    }

    public String getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(String deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public String getDeliveryOperaId() {
        return deliveryOperaId;
    }

    public void setDeliveryOperaId(String deliveryOperaId) {
        this.deliveryOperaId = deliveryOperaId;
    }

    public String getDeliveryOperaName() {
        return deliveryOperaName;
    }

    public void setDeliveryOperaName(String deliveryOperaName) {
        this.deliveryOperaName = deliveryOperaName;
    }

    public String getReceiveDateTime() {
        return receiveDateTime;
    }

    public void setReceiveDateTime(String receiveDateTime) {
        this.receiveDateTime = receiveDateTime;
    }

    public String getReceiveOperaId() {
        return receiveOperaId;
    }

    public void setReceiveOperaId(String receiveOperaId) {
        this.receiveOperaId = receiveOperaId;
    }

    public String getReceiveOperaName() {
        return receiveOperaName;
    }

    public void setReceiveOperaName(String receiveOperaName) {
        this.receiveOperaName = receiveOperaName;
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

    public String getLabTypeCode() {
        return labTypeCode;
    }

    public void setLabTypeCode(String labTypeCode) {
        this.labTypeCode = labTypeCode;
    }

    public String getLabTypeName() {
        return labTypeName;
    }

    public void setLabTypeName(String labTypeName) {
        this.labTypeName = labTypeName;
    }

    public String getPrintDateTime() {
        return printDateTime;
    }

    public void setPrintDateTime(String printDateTime) {
        this.printDateTime = printDateTime;
    }

    public String getPrintOperaId() {
        return printOperaId;
    }

    public void setPrintOperaId(String printOperaId) {
        this.printOperaId = printOperaId;
    }

    public String getPrintOperaName() {
        return printOperaName;
    }

    public void setPrintOperaName(String printOperaName) {
        this.printOperaName = printOperaName;
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

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getCollectAddress() {
        return collectAddress;
    }

    public void setCollectAddress(String collectAddress) {
        this.collectAddress = collectAddress;
    }
}
