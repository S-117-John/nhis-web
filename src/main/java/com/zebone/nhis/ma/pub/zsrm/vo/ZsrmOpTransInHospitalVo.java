package com.zebone.nhis.ma.pub.zsrm.vo;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * 门急诊转入院转化Vo
 */
@Table(value="zy_SyncMzPatInfo")
public class ZsrmOpTransInHospitalVo {
    /**
     * 门诊病人ID
     */
    @Field(value = "patient_id_mz")
    private String patientIdMz;

    /**
     * 患者门诊号（住院号）
     */
    @Field(value = "hisOutPatientId")
    private String hisOutPatientId;

    /**
     * 门诊就诊次数
     */
    @Field(value = "hisOutPatientTimes")
    private Integer hisOutPatientTimes;

    /**
     * 门诊科室编码
     */
    @Field(value = "hisWardCode")
    private String hisWardCode;

    /**
     * 门诊诊断编号
     */
    @Field(value = "diagnosisCode")
    private String diagnosisCode;

    /**
     * 门诊诊断名称
     */
    @Field(value = "diagnosisName")
    private String diagnosisName;

    /**
     * 状态	0申请，1.办理入院，9删除
     */
    @Field(value = "status")
    private String status;

    /**
     * 子诊断编号
     */
    @Field(value = "childDiagnosisCode")
    private String childDiagnosisCode;

    /**
     * 子诊断名称
     */
    @Field(value = "childDiagnosisName")
    private String childDiagenosisName;

    /**
     * 当前住址
     */
    @Field(value = "address")
    private String address;

    /**
     * 入院方式 	1.步行 2.推床 3. 担架 4. 轮椅 5. 床边 6.其他
     */
    @Field(value = "admissionMethod")
    private String admissionMethod;

    /**
     * 申请入院时间 	格式：yyyy-MM-dd HH:mm:ss
     */
    @Field(value = "applyAdmissionTime")
    private String applyAdmissionTime;

    /**
     * 申请入院方式	1.今日入院 2.择期入院
     */
    @Field(value = "applyAdmissionType")
    private String applyAdmissionType;

    /**
     * 开单科室编号
     */
    @Field(value = "applyDepartCode")
    private String applyDepartCode;

    /**
     * 开单科室名称
     */
    @Field(value = "applyDepartName")
    private String applyDepartName;

    /**
     * 开单医生编号
     */
    @Field(value = "applyDoctorCode")
    private String applyDoctorCode;

    /**
     * 开单医生姓名
     */
    @Field(value = "applyDoctorName")
    private String applyDoctorName;

    /**
     * 开单时间 	格式：yyyy-MM-dd HH:mm:ss
     */
    @Field(value = "applyTime")
    private String applyTime;

    /**
     * 联系人电话
     */
    @Field(value = "contactTel")
    private String contactTel;

    /**
     * 门诊诊断
     */
    @Field(value = "diagnosis")
    private String diagnosis;

    /**
     * 患者紧急联系人
     */
    @Field(value = "emergencyContact")
    private String emergencyContact;

    /**
     * 入院单号
     */
    @Field(value = "hisOrderId")
    private String hisOrderId;

    /**
     * 所属机构编号
     */
    @Field(value = "hospitalId")
    private String hospitalId;

    /**
     * 是否监护床 	0.否 1.是
     */
    @Field(value = "isGuard")
    private String isGurad;

    /**
     * 是否隔离	0.否 1.是
     */
    @Field(value = "isIsolation")
    private String isIsolation;

    /**
     * 入院医疗组
     */
    @Field(value = "medicalGroup")
    private String medicalGroup;

    /**
     * 患者年龄
     */
    @Field(value = "patientAge")
    private String patientAge;

    /**
     * 患者生日
     */
    @Field(value = "patientBirth")
    private String patientBirth;

    /**
     * 身份证号
     */
    @Field(value = "patientIdCard")
    private String patientIdCard;

    /**
     * 患者等级名称
     */
    @Field(value = "patientLevelValue")
    private String patientLevelValue;

    /**
     * 患者姓名
     */
    @Field(value = "patientName")
    private String patientName;

    /**
     * 患者性别 	1.女 2. 男
     */
    @Field(value = "patientSex")
    private String patientSex;

    /**
     * 患者来源	1急诊   2 门诊
     */
    @Field(value = "patientSource")
    private String patientSource;

    /**
     * 患者手机号码
     */
    @Field(value = "patientTel")
    private String patientTel;

    /**
     * 患者分类等级名称
     */
    @Field(value = "patientTypeValue")
    private String patientTypeValue;

    /**
     * 入院申请单处理方式 	1.医院处理 2.预约平台处理
     */
    @Field(value = "processMethod")
    private String processMethod;

    /**
     * 备注
     */
    @Field(value = "remark")
    private String remark;

    /**
     * 申请床号
     */
    @Field(value = "requestBedNum")
    private String requestBedNum;

    /**
     * 申请入院病区编码
     */
    @Field(value = "requestDepartCode")
    private String requestDepartCode;

    /**
     * 申请入院病区名称
     */
    @Field(value = "requestDepartName")
    private String requestDepartName;

    /**
     * 患者住院号
     */
    @Field(value = "hisInPatientOn")
    private String hisInPatientOn;

    /**
     * 入院病区编码
     */
    @Field(value = "departCode")
    private String departCode;

    /**
     * 入院病区名称
     */
    @Field(value = "departName")
    private String departName;

    /**
     * 时间戳
     */
    @Field(value = "ts")
    private String ts;

