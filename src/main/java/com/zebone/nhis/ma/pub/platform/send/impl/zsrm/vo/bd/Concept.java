package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Property;

import java.util.List;

public class Concept {
    private String code;
    private String display;
    private List<Property> property;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public List<Property> getProperty() {
        return property;
    }

    public void setProperty(List<Property> property) {
        this.property = property;
    }

    public Concept() {
    }

    public Concept(String code, String display, List<Property> property) {
        this.code = code;
        this.display = display;
        this.property = property;
    }
}
