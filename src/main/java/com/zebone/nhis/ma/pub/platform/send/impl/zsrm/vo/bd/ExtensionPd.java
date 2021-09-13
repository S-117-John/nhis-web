package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Property;

import java.util.List;

public class ExtensionPd {
    private String url;
    private String valueString;
    private Boolean valueBoolean;
    private String valueCode;
    private Double valueDecimal;
    private Coding valueCoding;

    public Coding getValueCoding() {
        return valueCoding;
    }

    public ExtensionPd(String url, Coding valueCoding) {
        this.url = url;
        this.valueCoding = valueCoding;
    }

    public void setValueCoding(Coding valueCoding) {
        this.valueCoding = valueCoding;
    }

    public ExtensionPd() {
    }

    public ExtensionPd(String url, String valueString) {
        this.url = url;
        this.valueString = valueString;
    }

    public ExtensionPd(String url, String valueString, Boolean valueBoolean) {
        this.url = url;
        this.valueString = valueString;
        this.valueBoolean = valueBoolean;
    }

    public ExtensionPd(String url, String valueString, Boolean valueBoolean, String valueCode) {
        this.url = url;
        this.valueString = valueString;
        this.valueBoolean = valueBoolean;
        this.valueCode = valueCode;
    }

    public ExtensionPd(String url, String valueString, Boolean valueBoolean, String valueCode, Double valueDecimal) {
        this.url = url;
        this.valueString = valueString;
        this.valueBoolean = valueBoolean;
        this.valueCode = valueCode;
        this.valueDecimal = valueDecimal;
    }

    public Double getValueDecimal() {
        return valueDecimal;
    }

    public void setValueDecimal(Double valueDecimal) {
        this.valueDecimal = valueDecimal;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }
}
