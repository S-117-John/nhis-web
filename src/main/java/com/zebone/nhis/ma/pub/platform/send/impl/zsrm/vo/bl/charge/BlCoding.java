package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

public class BlCoding {

    private String system;
    private String code;
    private String display;
    private String value;

    public BlCoding(){}
    public BlCoding(String system, String code, String display) {
        this.system = system;
        this.code = code;
        this.display = display;
    }

    public BlCoding(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return display;
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
}
