package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlElement;

public class CriticalXmlCust {
	private CriticalXmlRecver[] recver;

	public CriticalXmlRecver[] getCriticalXmlRecver() {
		return recver;
	}
	@XmlElement(name="recver")
	public void setCriticalXmlRecver(CriticalXmlRecver[] recver) {
		this.recver = recver;
	}
}
