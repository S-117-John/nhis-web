package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class CreateACardResSubject {

    @XmlElement(name="res")
    private CreateACardRes res;

	public CreateACardRes getRes() {
		return res;
	}

	public void setRes(CreateACardRes res) {
		this.res = res;
	}

	
}
