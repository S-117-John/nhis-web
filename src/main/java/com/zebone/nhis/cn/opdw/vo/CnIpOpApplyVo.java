package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOpApply;

public class CnIpOpApplyVo extends CnOpApply {
    /**患者就诊状态*/
    private String euPvtype;
    /**是否急*/
    private String flagEmer;
    /***/
    private String flagItc;
    private String dtHpprop;

    public String getEuPvtype() {
        return euPvtype;
    }

    public void setEuPvtype(String euPvtype) {
        this.euPvtype = euPvtype;
    }

    public String getFlagEmer() {
        return flagEmer;
    }

    public void setFlagEmer(String flagEmer) {
        this.flagEmer = flagEmer;
    }

    public String getFlagItc() {
        return flagItc;
    }

    public void setFlagItc(String flagItc) {
        this.flagItc = flagItc;
    }

    public String getDtHpprop() {
        return dtHpprop;
    }

    public void setDtHpprop(String dtHpprop) {
        this.dtHpprop = dtHpprop;
    }
}
