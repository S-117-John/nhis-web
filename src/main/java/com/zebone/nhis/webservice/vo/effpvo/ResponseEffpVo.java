package com.zebone.nhis.webservice.vo.effpvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="res")
public class ResponseEffpVo {
	private String status;
	private String desc;
	private String errorMessage;
	private EffpvData datalist;
	
	@XmlElement(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name="desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@XmlElement(name="errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@XmlElement(name="data")
	public EffpvData getDatalist() {
		return datalist;
	}
	public void setDatalist(EffpvData datalist) {
		this.datalist = datalist;
	}
	
	
}
