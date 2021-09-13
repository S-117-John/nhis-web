package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="req")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReqPiInfo {
	//住院号
	@XmlElement(name="ipSeqnoText")
	private String ipSeqnoText;
	
	//患者姓名
	@XmlElement(name="patientName")
	private String patientName;
	
	//身份证号
	@XmlElement(name="userCardId")
	private String userCardId;

	public String getIpSeqnoText() {
		return ipSeqnoText;
	}

	public void setIpSeqnoText(String ipSeqnoText) {
		this.ipSeqnoText = ipSeqnoText;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getUserCardId() {
		return userCardId;
	}

	public void setUserCardId(String userCardId) {
		this.userCardId = userCardId;
	}
	
	
}
