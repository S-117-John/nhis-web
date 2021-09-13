package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

public class Numerator {
    private String value;
    private String unit;

    public Numerator() {
    }

    public Numerator(String value, String unit) {
        this.value = value;
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
