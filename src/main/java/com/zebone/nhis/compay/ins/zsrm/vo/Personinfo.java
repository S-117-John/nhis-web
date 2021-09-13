package com.zebone.nhis.compay.ins.zsrm.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="personinfo")
public class Personinfo {
	
	@XmlElement(name="aac001") 
	private String aac001;
	@XmlElement(name="aac003") 
	private String aac003;
	@XmlElement(name="aac004") 
	private String aac004;

	@XmlElement(name="aac013") 
	private String aac013;
	@XmlElement(name="bka005") 
	private String bka005;
	@XmlElement(name="aac002") 
	private String aac002;
	@XmlElement(name="aae005") 
	private String aae005;
	@XmlElement(name="aac006") 
	private String aac006;
	@XmlElement(name="baa027") 
	private String baa027;
	
	@XmlElement(name="aab001") 
	private String aab001;
	@XmlElement(name="bka008") 
	private String bka008;
	public String getAac001() {
		return aac001;
	}
	public void setAac001(String aac001) {
		this.aac001 = aac001;
	}
	public String getAac003() {
		return aac003;
	}
	public void setAac003(String aac003) {
		this.aac003 = aac003;
	}
	public String getAac004() {
		return aac004;
	}
	public void setAac004(String aac004) {
		this.aac004 = aac004;
	}
	public String getAac013() {
		return aac013;
	}
	public void setAac013(String aac013) {
		this.aac013 = aac013;
	}
	public String getBka005() {
		return bka005;
	}
	public void setBka005(String bka005) {
		this.bka005 = bka005;
	}
	public String getAac002() {
		return aac002;
	}
	public void setAac002(String aac002) {
		this.aac002 = aac002;
	}
	public String getAae005() {
		return aae005;
	}
	public void setAae005(String aae005) {
		this.aae005 = aae005;
	}
	public String getAac006() {
		return aac006;
	}
	public void setAac006(String aac006) {
		this.aac006 = aac006;
	}
	public String getBaa027() {
		return baa027;
	}
	public void setBaa027(String baa027) {
		this.baa027 = baa027;
	}
	public String getAab001() {
		return aab001;
	}
	public void setAab001(String aab001) {
		this.aab001 = aab001;
	}
	public String getBka008() {
		return bka008;
	}
	public void setBka008(String bka008) {
		this.bka008 = bka008;
	}

	
	
}
