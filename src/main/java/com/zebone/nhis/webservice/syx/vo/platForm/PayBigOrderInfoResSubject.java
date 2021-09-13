package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PayBigOrderInfoResSubject {
	
	@XmlElement(name = "res")
	private PayBigOrderResInfo res;

	public PayBigOrderResInfo getRes() {
		return res;
	}

	public void setRes(PayBigOrderResInfo res) {
		this.res = res;
	}
}
