package com.zebone.nhis.ma.pub.sd.vo.cmrInsu;

public class CiReqCanlStVo {

    /**
     * 结算流水号
     */
    private String settleSerialNum;

    /**
     * 就诊流水号
     */
    private String medicalNum;

    /**
     * 单据号
     */
    private String billNum;

    /**
     * 退费日期
     */
    private String revokeDate;

    /**
     * 经办人
     */
    private String updateBy;

    /**
     * 是否保存处方标志
     */
    private String isRetainedFlg;

    public String getSettleSerialNum() {
        return settleSerialNum;
    }

    public void setSettleSerialNum(String settleSerialNum) {
        this.settleSerialNum = settleSerialNum;
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

    public String getRevokeDate() {
        return revokeDate;
    }

    public void setRevokeDate(String revokeDate) {
        this.revokeDate = revokeDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getIsRetainedFlg() {
        return isRetainedFlg;
    }

    public void setIsRetainedFlg(String isRetainedFlg) {
        this.isRetainedFlg = isRetainedFlg;
    }
}
