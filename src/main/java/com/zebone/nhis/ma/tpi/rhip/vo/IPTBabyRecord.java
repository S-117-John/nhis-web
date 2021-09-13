package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "IPT_BabyRecord")
@XmlAccessorType(XmlAccessType.FIELD)
public class IPTBabyRecord {
	 @XmlElement(name = "FMJG")
     protected String fmjg;
     @XmlElement(name = "YEQJCS")
     protected String yeqjcs;
     @XmlElement(name = "YEQJCGCS")
     protected String yeqjcgcs;
     @XmlElement(name = "YETZ")
     protected String yetz;
     @XmlElement(name = "YEZG")
     protected String yezg;
     @XmlElement(name = "YEHX")
     protected String yehx;
     @XmlElement(name = "YEXB")
     protected String yexb;
     
	public String getFmjg() {
		return fmjg;
	}
	public void setFmjg(String fmjg) {
		this.fmjg = fmjg;
	}
	public String getYeqjcs() {
		return yeqjcs;
	}
	public void setYeqjcs(String yeqjcs) {
		this.yeqjcs = yeqjcs;
	}
	public String getYeqjcgcs() {
		return yeqjcgcs;
	}
	public void setYeqjcgcs(String yeqjcgcs) {
		this.yeqjcgcs = yeqjcgcs;
	}
	public String getYetz() {
		return yetz;
	}
	public void setYetz(String yetz) {
		this.yetz = yetz;
	}
	public String getYezg() {
		return yezg;
	}
	public void setYezg(String yezg) {
		this.yezg = yezg;
	}
	public String getYehx() {
		return yehx;
	}
	public void setYehx(String yehx) {
		this.yehx = yehx;
	}
	public String getYexb() {
		return yexb;
	}
	public void setYexb(String yexb) {
		this.yexb = yexb;
	}
     
     

}
