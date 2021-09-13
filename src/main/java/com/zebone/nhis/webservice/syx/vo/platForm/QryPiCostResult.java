package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="result")
public class QryPiCostResult {
	@XmlElement(name="id")
	private String id;
	
	@XmlElement(name="text")
	private String text;
	
	@XmlElement(name="requestId")
	private String requestId;
	
	@XmlElement(name="requestTime")
	private String requestTime;
	
	@XmlElement(name="subject")
	private QrypiCostResSub costSubject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}

	public QrypiCostResSub getCostSubject() {
		return costSubject;
	}

	public void setCostSubject(QrypiCostResSub costSubject) {
		this.costSubject = costSubject;
	}
	
	
}
