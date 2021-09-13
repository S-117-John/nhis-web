package com.zebone.nhis.cn.pub.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class OrdBlVo extends CnOrder {

    private String unit;

    private String supply;

    private String freq;

    private String hs;

    private String nameDeptExec;

    private String presNoOrd;

    private double amountCg;//费用——前台显示

    private double amount;//费用--后台统计

    private String flagSettle;//费用标记--前台显示

    private double settle;//费用标记--后台统计

    private double occ;//执行标记--后台统计

    private double refund;//退药标记--后台统计

    private double exSum;//总执行次数--后台统计

    private String flagEx;//执行标记--前台显示

    private String pkSettle;//收费结算主键

    private String dtPaymode;//支付方式

    private String dtTubetype;

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getHs() {
        return hs;
    }

    public void setHs(String hs) {
        this.hs = hs;
    }

    public String getNameDeptExec() {
        return nameDeptExec;
    }

    public void setNameDeptExec(String nameDeptExec) {
        this.nameDeptExec = nameDeptExec;
    }

    public String getPresNoOrd() {
        return presNoOrd;
    }

    public void setPresNoOrd(String presNoOrd) {
        this.presNoOrd = presNoOrd;
    }

    public double getAmountCg() {
        return amountCg;
    }

    public void setAmountCg(double amountCg) {
        this.amountCg = amountCg;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getFlagSettle() {
        return flagSettle;
    }

    public void setFlagSettle(String flagSettle) {
        this.flagSettle = flagSettle;
    }

    public double getSettle() {
        return settle;
    }

    public void setSettle(double settle) {
        this.settle = settle;
    }

    public double getOcc() {
        return occ;
    }

    public void setOcc(double occ) {
        this.occ = occ;
    }

    public double getRefund() {
        return refund;
    }

    public void setRefund(double refund) {
        this.refund = refund;
    }

    public double getExSum() {
        return exSum;
    }

    public void setExSum(double exSum) {
        this.exSum = exSum;
    }

    public String getFlagEx() {
        return flagEx;
    }

    public void setFlagEx(String flagEx) {
        this.flagEx = flagEx;
    }

    public String getPkSettle() {
        return pkSettle;
    }

    public void setPkSettle(String pkSettle) {
        this.pkSettle = pkSettle;
    }

    public String getDtPaymode() {
        return dtPaymode;
    }

    public void setDtPaymode(String dtPaymode) {
        this.dtPaymode = dtPaymode;
    }

    public String getDtTubetype() {
        return dtTubetype;
    }

    public void setDtTubetype(String dtTubetype) {
        this.dtTubetype = dtTubetype;
    }
}
