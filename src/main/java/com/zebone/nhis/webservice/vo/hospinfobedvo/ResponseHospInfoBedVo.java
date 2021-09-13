package com.zebone.nhis.webservice.vo.hospinfobedvo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "res")
public class ResponseHospInfoBedVo {
	private String status;
	private String desc;
	private String errorMessage;
	private HospInfoBedData hospInfoBedData;
	
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
	public HospInfoBedData getHospInfoBedData() {
		return hospInfoBedData;
	}
	public void setHospInfoBedData(HospInfoBedData hospInfoBedData) {
		this.hospInfoBedData = hospInfoBedData;
	}

}
