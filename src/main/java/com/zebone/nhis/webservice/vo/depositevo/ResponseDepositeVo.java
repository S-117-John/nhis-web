package com.zebone.nhis.webservice.vo.depositevo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.zebone.nhis.webservice.vo.deptvo.DeptData;

@XmlRootElement(name = "status")
public class ResponseDepositeVo {

	private String status;
	private String desc;
	private String errorMessage;
	private DepositeData datalist;

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
	public DepositeData getDatalist() {
		return datalist;
	}

	public void setDatalist(DepositeData datalist) {
		this.datalist = datalist;
	}

}
