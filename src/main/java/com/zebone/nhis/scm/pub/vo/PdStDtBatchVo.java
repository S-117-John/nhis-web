package com.zebone.nhis.scm.pub.vo;

import java.io.Serializable;

public class PdStDtBatchVo implements Serializable {

    private String pkPdstdt;

    private String pkPd;

    private Double quanMin;

    public String getPkPdstdt() {
        return pkPdstdt;
    }

    public void setPkPdstdt(String pkPdstdt) {
        this.pkPdstdt = pkPdstdt;
    }

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public Double getQuanMin() {
        return quanMin;
    }

    public void setQuanMin(Double quanMin) {
        this.quanMin = quanMin;
    }
}
