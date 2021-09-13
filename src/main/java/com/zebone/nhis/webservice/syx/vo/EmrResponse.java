package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * 响应
 * @author chengjia
 *
 */
@XmlRootElement(name = "CALLRESPONSE")
public class EmrResponse {
	
	@XmlElement(name="RESPONSEDATA")
	private EmrRespData respData;

	@XmlTransient
	public EmrRespData getRespData() {
		return respData;
	}

	public void setRespData(EmrRespData respData) {
		this.respData = respData;
	}
	
	
}
