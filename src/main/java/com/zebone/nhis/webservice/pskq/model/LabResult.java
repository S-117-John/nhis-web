package com.zebone.nhis.webservice.pskq.model;

import com.zebone.nhis.webservice.pskq.annotation.MetadataDescribe;

/**
 * 检验结果
 */
public class LabResult {

    @MetadataDescribe(id= "LHDE0025001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0025002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0025003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0025004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0025005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0025006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0025007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0025008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0025009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0025010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0025011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0025012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0025013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0025014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0025015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0025016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0025017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0025018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0025019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0025020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0025021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0025022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0025023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0025024",name = "检验报告ID",eName = "LAB_RESULT_ID")
    private String labResultId;

    @MetadataDescribe(id= "LHDE0025025",name = "报告单号",eName = "FILLER_ORDER_NO")
    private String fillerOrderNo;

    @MetadataDescribe(id= "LHDE0025026",name = "检验申请ID",eName = "LAB_APPLY_ID")
    private String labApplyId;

    @MetadataDescribe(id= "LHDE0025027",name = "检验申请单号",eName = "LAB_APPLY_NO")
    private String labApplyNo;

    @MetadataDescribe(id= "LHDE0025028",name = "医嘱号",eName = "PLACER_ORDER_NO")
    private String placerOrderNo;

    @MetadataDescribe(id= "LHDE0025029",name = "申请日期时间",eName = "APPLY_DATE_TIME")
    private String applyDateTime;

    @MetadataDescribe(id= "LHDE0025030",name = "申请科室ID",eName = "APPLY_DEPT_ID")
    private String applyDeptId;

    @MetadataDescribe(id= "LHDE0025031",name = "申请科室名称",eName = "APPLY_DEPT_NAME")
    private String applyDeptName;

    @MetadataDescribe(id= "LHDE0025032",name = "申请医师ID",eName = "APPLY_DOCTOR_ID")
    private String applyDoctorId;

    @MetadataDescribe(id= "LHDE0025033",name = "申请医师姓名",eName = "APPLY_DOCTOR_NAME")
    private String applyDoctorName;

    @MetadataDescribe(id= "LHDE0025034",name = "诊断日期时间",eName = "DIAG_DATE_TIME")
    private String diagDateTime;

    @MetadataDescribe(id= "LHDE0025035",name = "疾病诊断编码",eName = "DIAG_CODE")
    private String diagCode;

    @MetadataDescribe(id= "LHDE0025036",name = "诊断名称",eName = "DIAG_NAME")
    private String diagName;

    @MetadataDescribe(id= "LHDE0025037",name = "诊断描述",eName = "DIAG_DESC")
    private String diagDesc;

    @MetadataDescribe(id= "LHDE0025038",name = "检查目的描述",eName = "APPLY_PURPOSE_DESC")
    private String applyPurposeDesc;

    @MetadataDescribe(id= "LHDE0025039",name = "检验分类代码",eName = "LAB_CATEGORY_CODE")
    private String labCategoryCode;

    @MetadataDescribe(id= "LHDE0025040",name = "检验分类名称",eName = "LAB_CATEGORY_NAME")
    private String labCategoryName;

    @MetadataDescribe(id= "LHDE0025041",name = "医嘱项目代码",eName = "UNIVERSAL_SERVICE_CODE")
    private String universalServiceCode;

    @MetadataDescribe(id= "LHDE0025042",name = "医嘱项目名称",eName = "UNIVERSAL_SERVICE_NAME")
    private String universalServiceName;

    @MetadataDescribe(id= "LHDE0025043",name = "标本类型代码",eName = "SPECIMEN_TYPE_CODE")
    private String specimenTypeCode;

    @MetadataDescribe(id= "LHDE0025044",name = "标本类型名称",eName = "SPECIMEN_TYPE_NAME")
    private String specimenTypeName;

    @MetadataDescribe(id= "LHDE0025045",name = "标本号",eName = "SPECIMEN_NO")
    private String specimenNo;

    @MetadataDescribe(id= "LHDE0025046",name = "执行日期时间",eName = "EXEC_DATE_TIME")
    private String execDateTime;

    @MetadataDescribe(id= "LHDE0025047",name = "执行科室ID",eName = "EXEC_DEPT_ID")
    private String execDeptId;

    @MetadataDescribe(id= "LHDE0025048",name = "执行科室名称",eName = "EXEC_DEPT_NAME")
    private String execDeptName;

    @MetadataDescribe(id= "LHDE0025049",name = "执行人ID",eName = "EXEC_OPERA_ID")
    private String execOperaId;

    @MetadataDescribe(id= "LHDE0025050",name = "执行人姓名",eName = "EXEC_OPERA_NAME")
    private String execOperaName;

    @MetadataDescribe(id= "LHDE0025051",name = "报告日期时间",eName = "REPORT_DATE_TIME")
    private String reportDateTime;

    @MetadataDescribe(id= "LHDE0025052",name = "报告人ID",eName = "REPORT_OPERA_ID")
    private String reportOperaId;

