package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Pt_TrainResult")
@XmlAccessorType(XmlAccessType.FIELD)
public class PtTrainResult {
	@XmlElement(name = "PYJYXMDM", required = true)
    protected String pyjyxmdm;
    @XmlElement(name = "XJPYJG")
    protected String xjpyjg;
    @XmlElement(name = "XJPYLX")
    protected String xjpylx;
    @XmlElement(name = "PYJYXMMC", required = true)
    protected String pyjyxmmc;
    @XmlElement(name = "JGLX")
    protected String jglx;
    @XmlElement(name = "PYJGLSH", required = true)
    protected String pyjglsh;
    
	public String getPyjyxmdm() {
		return pyjyxmdm;
	}
	public void setPyjyxmdm(String pyjyxmdm) {
		this.pyjyxmdm = pyjyxmdm;
	}
	public String getXjpyjg() {
		return xjpyjg;
	}
	public void setXjpyjg(String xjpyjg) {
		this.xjpyjg = xjpyjg;
	}
	public String getXjpylx() {
		return xjpylx;
	}
	public void setXjpylx(String xjpylx) {
		this.xjpylx = xjpylx;
	}
	public String getPyjyxmmc() {
		return pyjyxmmc;
	}
	public void setPyjyxmmc(String pyjyxmmc) {
		this.pyjyxmmc = pyjyxmmc;
	}
	public String getJglx() {
		return jglx;
	}
	public void setJglx(String jglx) {
		this.jglx = jglx;
	}
	public String getPyjglsh() {
		return pyjglsh;
	}
	public void setPyjglsh(String pyjglsh) {
		this.pyjglsh = pyjglsh;
	}
    
    
    
    
}
