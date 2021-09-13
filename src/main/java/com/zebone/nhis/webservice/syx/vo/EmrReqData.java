package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name="REQUEST")
public class EmrReqData {
	
	@XmlElement(name="REQUEST")
    private EmrRequestData emrRequestData;

	@XmlTransient
	public EmrRequestData getEmrRequestData() {
		return emrRequestData;
	}

	public void setEmrRequestData(EmrRequestData emrRequestData) {
		this.emrRequestData = emrRequestData;
	}
	
}
