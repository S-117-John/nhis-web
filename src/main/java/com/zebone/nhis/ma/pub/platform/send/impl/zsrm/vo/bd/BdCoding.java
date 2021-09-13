package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import java.util.List;

public class BdCoding {

    private String system;
    private String code;
    private String display;
    private List<BdExtension> extension;
    private String value;

    public BdCoding(){}
    public BdCoding(String system, String code, String display) {
        this.system = system;
        this.code = code;
        this.display = display;
    }

    public BdCoding(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public BdCoding(String code, String display, List<BdExtension> extension) {
        this.code = code;
        this.display = display;
        this.extension = extension;
    }

    public BdCoding(String system, String code, String display, List<BdExtension> extension, String value) {
        this.system = system;
        this.code = code;
        this.display = display;
        this.extension = extension;
        this.value = value;
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

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
