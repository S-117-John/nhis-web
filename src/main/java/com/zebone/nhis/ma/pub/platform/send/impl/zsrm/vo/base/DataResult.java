package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class DataResult {
    private String code;
    private String codeHp;
    private String name;
    private String unit;
    private String spec;
    private String price;
    public void setCode(String code) {
        this.code = code;
    }
    public String getCode() {
        return code;
    }

    public void setCodeHp(String codeHp) {
        this.codeHp = codeHp;
    }
    public String getCodeHp() {
        return codeHp;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getUnit() {
        return unit;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }
    public String getSpec() {
        return spec;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    public String getPrice() {
        return price;
    }
}
