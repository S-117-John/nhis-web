package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PayDetailInfoResSubject {

    @XmlElement(name="res")
    private PayDetailInfoResSubjectRes res;

	public PayDetailInfoResSubjectRes getRes() {
		return res;
	}

	public void setRes(PayDetailInfoResSubjectRes res) {
		this.res = res;
	}

	
}
