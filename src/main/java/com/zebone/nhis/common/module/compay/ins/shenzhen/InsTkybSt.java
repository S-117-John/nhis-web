package com.zebone.nhis.common.module.compay.ins.shenzhen;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="INS_TKYB_ST")
public class InsTkybSt extends BaseModule {

    @PK
    @Field(value="PK_INSST",id= Field.KeyId.UUID)
    private String pkInsst;

    @Field(value="PK_PV")
    private String pkPv;

    @Field(value="PK_PI")
    private String pkPi;

    @Field(value="PK_SETTLE")
    private String pkSettle;

    @Field(value="MEDICAL_NUM")
    private String medicalNum;

    @Field(value="BILL_NUM")
    private String billNum;

    @Field(value="SETTLE_SERIAL_NUM")
    private String settleSerialNum;

    @Field(value="SETTLE_DATE")
    private Date settleDate;

    @Field(value="MEDICAL_COST")
    private Double medicalCost;

    @Field(value="COMMERCIAL_INSURANCE_COST")
    private Double commercialInsuranceCost;

    @Field(value="SB_FUND_PAY")
    private Double sbFundPay;

    @Field(value="PERSONAL_AMOUNT")
    private Double personalAmount;

    @Field(value="ACCUM_COMP_AMOUNT")
    private Double accumCompAmount;

    @Field(value="INSUR_HOSP_ACCUMULATE_PAYMENT")
    private Double insurHospAccumulatePayment;

    @Field(value="INSURANCE_OUTPATIENT")
    private Double insuranceOutpatient;

    @Field(value="RESIDUAL_AMOUNT")
    private Double residualAmount;

    @Field(value="INDEMNITY_SIGN")
    private String indemnitySign;

    @Field(value="NON_IMMEDIATE_REASON")
    private String nonImmediateReason;

    @Field(value="WAIVER_SIGN")
    private String waiverSign;

    @Field(value="WAIVER_REASON")
    private String waiverReason;

    @Field(value="CALCULATE_INFORMATION")
    private String calculateInformation;

    @Field(value="REMARK")
    private String remark;

    @Field(value="PROMPTINFORMATION")
    private String promptInformation;

    @Field(value="POLICY_FILENAME")
    private String policyFileName;

    @Field(value="MNG_FINAL_PAY")
    private String mngFinalPay;

    @Field(value="CASE_END_DATE")
    private Date caseEndDate;

    @Field(value="CASE_STATUS")
    private String caseStatus;

    @Field(value="CONFIRMATION")
    private byte[] confirmation;

    @Field(value="ACCEPT_FLAG")
    private String acceptFlag;

    public String getPkInsst() {
        return pkInsst;
    }

    public void setPkInsst(String pkInsst) {
        this.pkInsst = pkInsst;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

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

    public Date getSettleDate() {
        return settleDate;
    }

    public void setSettleDate(Date settleDate) {
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

    public Double getInsurHospAccumulatePayment() {
        return insurHospAccumulatePayment;
    }

    public void setInsurHospAccumulatePayment(Double insurHospAccumulatePayment) {
        this.insurHospAccumulatePayment = insurHospAccumulatePayment;
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

    public String getMngFinalPay() {
        return mngFinalPay;
    }

    public void setMngFinalPay(String mngFinalPay) {
        this.mngFinalPay = mngFinalPay;
    }

    public Date getCaseEndDate() {
        return caseEndDate;
    }

    public void setCaseEndDate(Date caseEndDate) {
        this.caseEndDate = caseEndDate;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public byte[] getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(byte[] confirmation) {
        this.confirmation = confirmation;
    }

    public String getAcceptFlag() {
        return acceptFlag;
    }

    public void setAcceptFlag(String acceptFlag) {
        this.acceptFlag = acceptFlag;
    }
}
