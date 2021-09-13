package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="DOCDATA")
public class EmrReqDocData {
	
	@XmlElement(name="EST")
    private EmrReqDocDataEst est;

	@XmlElement(name="BTS")
    private EmrReqDocDataBts bts;
	
	@XmlElement(name="EFF")
    private EmrReqDocDataEff eff;
	
	@XmlTransient
	public EmrReqDocDataBts getBts() {
		return bts;
	}

	public void setBts(EmrReqDocDataBts bts) {
		this.bts = bts;
	}

	@XmlTransient
	public EmrReqDocDataEst getEst() {
		return est;
	}

	public void setEst(EmrReqDocDataEst est) {
		this.est = est;
	}

	@XmlTransient
	public EmrReqDocDataEff getEff() {
		return eff;
	}

	public void setEff(EmrReqDocDataEff eff) {
		this.eff = eff;
	}

	
}
