package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PayOrderResSubject {
	
	@XmlElement(name = "res")
	private PayOrderRes res;

	public PayOrderRes getRes() {
		return res;
	}

	public void setRes(PayOrderRes res) {
		this.res = res;
	}
	
	
	
}
