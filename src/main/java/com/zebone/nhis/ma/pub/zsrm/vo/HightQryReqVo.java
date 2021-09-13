package com.zebone.nhis.ma.pub.zsrm.vo;

import org.codehaus.jackson.annotate.JsonProperty;

public class HightQryReqVo {
    @JsonProperty("BAR_CODE")
    private String barcode;

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
