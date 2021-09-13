package com.zebone.nhis.ma.pub.sd.vo;

public class FollowUpClinicAdviceItemVo {

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 医嘱内容
     */
    private String adviceContents;

    /**
     * 单量
     */
    private String singleAmount;

    /**
     * 总天数
     */
    private String totalDays;

    /**
     * 频率
     */
    private String frequency;

    /**
     * 用法
     */
    private String usages;

    /**
     * 医生嘱托
     */
    private String medicalAdvice;

    /**
     * 执行时间
     */
    private String execTime;

    /**
     * 执行科室
     */
    private String execDept;

    /**
     * 执行性质
     */
    private String execNature;

    /**
     * 执行状态
     * 只上传状态为“已发送”的住院医嘱,
     * 状态有：
     * 3、已发送;
     * 其他状态忽略
     */
    private String execState;

    /**
     * 开嘱医生
     */
    private String adviceDoctor;

    /**
     * 开嘱时间
     */
    private String adviceTime;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 超量说明
     */
    private String excessDesc;

    /**
     * 基本药物
     */
    private String basicDrug;

    /**
     * 医生ID
     */
    private String adviceDoctorId;

    /**
     * 剂量单位
     */
    private String medicineUnit;

    public String getMedicineUnit() {
        return medicineUnit;
    }

    public void setMedicineUnit(String medicineUnit) {
        this.medicineUnit = medicineUnit;
    }

    public String getAdviceDoctorId() {
        return adviceDoctorId;
    }

    public void setAdviceDoctorId(String adviceDoctorId) {
        this.adviceDoctorId = adviceDoctorId;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getAdviceContents() {
        return adviceContents;
    }

    public void setAdviceContents(String adviceContents) {
        this.adviceContents = adviceContents;
    }

    public String getSingleAmount() {
        return singleAmount;
    }

    public void setSingleAmount(String singleAmount) {
        this.singleAmount = singleAmount;
    }

    public String getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(String totalDays) {
        this.totalDays = totalDays;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getUsages() {
        return usages;
    }

    public void setUsages(String usages) {
        this.usages = usages;
    }

    public String getMedicalAdvice() {
        return medicalAdvice;
    }

    public void setMedicalAdvice(String medicalAdvice) {
        this.medicalAdvice = medicalAdvice;
    }

    public String getExecTime() {
        return execTime;
    }

    public void setExecTime(String execTime) {
        this.execTime = execTime;
    }

    public String getExecDept() {
        return execDept;
    }

    public void setExecDept(String execDept) {
        this.execDept = execDept;
    }

    public String getExecNature() {
        return execNature;
    }

    public void setExecNature(String execNature) {
        this.execNature = execNature;
    }

    public String getExecState() {
        return execState;
    }

    public void setExecState(String execState) {
        this.execState = execState;
    }

    public String getAdviceDoctor() {
        return adviceDoctor;
    }

    public void setAdviceDoctor(String adviceDoctor) {
        this.adviceDoctor = adviceDoctor;
    }

    public String getAdviceTime() {
        return adviceTime;
    }

    public void setAdviceTime(String adviceTime) {
        this.adviceTime = adviceTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getExcessDesc() {
        return excessDesc;
    }

    public void setExcessDesc(String excessDesc) {
        this.excessDesc = excessDesc;
    }

    public String getBasicDrug() {
        return basicDrug;
    }

    public void setBasicDrug(String basicDrug) {
        this.basicDrug = basicDrug;
    }
}
