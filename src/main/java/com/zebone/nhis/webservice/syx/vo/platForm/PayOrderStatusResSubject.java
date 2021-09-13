package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PayOrderStatusResSubject {

    @XmlElement(name="res")
    private PayOrderStatusRes res;

	public PayOrderStatusRes getRes() {
		return res;
	}

	public void setRes(PayOrderStatusRes res) {
		this.res = res;
	}

	
}
