package com.zebone.nhis.ma.pub.sd.vo;

import java.util.List;
import java.util.Map;

public class FollowUpPiInfoVo {
    /**
     * 挂号单号
     */
    private String registerNo;
    /**
     * 病人ID
     */
    private String patientId;
    /**
     * 科室名称
     */
    private String deptName;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     * 1:男
     * 2：女
     * 3：未知
     */
    private String gender;
    /**
     * 联系人手机号
     */
    private String telephone;
    /**
     * 年龄
     */
    private String age;
    /**
     * 民族
     */
    private String nation;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 是否已婚：
     * 1：已婚
     * 2：未婚
     * 3: 离异
     * 4：丧偶
     * 5：其他
     */
    private String marry;
    /**
     * 出生日期 格式yyyy-mm-dd
     */
    private String birthday;
    /**
     * 具体可以参照体检报告类型
     * 1：身份证
     * 20：其他
     */
    private String idType;
    /**
     * 证件号码
     */
    private String idNo;
    /**
     * 现住址
     */
    private String presentAddr;
    /**
     * 职业
     */
    private String profession;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 公司电话
     */
    private String companyTel;
    /**
     * 医保号
     */
    private String medicalNo;
    /**
     * 发病时间 格式yyyy-mm-dd
     */
    private String diseaseTime;
    /**
     * 发病地址
     */
    private String diseaseAddr;
    /**
     * 门诊类型
     * 1:急诊
     * 2:普通门诊
     */
    private String clinicType;
    /**
     * 去向
     */
    private String direction;
    /**
     * 监护人
     */
    private String guardian;
    /**
     * 家庭电话
     */
    private String homeTel;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 过敏史
     */
    private String allergyHis;
    /**
     * 基本体征信息，包括(括号内为简称)：
     * 身高(hw_height)
     * 体重(hw_weight)
     * 体温(bt_temperature)
     * 脉搏(bo_pulse)
     * 呼吸(bt_breathing)
     * 血压(bp_systolic),
     * 以json数据的形式保存：
     * {
     * "hw_height":"身高",
     * "hw_weight":"体重",
     * "bt_temperature":"体温",
     * "bo_pulse":"脉搏",
     * "bt_breathing":"呼吸",
     * "bp_systolic":"血压",
     * }
     */
    private String signsInfo;
    /**
     * 费别
     */
    private String costType;
    /**
     * 付款方式
     */
    private String payMode;
    /**
     * 号类, 分三类：
     * 1:普通
     * 2:特诊
     * 3:专家
     */
    private String registerType;
//    /**
//     *入院诊断
//     */
//    private String diagnosis;
    /**
     * 过敏药物
     */
    private String allergyDrug;
    /**
     * 接诊时间
     */
    private String clinicTime;
    /**
     * 诊断
     */
    private List<Map<String, Object>> diagnosis;
    /**
     * 门诊详情
     */
    private List<FollowUpClinicAdviceItemVo> clinicAdviceItem;
    /**
     * 医生信息
     */
    private List<FollowUpAdviceDoctorInfoVo> adviceDoctorInfo;

    public String getClinicTime() {
        return clinicTime;
    }

    public void setClinicTime(String clinicTime) {
        this.clinicTime = clinicTime;
    }

    public List<FollowUpAdviceDoctorInfoVo> getAdviceDoctorInfo() {
        return adviceDoctorInfo;
    }

    public void setAdviceDoctorInfo(List<FollowUpAdviceDoctorInfoVo> adviceDoctorInfo) {
        this.adviceDoctorInfo = adviceDoctorInfo;
    }

    public List<Map<String, Object>> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Map<String, Object>> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<FollowUpClinicAdviceItemVo> getClinicAdviceItem() {
        return clinicAdviceItem;
    }

    public void setClinicAdviceItem(List<FollowUpClinicAdviceItemVo> clinicAdviceItem) {
        this.clinicAdviceItem = clinicAdviceItem;
    }

    public String getRegisterNo() {
        return registerNo;
    }

    public void setRegisterNo(String registerNo) {
        this.registerNo = registerNo;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getNativePlace() {
        return nativePlace;
    }

    public void setNativePlace(String nativePlace) {
        this.nativePlace = nativePlace;
    }

    public String getMarry() {
        return marry;
    }

    public void setMarry(String marry) {
        this.marry = marry;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPresentAddr() {
        return presentAddr;
    }

    public void setPresentAddr(String presentAddr) {
        this.presentAddr = presentAddr;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyTel() {
        return companyTel;
    }

    public void setCompanyTel(String companyTel) {
        this.companyTel = companyTel;
    }

    public String getMedicalNo() {
        return medicalNo;
    }

    public void setMedicalNo(String medicalNo) {
        this.medicalNo = medicalNo;
    }

    public String getDiseaseTime() {
        return diseaseTime;
    }

    public void setDiseaseTime(String diseaseTime) {
        this.diseaseTime = diseaseTime;
    }

    public String getDiseaseAddr() {
        return diseaseAddr;
    }

    public void setDiseaseAddr(String diseaseAddr) {
        this.diseaseAddr = diseaseAddr;
    }

    public String getClinicType() {
        return clinicType;
    }

    public void setClinicType(String clinicType) {
        this.clinicType = clinicType;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getGuardian() {
        return guardian;
    }

    public void setGuardian(String guardian) {
        this.guardian = guardian;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAllergyHis() {
        return allergyHis;
    }

    public void setAllergyHis(String allergyHis) {
        this.allergyHis = allergyHis;
    }

    public String getSignsInfo() {
        return signsInfo;
    }

    public void setSignsInfo(String signsInfo) {
        this.signsInfo = signsInfo;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getRegisterType() {
        return registerType;
    }

    public void setRegisterType(String registerType) {
        this.registerType = registerType;
    }

//    public String getDiagnosis() {
//        return diagnosis;
//    }

//    public void setDiagnosis(String diagnosis) {
//        this.diagnosis = diagnosis;
//    }

    public String getAllergyDrug() {
        return allergyDrug;
    }

    public void setAllergyDrug(String allergyDrug) {
        this.allergyDrug = allergyDrug;
    }
}
