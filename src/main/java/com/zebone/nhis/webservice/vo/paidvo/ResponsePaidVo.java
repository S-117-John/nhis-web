package com.zebone.nhis.webservice.vo.paidvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "res")
public class ResponsePaidVo {
	private String status;
	private String desc;
	private String errorMessage;
	private PaidData datalist;
	
	@XmlElement(name = "status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@XmlElement(name = "desc")
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	@XmlElement(name = "errorMessage")
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	@XmlElement(name = "data")
	public PaidData getDatalist() {
		return datalist;
	}
	public void setDatalist(PaidData datalist) {
		this.datalist = datalist;
	}
}
