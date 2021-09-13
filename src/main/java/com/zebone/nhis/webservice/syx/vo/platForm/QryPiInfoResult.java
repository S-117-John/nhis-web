package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.syx.vo.ResPiInfoResVo;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="result")
public  class QryPiInfoResult  {
	@XmlElement(name="id")
	private String id;
	
	@XmlElement(name="text")
	private String text;
	
	@XmlElement(name="requestId")
	private String requestId;
	
	@XmlElement(name="requestTime")
	private String requestTime;
	
	@XmlElement(name="subject")
	private ResPiInfoResVo infoResVo ;

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

	public ResPiInfoResVo getInfoResVo() {
		return infoResVo;
	}

	public void setInfoResVo(ResPiInfoResVo infoResVo) {
		this.infoResVo = infoResVo;
	}

}
