package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayDepoResSubject {
	
	@XmlElement(name="res")
	private PayDepoRes payDepoRes;

	public PayDepoRes getPayDepoRes() {
		return payDepoRes;
	}

	public void setPayDepoRes(PayDepoRes payDepoRes) {
		this.payDepoRes = payDepoRes;
	}
	
	
}
