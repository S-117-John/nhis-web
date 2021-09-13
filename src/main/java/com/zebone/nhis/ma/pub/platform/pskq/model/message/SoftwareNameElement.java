package com.zebone.nhis.ma.pub.platform.pskq.model.message;

public class SoftwareNameElement {

    private String code;

    private String name;

    public SoftwareNameElement() {
    }

    public SoftwareNameElement(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
