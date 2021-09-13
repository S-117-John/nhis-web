package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlAccessorType(XmlAccessType.FIELD)  
public class EmrRespData {
	@XmlElement(name="RETURNCODE")
	private String rtnCode;
	@XmlElement(name="RETURNMSG")
	private String rtnMsg;
	@XmlElement(name="RESPONSE")
    private EmrAdmitRecRtn responseData;
	@XmlElement(name="RESPONSE")
    private EmrPatInfoRtn responseDataPatInfo;
	@XmlTransient
	public String getRtnCode() {
		return rtnCode;
	}

	public void setRtnCode(String rtnCode) {
		this.rtnCode = rtnCode;
	}

	@XmlTransient
	public String getRtnMsg() {
		return rtnMsg;
	}

	public void setRtnMsg(String rtnMsg) {
		this.rtnMsg = rtnMsg;
	}

	@XmlTransient
	public EmrAdmitRecRtn getResponseData() {
		return responseData;
	}

	public void setResponseData(EmrAdmitRecRtn responseData) {
		this.responseData = responseData;
	}

	public EmrPatInfoRtn getResponseDataPatInfo() {
		return responseDataPatInfo;
	}

	public void setResponseDataPatInfo(EmrPatInfoRtn responseDataPatInfo) {
		this.responseDataPatInfo = responseDataPatInfo;
	}
	
}
