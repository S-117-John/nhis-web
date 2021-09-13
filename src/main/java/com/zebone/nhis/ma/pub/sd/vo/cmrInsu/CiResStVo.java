package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiResStVo {

    /**
     * 就诊流水号
     */
    private String medicalNum;

    /**
     * 单据号
     */
    private String billNum;

    /**
     * 结算流水号
     */
    private String settleSerialNum;

    /**
     * 结算日期
     */
    private String settleDate;

    /**
     * 医疗费总额
     */
    private Double medicalCost;

    /**
     * 符合商保费用
     */
    private Double commercialInsuranceCost;

    /**
     * 商保基金支付
     */
    private Double sbFundPay;
    /**
     * 个人自付金额
     */
    private Double personalAmount;

    /**
     * 累计补偿金额
     */
    private Double accumCompAmount;
    /**
     * 商业保险住院累计补偿金额
     */
    private Double insuranceHospitalAccumulatePayment;

    /**
     * 商业保险门诊累计补偿金额
     */
    private Double insuranceOutpatient;

    /**
     * 剩余额度
     */
    private Double residualAmount;

    /**
     * 结算方式
     */
    private String indemnitySign;

    /**
     * 转非即时结算原因
     */
    private String nonImmediateReason;

    /**
     * 责任免除标识
     */
    private String waiverSign;

    /**
     * 责任免除原因
     */
    private String waiverReason;

    /**
     * 计算过程
     */
    private String calculateInformation;

    /**
     * 备注
     */
    private String remark;

    /**
     * 提示信息
     */
    private String promptInformation;

    /**
     * 理赔单文件名
     */
    private String policyFileName;

    public String getMedicalNum() {
        return medicalNum;
    }

    public void setMedicalNum(String medicalNum) {
        this.medicalNum = medicalNum;
    }

    public String getBillNum() {
        return billNum;
    }

    public void setBillNum(String billNum) {
        this.billNum = billNum;
    }

    public String getSettleSerialNum() {
        return settleSerialNum;
    }

    public void setSettleSerialNum(String settleSerialNum) {
        this.settleSerialNum = settleSerialNum;
    }

    public String getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(String settleDate) {
        this.settleDate = settleDate;
    }

    public Double getMedicalCost() {
        return medicalCost;
    }

    public void setMedicalCost(Double medicalCost) {
        this.medicalCost = medicalCost;
    }

    public Double getCommercialInsuranceCost() {
        return commercialInsuranceCost;
    }

    public void setCommercialInsuranceCost(Double commercialInsuranceCost) {
        this.commercialInsuranceCost = commercialInsuranceCost;
    }

    public Double getSbFundPay() {
        return sbFundPay;
    }

    public void setSbFundPay(Double sbFundPay) {
        this.sbFundPay = sbFundPay;
    }

    public Double getPersonalAmount() {
        return personalAmount;
    }

    public void setPersonalAmount(Double personalAmount) {
        this.personalAmount = personalAmount;
    }

    public Double getAccumCompAmount() {
        return accumCompAmount;
    }

    public void setAccumCompAmount(Double accumCompAmount) {
        this.accumCompAmount = accumCompAmount;
    }

    public Double getInsuranceHospitalAccumulatePayment() {
        return insuranceHospitalAccumulatePayment;
    }

    public void setInsuranceHospitalAccumulatePayment(Double insuranceHospitalAccumulatePayment) {
        this.insuranceHospitalAccumulatePayment = insuranceHospitalAccumulatePayment;
    }

    public Double getInsuranceOutpatient() {
        return insuranceOutpatient;
    }

    public void setInsuranceOutpatient(Double insuranceOutpatient) {
        this.insuranceOutpatient = insuranceOutpatient;
    }

    public Double getResidualAmount() {
        return residualAmount;
    }

    public void setResidualAmount(Double residualAmount) {
        this.residualAmount = residualAmount;
    }

    public String getIndemnitySign() {
        return indemnitySign;
    }

    public void setIndemnitySign(String indemnitySign) {
        this.indemnitySign = indemnitySign;
    }

    public String getNonImmediateReason() {
        return nonImmediateReason;
    }

    public void setNonImmediateReason(String nonImmediateReason) {
        this.nonImmediateReason = nonImmediateReason;
    }

    public String getWaiverSign() {
        return waiverSign;
    }

    public void setWaiverSign(String waiverSign) {
        this.waiverSign = waiverSign;
    }

    public String getWaiverReason() {
        return waiverReason;
    }

    public void setWaiverReason(String waiverReason) {
        this.waiverReason = waiverReason;
    }

    public String getCalculateInformation() {
        return calculateInformation;
    }

    public void setCalculateInformation(String calculateInformation) {
        this.calculateInformation = calculateInformation;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPromptInformation() {
        return promptInformation;
    }

    public void setPromptInformation(String promptInformation) {
        this.promptInformation = promptInformation;
    }

    public String getPolicyFileName() {
        return policyFileName;
    }

    public void setPolicyFileName(String policyFileName) {
        this.policyFileName = policyFileName;
    }
}
