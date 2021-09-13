package com.zebone.nhis.webservice.zsrm.vo.pack;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="PRESCRIBEDETAIL")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZsrmHerbPresDtVo {
    @XmlElement(name = "PRESCRIBE_NO")
    private String presNo;

    @XmlElement(name = "GRANULE_NO")
    private String no;

    @XmlElement(name = "GRANULE_ID")
    private String code;

    @XmlElement(name = "PARTICLE_NAME")
    private String name;

    @XmlElement(name = "EQUIVALENT")
    private double val;

    @XmlElement(name = "PARTICLE_DOSE")
    private double quan;

    @XmlElement(name = "DECOCTION_DOSE>")
    private double qwe;

    @XmlElement(name = "PARTICLE_PRICE")
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
