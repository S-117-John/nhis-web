package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiResClaimVo {

    /**
     * 最终赔付金额
     */
    private String mngFinalPay;

    /**
     * 结案日期
     */
    private String caseEndDate;

    /**
     * 案件状态
     */
    private String caseStatus;

    /**
     * 理赔确认书
     */
    private String confirmation;

    /**
     * 结算流水号
     */
    private String settleSerialNum;

    public String getSettleSerialNum() {
        return settleSerialNum;
    }

    public void setSettleSerialNum(String settleSerialNum) {
        this.settleSerialNum = settleSerialNum;
    }

    public String getMngFinalPay() {
        return mngFinalPay;
    }

    public void setMngFinalPay(String mngFinalPay) {
        this.mngFinalPay = mngFinalPay;
    }

    public String getCaseEndDate() {
        return caseEndDate;
    }

    public void setCaseEndDate(String caseEndDate) {
        this.caseEndDate = caseEndDate;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }
}
