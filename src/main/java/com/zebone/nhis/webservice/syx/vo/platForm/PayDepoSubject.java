package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subject")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayDepoSubject {
	
	@XmlElement(name="req")
	private PayDepoReq payDepoReq;

	public PayDepoReq getPayDepoReq() {
		return payDepoReq;
	}

	public void setPayDepoReq(PayDepoReq payDepoReq) {
		this.payDepoReq = payDepoReq;
	}
}
