package com.zebone.nhis.compay.ins.zsrm.vo;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;

/**
 * 收费项目信息
 */
public class InsQgybIteminfo {
    /**
     * 医疗收费项目
     */
    @JSONField(name = "med_chrgitm")
    private String medChrgitm;
    /**
     * 金额
     */
    private BigDecimal amt;
    /**
     * 甲类费用合计
     */
    @JSONField(name = "claa_sumfee")
    private BigDecimal claaSumfee;
    /**
     * 乙类金额
     */
    @JSONField(name = "clab_amt")
    private BigDecimal clabAmt;
    /**
     * 全自费金额
     */
    @JSONField(name = "fulamt_ownpay_amt")
    private BigDecimal fulamtOwnpayAmt;
    /**
     * 其他金额
     */
    @JSONField(name = "oth_amt")
    private BigDecimal othAmt;

    public String getMedChrgitm() {
        return medChrgitm;
    }

    public void setMedChrgitm(String medChrgitm) {
        this.medChrgitm = medChrgitm;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public BigDecimal getClaaSumfee() {
        return claaSumfee;
    }

    public void setClaaSumfee(BigDecimal claaSumfee) {
        this.claaSumfee = claaSumfee;
    }

    public BigDecimal getClabAmt() {
        return clabAmt;
    }

    public void setClabAmt(BigDecimal clabAmt) {
        this.clabAmt = clabAmt;
    }

    public BigDecimal getFulamtOwnpayAmt() {
        return fulamtOwnpayAmt;
    }

    public void setFulamtOwnpayAmt(BigDecimal fulamtOwnpayAmt) {
        this.fulamtOwnpayAmt = fulamtOwnpayAmt;
    }

    public BigDecimal getOthAmt() {
        return othAmt;
    }

    public void setOthAmt(BigDecimal othAmt) {
        this.othAmt = othAmt;
    }
}
