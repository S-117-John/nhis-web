package com.zebone.nhis.compay.ins.zsrm.vo;

import java.util.List;

public class InsQgybSettleList {
    /**
     * 收费信息
     */
    private InsQgybSetInfo setlinfo;
    /**
     * 基金支付信息
     */
    private List<InsQgybPayInfo> payinfo;
    /**
     * 门诊特慢病诊断信息
     */
    private List<InsQgybOpspdiseinfo> opspdiseinfo;
    /**
     * 住院诊断信息
     */
    private List<InsQgybDiseinfo> diseinfo;
    /**
     * 收费项目信息
     */
    private List<InsQgybIteminfo> iteminfo;
    /**
     * 手术信息
     */
    private List<InsQgybOprnInfo> oprninfo;
    /**
     * 重症监护信息
     */
    private List<InsQgybIcuInfo> icuinfo;

    public InsQgybSettleList(InsQgybSetInfo setlinfo, List<InsQgybPayInfo> payinfo, List<InsQgybOpspdiseinfo> opspdiseinfo, List<InsQgybDiseinfo> diseinfo, List<InsQgybIteminfo> iteminfo, List<InsQgybOprnInfo> oprninfo, List<InsQgybIcuInfo> icuinfo) {
        this.setlinfo = setlinfo;
        this.payinfo = payinfo;
        this.opspdiseinfo = opspdiseinfo;
        this.diseinfo = diseinfo;
        this.iteminfo = iteminfo;
        this.oprninfo = oprninfo;
        this.icuinfo = icuinfo;
    }

    public InsQgybSettleList() {
    }

    public InsQgybSetInfo getSetlinfo() {
        return setlinfo;
    }

    public void setSetlinfo(InsQgybSetInfo setlinfo) {
        this.setlinfo = setlinfo;
    }

    public List<InsQgybPayInfo> getPayinfo() {
        return payinfo;
    }

    public void setPayinfo(List<InsQgybPayInfo> payinfo) {
        this.payinfo = payinfo;
    }

    public List<InsQgybOpspdiseinfo> getOpspdiseinfo() {
        return opspdiseinfo;
    }

    public void setOpspdiseinfo(List<InsQgybOpspdiseinfo> opspdiseinfo) {
        this.opspdiseinfo = opspdiseinfo;
    }

    public List<InsQgybDiseinfo> getDiseinfo() {
        return diseinfo;
    }

    public void setDiseinfo(List<InsQgybDiseinfo> diseinfo) {
        this.diseinfo = diseinfo;
    }

    public List<InsQgybIteminfo> getIteminfo() {
        return iteminfo;
    }

    public void setIteminfo(List<InsQgybIteminfo> iteminfo) {
        this.iteminfo = iteminfo;
    }

    public List<InsQgybOprnInfo> getOprninfo() {
        return oprninfo;
    }

    public void setOprninfo(List<InsQgybOprnInfo> oprninfo) {
        this.oprninfo = oprninfo;
    }

    public List<InsQgybIcuInfo> getIcuinfo() {
        return icuinfo;
    }

    public void setIcuinfo(List<InsQgybIcuInfo> icuinfo) {
        this.icuinfo = icuinfo;
    }
}
