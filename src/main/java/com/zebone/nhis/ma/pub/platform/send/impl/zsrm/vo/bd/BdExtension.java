package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.ValuePeriod;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BdExtension {

    private String url;
    private BdCodeableConcept valueCodeableConcept;
    private String valueString;
    private ValuePeriod valuePeriod;
    private Coding valueCoding;
    private String valueDate;
    private Boolean valueBoolean;
    private BigDecimal valueDecimal;
    private String valueCode;
    private List<BdCoding> valueQuantity;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SS")
    private Date valueDateTime;

    public BdExtension() {
    }

    public BdExtension(String url, String valueString, BdCodeableConcept valueCodeableConcept) {
        this.url = url;
        this.valueCodeableConcept = valueCodeableConcept;
        this.valueString = valueString;
    }

    public BdExtension(String url,Boolean valueBoolean) {
        this.url = url;
        this.valueBoolean = valueBoolean;
    }

    public BdExtension(String url, BigDecimal valueDecimal) {
        this.url = url;
        this.valueDecimal = valueDecimal;
    }

    public BdExtension(String url, Coding valueCoding) {
        this.url = url;
        this.valueCoding = valueCoding;
    }

    public BdExtension(String url, BdCodeableConcept valueCodeableConcept, String valueString, ValuePeriod valuePeriod, Coding valueCoding, String valueDate, Boolean valueBoolean, String valueCode) {
        this.url = url;
        this.valueCodeableConcept = valueCodeableConcept;
        this.valueString = valueString;
        this.valuePeriod = valuePeriod;
        this.valueCoding = valueCoding;
        this.valueDate = valueDate;
        this.valueBoolean = valueBoolean;
        this.valueCode = valueCode;
    }

    public BdExtension(String url, ValuePeriod valuePeriod) {
        this.url = url;
        this.valuePeriod = valuePeriod;
    }

    public BdExtension(String url, List<BdCoding> valueQuantity) {
        this.url = url;
        this.valueQuantity = valueQuantity;
    }

    public BdExtension(String url, Date valueDateTime) {
        this.url = url;
        this.valueDateTime = valueDateTime;
    }

    public BdExtension(String url) {
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

    public BdCodeableConcept getValueCodeableConcept() {
        return valueCodeableConcept;
    }

    public void setValueCodeableConcept(BdCodeableConcept valueCodeableConcept) {
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

    public BigDecimal getValueDecimal() {
        return valueDecimal;
    }

    public void setValueDecimal(BigDecimal valueDecimal) {
        this.valueDecimal = valueDecimal;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public List<BdCoding> getValueQuantity() {
        return valueQuantity;
    }

    public void setValueQuantity(List<BdCoding> valueQuantity) {
        this.valueQuantity = valueQuantity;
    }

    public Date getValueDateTime() {
        return valueDateTime;
    }

    public void setValueDateTime(Date valueDateTime) {
        this.valueDateTime = valueDateTime;
    }
}