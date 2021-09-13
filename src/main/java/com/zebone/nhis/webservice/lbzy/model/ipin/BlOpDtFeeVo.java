package com.zebone.nhis.webservice.lbzy.model.ipin;

import com.zebone.nhis.common.module.bl.opcg.BlOpDt;

public class BlOpDtFeeVo extends BlOpDt {

    private String presNo;
    private String comNo;

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getComNo() {
        return comNo;
    }

    public void setComNo(String comNo) {
        this.comNo = comNo;
    }
}
