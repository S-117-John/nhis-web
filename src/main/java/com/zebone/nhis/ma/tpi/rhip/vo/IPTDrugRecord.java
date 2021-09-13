package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IPT_DrugRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IPTDrugRecord {
    @XmlElement(name = "HLRQ")
    protected String hlrq;
    @XmlElement(name = "HLYWMC")
    protected String hlywmc;
    @XmlElement(name = "HLYWJL")
    protected String hlywjl;
    @XmlElement(name = "HLLC")
    protected String hllc;
    @XmlElement(name = "HLLX")
    protected String hllx;
	public String getHlrq() {
		return hlrq;
	}
	public void setHlrq(String hlrq) {
		this.hlrq = hlrq;
	}
	public String getHlywmc() {
		return hlywmc;
	}
	public void setHlywmc(String hlywmc) {
		this.hlywmc = hlywmc;
	}
	public String getHlywjl() {
		return hlywjl;
	}
	public void setHlywjl(String hlywjl) {
		this.hlywjl = hlywjl;
	}
	public String getHllc() {
		return hllc;
	}
	public void setHllc(String hllc) {
		this.hllc = hllc;
	}
	public String getHllx() {
		return hllx;
	}
	public void setHllx(String hllx) {
		this.hllx = hllx;
	}
    
    
}
