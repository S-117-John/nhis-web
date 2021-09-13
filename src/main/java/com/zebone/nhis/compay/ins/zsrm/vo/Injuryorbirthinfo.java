package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="injuryorbirthinfo")
public class Injuryorbirthinfo {
	
	@XmlElement(name="aae031") 
	private String aae031;
	@XmlElement(name="aae030") 
	private String aae030;
	@XmlElement(name="bka042") 
	private String bka042;
	public String getAae031() {
		return aae031;
	}
	public void setAae031(String aae031) {
		this.aae031 = aae031;
	}
	public String getAae030() {
		return aae030;
	}
	public void setAae030(String aae030) {
		this.aae030 = aae030;
	}
	public String getBka042() {
		return bka042;
	}
	public void setBka042(String bka042) {
		this.bka042 = bka042;
	}
	

	
}
