package com.zebone.nhis.webservice.vo.employeevo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "res")
public class ResponseEmployeeVo {
	private String status;
	private String desc;
	private String errorMessage;
	private ResEmployeeVo resEmployeeVo;
	
	@XmlElement(name = "data")
	public ResEmployeeVo getResEmployeeVo() {
		return resEmployeeVo;
	}
	public void setResEmployeeVo(ResEmployeeVo resEmployeeVo) {
		this.resEmployeeVo = resEmployeeVo;
	}
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
	
}
