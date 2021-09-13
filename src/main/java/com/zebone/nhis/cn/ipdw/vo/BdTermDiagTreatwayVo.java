package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.base.bd.code.BdTermDiagTreatway;

public class BdTermDiagTreatwayVo extends BdTermDiagTreatway {
    private String amt50;
    private String amt200;

    public String getAmt50() {
        return amt50;
    }

    public void setAmt50(String amt50) {
        this.amt50 = amt50;
    }

    public String getAmt200() {
        return amt200;
    }

    public void setAmt200(String amt200) {
        this.amt200 = amt200;
    }
}
