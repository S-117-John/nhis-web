package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.BdExtension;

import java.util.List;

public class Coding {

    private String system;
    private String code;
    private String display;
    private List<BdExtension> extension;

    public Coding(){}
    public Coding(String system, String code, String display) {
        this.system = system;
        this.code = code;
        this.display = display;
    }

    public Coding(String code, String display) {
        this.code = code;
        this.display = display;
    }

    public Coding(String system, String code, String display, List<BdExtension> extension) {
        this.system = system;
        this.code = code;
        this.display = display;
        this.extension = extension;
    }

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
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
}