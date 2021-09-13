package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "subject")
public class PiAddrInfoResSubject {

    @XmlElement(name="res")
    private PiAddrInfoRes res;

	public PiAddrInfoRes getRes() {
		return res;
	}

	public void setRes(PiAddrInfoRes res) {
		this.res = res;
	}

	
}
