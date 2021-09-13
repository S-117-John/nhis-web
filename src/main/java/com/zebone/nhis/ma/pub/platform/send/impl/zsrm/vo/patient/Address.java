package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;

import java.util.List;

public class Address {
    public Address() {
    }

    public Address(String text, String line, String district, String city, String state, String postalCode) {
        this.text = text;
        this.line = line;
        this.district = district;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
    }

    public Address(String use, String text) {
        this.use = use;
        this.text = text;
    }

    private String use;
    private String text;
    private String line;
    private String district;
    private String city;
    private String state;
    private String postalCode;
    private List<Extension> extension;

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLine() {
        return line;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

}