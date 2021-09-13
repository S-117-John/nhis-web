package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.zebone.nhis.ma.pub.platform.pskq.annotation.MetadataDescribe;


public class AdtChangeInfo {


    @MetadataDescribe(id= "LHDE0045001",name = "患者主索引号码",eName = "EMPI_ID")
    private String empiId;

    @MetadataDescribe(id= "LHDE0045002",name = "患者主键",eName = "PK_PATIENT")
    private String pkPatient;

    @MetadataDescribe(id= "LHDE0045003",name = "就诊ID",eName = "ENCOUNTER_ID")
    private String encounterId;

    @MetadataDescribe(id= "LHDE0045004",name = "机构/院部代码",eName = "ORG_CODE")
    private String orgCode;

    @MetadataDescribe(id= "LHDE0045005",name = "机构/院部名称",eName = "ORG_NAME")
    private String orgName;

    @MetadataDescribe(id= "LHDE0045006",name = "就诊类别代码",eName = "ENCOUNTER_TYPE_CODE")
    private String encounterTypeCode;

    @MetadataDescribe(id= "LHDE0045007",name = "就诊类别名称",eName = "ENCOUNTER_TYPE_NAME")
    private String encounterTypeName;

    @MetadataDescribe(id= "LHDE0045008",name = "就诊流水号",eName = "VISIT_ID")
    private String visitId;

    @MetadataDescribe(id= "LHDE0045009",name = "就诊次数",eName = "VISIT_NO")
    private String visitNo;

    @MetadataDescribe(id= "LHDE0045010",name = "病人姓名",eName = "PATIENT_NAME")
    private String patientName;

    @MetadataDescribe(id= "LHDE0045011",name = "性别代码",eName = "GENDER_CODE")
    private String genderCode;

    @MetadataDescribe(id= "LHDE0045012",name = "性别名称",eName = "GENDER_NAME")
    private String genderName;

    @MetadataDescribe(id= "LHDE0045013",name = "出生日期",eName = "DATE_OF_BIRTH")
    private String dateOfBirth;

    @MetadataDescribe(id= "LHDE0045014",name = "年龄-年",eName = "AGE_YEAR")
    private String ageYear;

    @MetadataDescribe(id= "LHDE0045015",name = "年龄-月",eName = "AGE_MONTH")
    private String ageMonth;

    @MetadataDescribe(id= "LHDE0045016",name = "年龄-天",eName = "AGE_DAY")
    private String ageDay;

    @MetadataDescribe(id= "LHDE0045017",name = "年龄-时",eName = "AGE_HOUR")
    private String ageHour;

    @MetadataDescribe(id= "LHDE0045018",name = "就诊日期时间",eName = "VISIT_DATE_TIME")
    private String visitDateTime;

    @MetadataDescribe(id= "LHDE0045019",name = "科室ID",eName = "DEPT_ID")
    private String deptId;

    @MetadataDescribe(id= "LHDE0045020",name = "科室名称",eName = "DEPT_NAME")
    private String deptName;

    @MetadataDescribe(id= "LHDE0045021",name = "病区ID",eName = "WARD_ID")
    private String wardId;

    @MetadataDescribe(id= "LHDE0045022",name = "病区名称",eName = "WARD_NAME")
    private String wardName;

    @MetadataDescribe(id= "LHDE0045023",name = "床号",eName = "BED_NO")
    private String bedNo;

    @MetadataDescribe(id= "LHDE0045024",name = "ADT入出转ID",eName = "ADT_CHANGE_INFO_ID")
    private String adtChangeInfoId;

    @MetadataDescribe(id= "LHDE0045025",name = "ADT入出转流水号",eName = "ADT_CHANGE_INFO_NO")
    private String adtChangeInfoNo;

    @MetadataDescribe(id= "LHDE0045026",name = "ADT入出转类型代码",eName = "ADT_CHANGE_TYPE_CODE")
    private String adtChangeTypeCode;

    @MetadataDescribe(id= "LHDE0045027",name = "ADT入出转类型名称",eName = "ADT_CHANGE_TYPE_NAME")
    private String adtChangeTypeName;

    @MetadataDescribe(id= "LHDE0045028",name = "ADT入出转时间",eName = "ADT_CHANGE_TIME")
    private String adtChangeTime;

    @MetadataDescribe(id= "LHDE0045029",name = "转出科室ID",eName = "TRUN_OUT_DEPT_ID")
    private String trunOutDeptId;

    @MetadataDescribe(id= "LHDE0045030",name = "转出科室名称",eName = "TRUN_OUT_DEPT_NAME")
    private String trunOutDeptName;

    @MetadataDescribe(id= "LHDE0045031",name = "转出病区ID",eName = "TRUN_OUT_WARD_ID")
    private String trunOutWardId;

    @MetadataDescribe(id= "LHDE0045032",name = "转出病区名称",eName = "TRUN_OUT_WARD_NAME")
    private String trunOutWardName;

    @MetadataDescribe(id= "LHDE0045033",name = "转出床号",eName = "TRUN_OUT_BED_NO")
    private String trunOutBedNo;

    @MetadataDescribe(id= "LHDE0045034",name = "转入科室ID",eName = "TRUN_IN_DEPT_ID")
    private String trunInDeptId;

    @MetadataDescribe(id= "LHDE0045035",name = "转入科室名称",eName = "TRUN_IN_DEPT_NAME")
    private String trunInDeptName;

