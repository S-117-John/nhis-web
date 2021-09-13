package com.zebone.platform.framework.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="PUSHRESPONSE")
public class SysPushResponse {

	@XmlElement(name="RESPONSEDATA")
	private SysResponseData responseData;

	public SysResponseData getResponseData() {
		return responseData;
	}

	public void setResponseData(SysResponseData responseData) {
		this.responseData = responseData;
	}
}
