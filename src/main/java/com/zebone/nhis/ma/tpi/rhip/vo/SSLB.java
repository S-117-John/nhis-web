package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 门诊病历-手术
 * @author chengjia
 *
 */
@XmlRootElement(name = "SSLB")
@XmlAccessorType(XmlAccessType.FIELD)
public class SSLB {
    @XmlElement(name = "SSLSH")
    protected String sslsh;
    @XmlElement(name = "SSBM")
    protected String ssbm;
    @XmlElement(name = "SSMC")
    protected String ssmc;
	public String getSslsh() {
		return sslsh;
	}
	public void setSslsh(String sslsh) {
		this.sslsh = sslsh;
	}
	public String getSsbm() {
		return ssbm;
	}
	public void setSsbm(String ssbm) {
		this.ssbm = ssbm;
	}
	public String getSsmc() {
		return ssmc;
	}
	public void setSsmc(String ssmc) {
		this.ssmc = ssmc;
	}
    
}
