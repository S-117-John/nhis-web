package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "resquest")
public class CriticalXmlResquest {
	private CriticalXmlHead head;
	private CriticalXmlBody body;
	
	@XmlElement(name="head")
	public CriticalXmlHead getHead() {
		return head;
	}
	public void setHead(CriticalXmlHead head) {
		this.head = head;
	}
	@XmlElement(name="body")
	public CriticalXmlBody getBody() {
		return body;
	}
	public void setBody(CriticalXmlBody body) {
		this.body = body;
	}
}
