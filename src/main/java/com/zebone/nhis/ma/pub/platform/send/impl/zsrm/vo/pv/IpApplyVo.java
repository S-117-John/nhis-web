package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;

public class IpApplyVo extends PhResource {

    /** 当前住址 */
    private String address;
    /** 入院方式 01 自行 02 救护车 03外院转入 99 其他*/
    private String admissionMethod;
    /** 收治选择 1.仅本科室 2.无收治限制*/
    private String admissionOptions;
    /** 申请入院时间 格式：yyyy-MM-dd*/
    private String applyAdmissionTime;
    /** 开单科室编号*/
    private String applyDepartCode;
    /** 开单科室名称*/
    private String applyDepartName;
    /**开单医生工号 */
    private String applyDoctorJobNum;

    /** 开单医生姓名*/
    private String applyDoctorName;
    /** 开单时间 格式：yyyy-MM-dd HH:mm:ss */
    private String applyTime;
    /** 紧急联系人电话*/
    private String contactTel;
    /**入院诊断编号 */
    private String diagnosisCode;
    /** 入院诊断名称*/
    private String diagnosisName;
    /** 患者紧急联系人*/
    private String emergencyContact;
    /** 患者门诊号*/
    private String hisOutPatientId;
    /** 患者门诊号*/
    private String hisOutPatientTimes;
    /**his患者id */
    private String hisPatientId;

    /**是否监护床 */
    private String isGuard;
    /** 是否隔离*/
    private String isIsolation;
    /**患者年龄  */
    private String patientAge;

    /**  患者生日 格式：yyyy-MM-dd */
    private String patientBirth;
    /**身份证号  */
    private String patientIdCard;
    /** 患者等级编号 */
    private String patientLevelNo;
    /** 患者姓名 */
    private String patientName;
    /** 患者性别编码 */
    private String patientSex;
    /** 患者来源 */
    private String patientSource;

    /**  患者手机号码*/
    private String patientTel;
    /**  患者分类编号*/
    private String patientTypeNo;
    /**  备注*/
    private String remark;
    /** 申请床号 */
    private String requestBedNum;
    /** 申请入院病区编码 */
    private String requestDepartCode;
    /** 申请入院病区名称*/
    private String requestDepartName;
    /** 新冠肺炎检测*/
    private String covid19Check;
    /** 新冠肺炎检测结果*/
    private String covid19Result;
    /** 新冠肺炎检测日期*/
    private String covid19Date;

    private String hisOrderId;

    private String requestWardCode;

    private String requestWardName;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHisOrderId() {
        return hisOrderId;
    }

    public void setHisOrderId(String hisOrderId) {
        this.hisOrderId = hisOrderId;
    }

    public String getCovid19Check() {
        return covid19Check;
    }

    public void setCovid19Check(String covid19Check) {
        this.covid19Check = covid19Check;
    }

    public String getCovid19Result() {
        return covid19Result;
    }

    public void setCovid19Result(String covid19Result) {
        this.covid19Result = covid19Result;
    }

    public String getCovid19Date() {
        return covid19Date;
    }

    public void setCovid19Date(String covid19Date) {
        this.covid19Date = covid19Date;
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

    public String getAdmissionOptions() {
        return admissionOptions;
    }

    public void setAdmissionOptions(String admissionOptions) {
        this.admissionOptions = admissionOptions;
    }

    public String getApplyAdmissionTime() {
        return applyAdmissionTime;
    }

    public void setApplyAdmissionTime(String applyAdmissionTime) {
        this.applyAdmissionTime = applyAdmissionTime;
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

    public String getApplyDoctorJobNum() {
        return applyDoctorJobNum;
    }

    public void setApplyDoctorJobNum(String applyDoctorJobNum) {
        this.applyDoctorJobNum = applyDoctorJobNum;
    }

    public String getApplyDoctorName() {
        return applyDoctorName;
    }

    public void setApplyDoctorName(String applyDoctorName) {
        this.applyDoctorName = applyDoctorName;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getContactTel() {
        return contactTel;
    }

    public void setContactTel(String contactTel) {
        this.contactTel = contactTel;
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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getHisOutPatientId() {
        return hisOutPatientId;
    }

    public void setHisOutPatientId(String hisOutPatientId) {
        this.hisOutPatientId = hisOutPatientId;
    }

    public String getHisOutPatientTimes() {
        return hisOutPatientTimes;
    }

    public void setHisOutPatientTimes(String hisOutPatientTimes) {
        this.hisOutPatientTimes = hisOutPatientTimes;
    }

    public String getHisPatientId() {
        return hisPatientId;
    }

    public void setHisPatientId(String hisPatientId) {
        this.hisPatientId = hisPatientId;
    }

    public String getIsGuard() {
        return isGuard;
    }

    public void setIsGuard(String isGuard) {
        this.isGuard = isGuard;
    }

    public String getIsIsolation() {
        return isIsolation;
    }

    public void setIsIsolation(String isIsolation) {
        this.isIsolation = isIsolation;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public void setPatientAge(String patientAge) {
        this.patientAge = patientAge;
    }

    public String getPatientBirth() {
        return patientBirth;
    }

    public void setPatientBirth(String patientBirth) {
        this.patientBirth = patientBirth;
    }

    public String getPatientIdCard() {
        return patientIdCard;
    }

    public void setPatientIdCard(String patientIdCard) {
        this.patientIdCard = patientIdCard;
    }

    public String getPatientLevelNo() {
        return patientLevelNo;
    }

    public void setPatientLevelNo(String patientLevelNo) {
        this.patientLevelNo = patientLevelNo;
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

    public String getPatientTypeNo() {
        return patientTypeNo;
    }

    public void setPatientTypeNo(String patientTypeNo) {
        this.patientTypeNo = patientTypeNo;
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

    public String getRequestWardCode() {
        return requestWardCode;
    }

    public void setRequestWardCode(String requestWardCode) {
        this.requestWardCode = requestWardCode;
    }

    public String getRequestWardName() {
        return requestWardName;
    }

    public void setRequestWardName(String requestWardName) {
        this.requestWardName = requestWardName;
    }
}
