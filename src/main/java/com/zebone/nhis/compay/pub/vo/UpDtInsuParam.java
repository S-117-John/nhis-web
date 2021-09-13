package com.zebone.nhis.compay.pub.vo;

import com.zebone.nhis.common.module.compay.ins.qgyb.InsQgybCg;

import java.util.List;
import java.util.Map;

public class UpDtInsuParam {
    /**
     * pkPv
     */
    private String pkPv;

    /**
     * 费用主键集合
     */
    private List<String> pkCgList;

    /**
     * 是否住院(true:住院业务，false:门诊业务)
     */
    private boolean flagIp;

    private List<InsQgybCg> insuCgList;

    private List<Map<String, Object>> hisCgList;

    private String flagInsu;

    public List<Map<String, Object>> getHisCgList() {
        return hisCgList;
    }

    public void setHisCgList(List<Map<String, Object>> hisCgList) {
        this.hisCgList = hisCgList;
    }

    public String getFlagInsu() {
        return flagInsu;
    }

    public void setFlagInsu(String flagInsu) {
        this.flagInsu = flagInsu;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public List<String> getPkCgList() {
        return pkCgList;
    }

    public void setPkCgList(List<String> pkCgList) {
        this.pkCgList = pkCgList;
    }

    public boolean isFlagIp() {
        return flagIp;
    }

    public void setFlagIp(boolean flagIp) {
        this.flagIp = flagIp;
    }

    public List<InsQgybCg> getInsuCgList() {
        return insuCgList;
    }

    public void setInsuCgList(List<InsQgybCg> insuCgList) {
        this.insuCgList = insuCgList;
    }
}