    /**
     * 转区标记	0-不转 1-转区
     */
    @Field(value = "lendFlag")
    private String lendFlag;

    public String getPatientIdMz() {
        return patientIdMz;
    }

    public void setPatientIdMz(String patientIdMz) {
        this.patientIdMz = patientIdMz;
    }

    public String getHisOutPatientId() {
        return hisOutPatientId;
    }

    public void setHisOutPatientId(String hisOutPatientId) {
        this.hisOutPatientId = hisOutPatientId;
    }

    public Integer getHisOutPatientTimes() {
        return hisOutPatientTimes;
    }

    public void setHisOutPatientTimes(Integer hisOutPatientTimes) {
        this.hisOutPatientTimes = hisOutPatientTimes;
    }

    public String getHisWardCode() {
        return hisWardCode;
    }

    public void setHisWardCode(String hisWardCode) {
        this.hisWardCode = hisWardCode;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getChildDiagnosisCode() {
        return childDiagnosisCode;
    }

    public void setChildDiagnosisCode(String childDiagnosisCode) {
        this.childDiagnosisCode = childDiagnosisCode;
    }

    public String getChildDiagenosisName() {
        return childDiagenosisName;
    }

    public void setChildDiagenosisName(String childDiagenosisName) {
        this.childDiagenosisName = childDiagenosisName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAdmissionMethod() {
        return admissionMethod;
    }

    public void setAdmissionMethod(String admissionMethod) {
        this.admissionMethod = admissionMethod;
    }


    public String getApplyAdmissionType() {
        return applyAdmissionType;
    }

    public void setApplyAdmissionType(String applyAdmissionType) {
        this.applyAdmissionType = applyAdmissionType;
    }

    public String getApplyDepartCode() {
        return applyDepartCode;
    }

    public void setApplyDepartCode(String applyDepartCode) {
        this.applyDepartCode = applyDepartCode;
    }

    public String getApplyDepartName() {
        return applyDepartName;
    }

    public void setApplyDepartName(String applyDepartName) {
        this.applyDepartName = applyDepartName;
    }

    public String getApplyDoctorCode() {
        return applyDoctorCode;
    }

    public void setApplyDoctorCode(String applyDoctorCode) {
        this.applyDoctorCode = applyDoctorCode;
    }

    public String getApplyDoctorName() {
        return applyDoctorName;
    }

    public void setApplyDoctorName(String applyDoctorName) {
        this.applyDoctorName = applyDoctorName;
    }


    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getHisOrderId() {
        return hisOrderId;
    }

    public void setHisOrderId(String hisOrderId) {
        this.hisOrderId = hisOrderId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getIsGurad() {
        return isGurad;
    }

    public void setIsGurad(String isGurad) {
        this.isGurad = isGurad;
    }

    public String getIsIsolation() {
        return isIsolation;
    }

    public void setIsIsolation(String isIsolation) {
        this.isIsolation = isIsolation;
    }

    public String getMedicalGroup() {
        return medicalGroup;
    }

    public void setMedicalGroup(String medicalGroup) {
        this.medicalGroup = medicalGroup;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }


    public String getPatientIdCard() {
        return patientIdCard;
    }

    public void setPatientIdCard(String patientIdCard) {
        this.patientIdCard = patientIdCard;
    }

    public String getPatientLevelValue() {
        return patientLevelValue;
    }

    public void setPatientLevelValue(String patientLevelValue) {
        this.patientLevelValue = patientLevelValue;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getPatientSource() {
        return patientSource;
    }

    public void setPatientSource(String patientSource) {
        this.patientSource = patientSource;
    }

    public String getPatientTel() {
        return patientTel;
    }

    public void setPatientTel(String patientTel) {
        this.patientTel = patientTel;
    }

    public String getPatientTypeValue() {
        return patientTypeValue;
    }

    public void setPatientTypeValue(String patientTypeValue) {
        this.patientTypeValue = patientTypeValue;
    }

    public String getProcessMethod() {
        return processMethod;
    }

    public void setProcessMethod(String processMethod) {
        this.processMethod = processMethod;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestBedNum() {
        return requestBedNum;
    }

    public void setRequestBedNum(String requestBedNum) {
        this.requestBedNum = requestBedNum;
    }

    public String getRequestDepartCode() {
        return requestDepartCode;
    }

    public void setRequestDepartCode(String requestDepartCode) {
        this.requestDepartCode = requestDepartCode;
    }

    public String getRequestDepartName() {
        return requestDepartName;
    }

    public void setRequestDepartName(String requestDepartName) {
        this.requestDepartName = requestDepartName;
    }

    public String getHisInPatientOn() {
        return hisInPatientOn;
    }

    public void setHisInPatientOn(String hisInPatientOn) {
        this.hisInPatientOn = hisInPatientOn;
    }

    public String getDepartCode() {
        return departCode;
    }

    public void setDepartCode(String departCode) {
        this.departCode = departCode;
    }

    public String getDepartName() {
        return departName;
    }

    public void setDepartName(String departName) {
        this.departName = departName;
    }


    public String getLendFlag() {
        return lendFlag;
    }

    public void setLendFlag(String lendFlag) {
        this.lendFlag = lendFlag;
    }

    public String getApplyAdmissionTime() {
        return applyAdmissionTime;
    }

    public void setApplyAdmissionTime(String applyAdmissionTime) {
        this.applyAdmissionTime = applyAdmissionTime;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getPatientBirth() {
        return patientBirth;
    }

    public void setPatientBirth(String patientBirth) {
        this.patientBirth = patientBirth;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
