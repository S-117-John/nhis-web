package com.zebone.nhis.ma.pub.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Prescribedetail")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresDtVo {
    @XmlElement(name = "Id")
    private String presNo;

    @XmlElement(name = "No")
    private String no;

    @XmlElement(name = "GranuleId")
    private String code;

    @XmlElement(name = "GranuleName")
    private String name;

    @XmlElement(name = "DoseHerb")
    private double doseHerb;

    @XmlElement(name = "Equivalent")
    private double val;

    @XmlElement(name = "Dose")
    private double quan;

    @XmlElement(name = "Price")
    private double amount;

    public String getPresNo() {
        return presNo;
    }

    public void setPresNo(String presNo) {
        this.presNo = presNo;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDoseHerb() {
        return doseHerb;
    }

    public void setDoseHerb(double doseHerb) {
        this.doseHerb = doseHerb;
    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }

    public double getQuan() {
        return quan;
    }

    public void setQuan(double quan) {
        this.quan = quan;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
