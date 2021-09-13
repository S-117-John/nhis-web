package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.bl.CnOrderBar;

public class OrdBdPdAttVo extends CnOrderBar {

    private String pkPd;

    private String spec;

    private String pkUnitMin;

    private Double quanprice;

    private Double packSize;

    public String getPkPd() {
        return pkPd;
    }

    public void setPkPd(String pkPd) {
        this.pkPd = pkPd;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getPkUnitMin() {
        return pkUnitMin;
    }

    public void setPkUnitMin(String pkUnitMin) {
        this.pkUnitMin = pkUnitMin;
    }

    public Double getQuanprice() {
        return quanprice;
    }

    public void setQuanprice(Double quanprice) {
        this.quanprice = quanprice;
    }

    public Double getPackSize() {
        return packSize;
    }

    public void setPackSize(Double packSize) {
        this.packSize = packSize;
    }
}
