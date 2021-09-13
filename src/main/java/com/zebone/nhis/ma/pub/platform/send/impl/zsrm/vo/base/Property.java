package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Property {
    private String code;
    private String valueString;
    private Boolean valueBoolean;
    private Coding valueCoding;
    private String valueCode;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private Date valueDateTime;

    public Property() {
    }

    public Property(String code, String valueString, Boolean valueBoolean, Coding valueCoding, String valueCode, Date valueDateTime) {
        this.code = code;
        this.valueString = valueString;
        this.valueBoolean = valueBoolean;
        this.valueCoding = valueCoding;
        this.valueCode = valueCode;
        this.valueDateTime = valueDateTime;
    }

    public Property(String code, Boolean valueBoolean, Coding valueCoding, String valueCode) {
        this.code = code;
        this.valueBoolean = valueBoolean;
        this.valueCoding = valueCoding;
        this.valueCode = valueCode;
    }

    public Property(String code, Coding valueCoding) {
        this.code = code;
        this.valueCoding = valueCoding;
    }

    public Property(String code, Coding valueCoding, String valueCode) {
        this.code = code;
        this.valueCoding = valueCoding;
        this.valueCode = valueCode;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Property(String code, Date valueDateTime) {
        this.code = code;
        this.valueDateTime = valueDateTime;
    }

    public Property(String code, Boolean valueBoolean) {
        this.code = code;
        this.valueBoolean = valueBoolean;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public Coding getValueCoding() {
        return valueCoding;
    }

    public void setValueCoding(Coding valueCoding) {
        this.valueCoding = valueCoding;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public Date getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Date valueDateTime) {
        this.valueDateTime = valueDateTime;
    }
}
