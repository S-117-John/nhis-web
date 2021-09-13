package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="req")
@XmlAccessorType( XmlAccessType.FIELD)
public class QryPiCostReq {
	@XmlElement(name="ipSeqnoText")
	private String ipSeqnoText;
	@XmlElement(name="inPatientId")
	private String inPatientId;
	public String getIpSeqnoText() {
		return ipSeqnoText;
	}
	public void setIpSeqnoText(String ipSeqnoText) {
		this.ipSeqnoText = ipSeqnoText;
	}
	public String getInPatientId() {
		return inPatientId;
	}
	public void setInPatientId(String inPatientId) {
		this.inPatientId = inPatientId;
	}
	
	

}
