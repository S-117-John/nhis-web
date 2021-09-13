package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Identifiers {
    public Identifiers() {
    }

    public Identifiers(String type, String system, String value, String use, String text) {
        this.type = type;
        this.system = system;
        this.value = value;
        this.use = use;
        this.text = text;
    }

    public Identifiers(String value) {
        this.value = value;
    }

    private String type;
    private String system;
    private String value;
    private String use;
    private String text;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
