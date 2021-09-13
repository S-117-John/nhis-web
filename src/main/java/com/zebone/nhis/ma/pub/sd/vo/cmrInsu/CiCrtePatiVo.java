package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

import java.util.List;

public class CiCrtePatiVo {
    /**
     *商保公司代码
     */
    private String professionalCode;

    /**
     *商保公司名称
     */
    private String professionalName;

    /**
     *就诊流水号
     *是
     */
    private String medicalNum;

    /**
     *医疗类别
     * 是
     */
    private String medicalType;

    /**
     *就诊时间
     *是
     */
    private String treatDate;

    /**
     *就诊科室编码
     */
    private String treatDeptCode;

    /**
     *就诊科室名称
     */
    private String treatDeptName;

    /**
     *病区
     */
    private String endemicArea;

    /**
     *床位号
     */
    private String bunkId;

    /**
     *住院号
     */
    private String inHospitalNum;

    /**
     *入院诊断医生编号
     */
    private String inHosDoctorCode;

    /**
     *入院诊断医生姓名
     */
    private String inHosDoctorName;

    /**
     *入院临床诊断
     *是
     */
    private String inHosClinicalDiagnosis;

    /**
     *入院诊断 ICD 列表
     */
    private List<CiIcdVo> inHosDiagnosisList;

    /**
     *证件类型
     *是
     */
    private String credentialType;

    /**
     *证件号码
     *是
     */
    private String credentialNum;

    /**
     *姓名
     *是
     *
     */
    private String name;

    /**
     *性别
     *是
     */
    private String gender;

    /**
     *出生日期
     *是
     */
    private String birthday;

    /**
     *证件到期日
     */
    private String idDueDay;

    /**
     *民族
     */
    private String race;

    /**
     *联系地址
     */
    private String homeAddress;

    /**
     *患者现状
     * 数据字典:
     */
    private String clientStatus;

    /**
     *联系人姓名
     */
    private String linkmanName;

    /**
     *联系人电话
     */
    private String linkmanMobile;

    /**
     *电子邮箱
     */
    private String email;
    /**
     *监护人证件类型
     */
    private String guardianIdType;

    /**
     *监护人证件号码
     */
    private String guardianIdNo;
    /**
     *监护人姓名
     */
    private String guardianName;

    /**
     *监护人性别
     */
    private String guardianGender;
    /**
     *监护人出生年月
     */
    private String guardianBirthday;

    /**
     *监护人证件到期日
     */
    private String guardianIDDueDay;

    /**
     *经办人
     */
    private String updateBy;

    /**
     *备注
     */
    private String remark;

    public String getProfessionalCode() {
        return professionalCode;
    }

    public void setProfessionalCode(String professionalCode) {
        this.professionalCode = professionalCode;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum;
    }

    public String getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(String medicalType) {
        this.medicalType = medicalType;
    }

    public String getTreatDate() {
        return treatDate;
    }

    public void setTreatDate(String treatDate) {
        this.treatDate = treatDate;
    }

    public String getTreatDeptCode() {
        return treatDeptCode;
    }

    public void setTreatDeptCode(String treatDeptCode) {
        this.treatDeptCode = treatDeptCode;
    }

    public String getTreatDeptName() {
        return treatDeptName;
    }

    public void setTreatDeptName(String treatDeptName) {
        this.treatDeptName = treatDeptName;
    }

    public String getEndemicArea() {
        return endemicArea;
    }

    public void setEndemicArea(String endemicArea) {
        this.endemicArea = endemicArea;
    }

    public String getBunkId() {
        return bunkId;
    }

    public void setBunkId(String bunkId) {
        this.bunkId = bunkId;
    }

    public String getInHospitalNum() {
        return inHospitalNum;
    }

    public void setInHospitalNum(String inHospitalNum) {
        this.inHospitalNum = inHospitalNum;
    }

    public String getInHosDoctorCode() {
        return inHosDoctorCode;
    }

    public void setInHosDoctorCode(String inHosDoctorCode) {
        this.inHosDoctorCode = inHosDoctorCode;
    }

    public String getInHosDoctorName() {
        return inHosDoctorName;
    }

    public void setInHosDoctorName(String inHosDoctorName) {
        this.inHosDoctorName = inHosDoctorName;
    }

    public String getInHosClinicalDiagnosis() {
        return inHosClinicalDiagnosis;
    }

    public void setInHosClinicalDiagnosis(String inHosClinicalDiagnosis) {
        this.inHosClinicalDiagnosis = inHosClinicalDiagnosis;
    }

    public List<CiIcdVo> getInHosDiagnosisList() {
        return inHosDiagnosisList;
    }

    public void setInHosDiagnosisList(List<CiIcdVo> inHosDiagnosisList) {
        this.inHosDiagnosisList = inHosDiagnosisList;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getCredentialNum() {
        return credentialNum;
    }

    public void setCredentialNum(String credentialNum) {
        this.credentialNum = credentialNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdDueDay() {
        return idDueDay;
    }

    public void setIdDueDay(String idDueDay) {
        this.idDueDay = idDueDay;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getClientStatus() {
        return clientStatus;
    }

    public void setClientStatus(String clientStatus) {
        this.clientStatus = clientStatus;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanMobile() {
        return linkmanMobile;
    }

    public void setLinkmanMobile(String linkmanMobile) {
        this.linkmanMobile = linkmanMobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGuardianIdType() {
        return guardianIdType;
    }

    public void setGuardianIdType(String guardianIdType) {
        this.guardianIdType = guardianIdType;
    }

    public String getGuardianIdNo() {
        return guardianIdNo;
    }

    public void setGuardianIdNo(String guardianIdNo) {
        this.guardianIdNo = guardianIdNo;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianGender() {
        return guardianGender;
    }

    public void setGuardianGender(String guardianGender) {
        this.guardianGender = guardianGender;
    }

    public String getGuardianBirthday() {
        return guardianBirthday;
    }

    public void setGuardianBirthday(String guardianBirthday) {
        this.guardianBirthday = guardianBirthday;
    }

    public String getGuardianIDDueDay() {
        return guardianIDDueDay;
    }

    public void setGuardianIDDueDay(String guardianIDDueDay) {
        this.guardianIDDueDay = guardianIDDueDay;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
