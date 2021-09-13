package com.zebone.nhis.webservice.syx.vo.scmhr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Sender")
public class Sender {
	@XmlElement(name="systemId")
	private String systemId;
	@XmlElement(name="systemName")
	private String systemName;
	@XmlElement(name="senderId")
	private String senderId;
	@XmlElement(name="sendername")
	private String sendername;

	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}

	public String getSystemId() {
		return systemId;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSendername(String sendername) {
		this.sendername = sendername;
	}

	public String getSendername() {
		return sendername;
	}
}
