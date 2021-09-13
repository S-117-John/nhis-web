package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExtResult {
	@XmlElement
	private String id = "AA";
	@XmlElement(name = "pk_addr")
	private String pkAddr;
	@XmlElement
	private String text = "处理成功";
	@XmlElement
	private String requestId;
	@XmlElement
	private String requestTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPkAddr() {
		return pkAddr;
	}
	public void setPkAddr(String pkAddr) {
		this.pkAddr = pkAddr;
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
	
	
}