    @MetadataDescribe(id= "LHDE0025053",name = "报告医师姓名",eName = "REPORT_OPERA_NAME")
    private String reportOperaName;

    @MetadataDescribe(id= "LHDE0025054",name = "复审日期时间",eName = "REVIEW_DATE_TIME")
    private String reviewDateTime;

    @MetadataDescribe(id= "LHDE0025055",name = "复审人ID",eName = "REVIEW_OPERA_ID")
    private String reviewOperaId;

    @MetadataDescribe(id= "LHDE0025056",name = "复审人姓名",eName = "REVIEW_OPERA_NAME")
    private String reviewOperaName;

    @MetadataDescribe(id= "LHDE0025057",name = "设备代码",eName = "EQUIPMENT_CODE")
    private String equipmentCode;

    @MetadataDescribe(id= "LHDE0025058",name = "设备名称",eName = "EQUIPMENT_NAME")
    private String equipmentName;

    @MetadataDescribe(id= "LHDE0025059",name = "报告单状态代码",eName = "REPORT_STATUS_CODE")
    private String reportStatusCode;

    @MetadataDescribe(id= "LHDE0025060",name = "报告单状态名称",eName = "REPORT_STATUS_NAME")
    private String reportStatusName;

    @MetadataDescribe(id= "LHDE0025061",name = "检验实验室代码",eName = "LAB_GROUP_CODE")
    private String labGroupCode;

    @MetadataDescribe(id= "LHDE0025062",name = "检验实验室名称",eName = "LAB_GROUP_NAME")
    private String labGroupName;

    @MetadataDescribe(id= "LHDE0025063",name = "文书内容_PDF",eName = "DOCUMENT_CONTENT_PDF")
    private String documentContentPdf;

    @MetadataDescribe(id= "LHDE0025064",name = "CA数字签名hash值",eName = "CA_HASH")
    private String caHash;

    @MetadataDescribe(id= "LHDE0025065",name = "病历文书PDF路径",eName = "DOCUMENT_CONTENT_PDF_URL")
    private String documentContentPdfUrl;

    @MetadataDescribe(id= "LHDE0025066",name = "检验类型代码",eName = "LAB_TYPE_CODE")
    private String labTypeCode;

    @MetadataDescribe(id= "LHDE0025067",name = "检验类型名称",eName = "LAB_TYPE_NAME")
    private String labTypeName;


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

    public String getLabResultId() {
        return labResultId;
    }

    public void setLabResultId(String labResultId) {
        this.labResultId = labResultId;
    }

    public String getFillerOrderNo() {
        return fillerOrderNo;
    }

    public void setFillerOrderNo(String fillerOrderNo) {
        this.fillerOrderNo = fillerOrderNo;
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

    public String getApplyPurposeDesc() {
        return applyPurposeDesc;
    }

    public void setApplyPurposeDesc(String applyPurposeDesc) {
        this.applyPurposeDesc = applyPurposeDesc;
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

    public String getReportDateTime() {
        return reportDateTime;
    }

    public void setReportDateTime(String reportDateTime) {
        this.reportDateTime = reportDateTime;
    }

    public String getReportOperaId() {
        return reportOperaId;
    }

    public void setReportOperaId(String reportOperaId) {
        this.reportOperaId = reportOperaId;
    }

    public String getReportOperaName() {
        return reportOperaName;
    }

    public void setReportOperaName(String reportOperaName) {
        this.reportOperaName = reportOperaName;
    }

    public String getReviewDateTime() {
        return reviewDateTime;
    }

    public void setReviewDateTime(String reviewDateTime) {
        this.reviewDateTime = reviewDateTime;
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

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getReportStatusCode() {
        return reportStatusCode;
    }

    public void setReportStatusCode(String reportStatusCode) {
        this.reportStatusCode = reportStatusCode;
    }

    public String getReportStatusName() {
        return reportStatusName;
    }

    public void setReportStatusName(String reportStatusName) {
        this.reportStatusName = reportStatusName;
    }

    public String getLabGroupCode() {
        return labGroupCode;
    }

    public void setLabGroupCode(String labGroupCode) {
        this.labGroupCode = labGroupCode;
    }

    public String getLabGroupName() {
        return labGroupName;
    }

    public void setLabGroupName(String labGroupName) {
        this.labGroupName = labGroupName;
    }

    public String getDocumentContentPdf() {
        return documentContentPdf;
    }

    public void setDocumentContentPdf(String documentContentPdf) {
        this.documentContentPdf = documentContentPdf;
    }

    public String getCaHash() {
        return caHash;
    }

    public void setCaHash(String caHash) {
        this.caHash = caHash;
    }

    public String getDocumentContentPdfUrl() {
        return documentContentPdfUrl;
    }

    public void setDocumentContentPdfUrl(String documentContentPdfUrl) {
        this.documentContentPdfUrl = documentContentPdfUrl;
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
}
