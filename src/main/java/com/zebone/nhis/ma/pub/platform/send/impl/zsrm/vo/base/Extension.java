package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Extension {

    private String url;
    private CodeableConcept valueCodeableConcept;
    private String valueString;
    private ValuePeriod valuePeriod;
    private Coding valueCoding;
    private String valueDate;
    private String valueDatetime;
    private Boolean valueBoolean;
    private Integer valueInteger;

    public Extension() {
    }

    public Extension(String url,String valueString, CodeableConcept valueCodeableConcept) {
        this.url = url;
        this.valueCodeableConcept = valueCodeableConcept;
        this.valueString = valueString;
    }

    public Extension(String url, ValuePeriod valuePeriod) {
        this.url = url;
        this.valuePeriod = valuePeriod;
    }

    public Extension(String url, Boolean valueBoolean) {
        this.url = url;
        this.valueBoolean = valueBoolean;
    }

    public Extension(String url, Integer valueInteger) {
        this.url = url;
        this.valueInteger = valueInteger;
    }

    public Integer getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(Integer valueInteger) {
        this.valueInteger = valueInteger;
    }

    public Extension(String url) {
        this.url = url;
    }

    public Coding getValueCoding() {
        return valueCoding;
    }

    public void setValueCoding(Coding valueCoding) {
        this.valueCoding = valueCoding;
    }

    public ValuePeriod getValuePeriod() {
        return valuePeriod;
    }

    public void setValuePeriod(ValuePeriod valuePeriod) {
        this.valuePeriod = valuePeriod;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }


    public CodeableConcept getValueCodeableConcept() {
        return valueCodeableConcept;
    }

    public void setValueCodeableConcept(CodeableConcept valueCodeableConcept) {
        this.valueCodeableConcept = valueCodeableConcept;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public String getValueDate() {
        return valueDate;
    }

    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public String getValueDatetime() {
        return valueDatetime;
    }

    public void setValueDatetime(String valueDatetime) {
        this.valueDatetime = valueDatetime;
    }
}