    @MetadataDescribe(id= "LHDE0045036",name = "转入病区ID",eName = "TRUN_IN_WARD_ID")
    private String trunInWardId;

    @MetadataDescribe(id= "LHDE0045037",name = "转入病区名称",eName = "TRUN_IN_WARD_NAME")
    private String trunInWardName;

    @MetadataDescribe(id= "LHDE0045038",name = "转入床号",eName = "TRUN_IN_BED_NO")
    private String trunInBedNo;

    @MetadataDescribe(id= "LHDE0045039",name = "源系统代码",eName = "SOURCE_SYSTEM_CODE")
    private String SourceSystemCode;

    @MetadataDescribe(id= "LHDE0045040",name = "录入人ID",eName = "ENTER_OPERA_ID")
    private String enterOperaId;

    @MetadataDescribe(id= "LHDE0045041",name = "录入人姓名",eName = "ENTER_OPERA_NAME")
    private String enterOperaName;

    @MetadataDescribe(id= "LHDE0045042",name = "录入日期时间",eName = "ENTER_DATE_TIME")
    private String enterDateTime;

    @MetadataDescribe(id= "LHDE0045043",name = "修改人ID",eName = "MODIFY_OPERA_ID")
    private String modifyOperaId;

    @MetadataDescribe(id= "LHDE0045044",name = "修改人姓名",eName = "MODIFY_OPERA_NAME")
    private String modifyOperaName;

    @MetadataDescribe(id= "LHDE0045045",name = "修改日期",eName = "MODIFY_DATE_TIME")
    private String modifyDateTime;

    @MetadataDescribe(id= "LHDE0045046",name = "ADT入出转状态代码",eName = "ADT_CHANGE_STATUS_CODE")
    private String adtChangeStatusCode;

    @MetadataDescribe(id= "LHDE0045047",name = "ADT入出转状态名称",eName = "ADT_CHANGE_STATUS_NAME")
    private String adtChangeStatusName;

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

    public String getAdtChangeInfoId() {
        return adtChangeInfoId;
    }

    public void setAdtChangeInfoId(String adtChangeInfoId) {
        this.adtChangeInfoId = adtChangeInfoId;
    }

    public String getAdtChangeInfoNo() {
        return adtChangeInfoNo;
    }

    public void setAdtChangeInfoNo(String adtChangeInfoNo) {
        this.adtChangeInfoNo = adtChangeInfoNo;
    }

    public String getAdtChangeTypeCode() {
        return adtChangeTypeCode;
    }

    public void setAdtChangeTypeCode(String adtChangeTypeCode) {
        this.adtChangeTypeCode = adtChangeTypeCode;
    }

    public String getAdtChangeTypeName() {
        return adtChangeTypeName;
    }

    public void setAdtChangeTypeName(String adtChangeTypeName) {
        this.adtChangeTypeName = adtChangeTypeName;
    }

    public String getAdtChangeTime() {
        return adtChangeTime;
    }

    public void setAdtChangeTime(String adtChangeTime) {
        this.adtChangeTime = adtChangeTime;
    }

    public String getTrunOutDeptId() {
        return trunOutDeptId;
    }

    public void setTrunOutDeptId(String trunOutDeptId) {
        this.trunOutDeptId = trunOutDeptId;
    }

    public String getTrunOutDeptName() {
        return trunOutDeptName;
    }

    public void setTrunOutDeptName(String trunOutDeptName) {
        this.trunOutDeptName = trunOutDeptName;
    }

    public String getTrunOutWardId() {
        return trunOutWardId;
    }

    public void setTrunOutWardId(String trunOutWardId) {
        this.trunOutWardId = trunOutWardId;
    }

    public String getTrunOutWardName() {
        return trunOutWardName;
    }

    public void setTrunOutWardName(String trunOutWardName) {
        this.trunOutWardName = trunOutWardName;
    }

    public String getTrunOutBedNo() {
        return trunOutBedNo;
    }

    public void setTrunOutBedNo(String trunOutBedNo) {
        this.trunOutBedNo = trunOutBedNo;
    }

    public String getTrunInDeptId() {
        return trunInDeptId;
    }

    public void setTrunInDeptId(String trunInDeptId) {
        this.trunInDeptId = trunInDeptId;
    }

    public String getTrunInDeptName() {
        return trunInDeptName;
    }

    public void setTrunInDeptName(String trunInDeptName) {
        this.trunInDeptName = trunInDeptName;
    }

    public String getTrunInWardId() {
        return trunInWardId;
    }

    public void setTrunInWardId(String trunInWardId) {
        this.trunInWardId = trunInWardId;
    }

    public String getTrunInWardName() {
        return trunInWardName;
    }

    public void setTrunInWardName(String trunInWardName) {
        this.trunInWardName = trunInWardName;
    }

    public String getTrunInBedNo() {
        return trunInBedNo;
    }

    public void setTrunInBedNo(String trunInBedNo) {
        this.trunInBedNo = trunInBedNo;
    }

    public String getSourceSystemCode() {
        return SourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        SourceSystemCode = sourceSystemCode;
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

    public String getAdtChangeStatusCode() {
        return adtChangeStatusCode;
    }

    public void setAdtChangeStatusCode(String adtChangeStatusCode) {
        this.adtChangeStatusCode = adtChangeStatusCode;
    }

    public String getAdtChangeStatusName() {
        return adtChangeStatusName;
    }

    public void setAdtChangeStatusName(String adtChangeStatusName) {
        this.adtChangeStatusName = adtChangeStatusName;
    }
}
