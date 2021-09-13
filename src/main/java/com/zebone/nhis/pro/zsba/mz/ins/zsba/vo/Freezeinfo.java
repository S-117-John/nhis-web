package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="freezeinfo")
public class Freezeinfo {

	@XmlElement(name="aac031") 
	private String aac031;
	@XmlElement(name="aad006") 
	private String aad006;
	@XmlElement(name="aaa157") 
	private String aaa157;
	@XmlElement(name="bka408") 
	private String bka408;
	public String getAac031() {
		return aac031;
	}
	public void setAac031(String aac031) {
		this.aac031 = aac031;
	}
	public String getAad006() {
		return aad006;
	}
	public void setAad006(String aad006) {
		this.aad006 = aad006;
	}
	public String getAaa157() {
		return aaa157;
	}
	public void setAaa157(String aaa157) {
		this.aaa157 = aaa157;
	}
	public String getBka408() {
		return bka408;
	}
	public void setBka408(String bka408) {
		this.bka408 = bka408;
	}
	
	
}
