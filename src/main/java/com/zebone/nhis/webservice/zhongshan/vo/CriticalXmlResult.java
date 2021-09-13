package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "result")
public class CriticalXmlResult {
	private CriticalXmlResponse criticalXmlResponse;
	@XmlElement(name = "response")
	public CriticalXmlResponse getCriticalXmlResponse() {
		return criticalXmlResponse;
	}

	public void setCriticalXmlResponse(CriticalXmlResponse criticalXmlResponse) {
		this.criticalXmlResponse = criticalXmlResponse;
	}
}
