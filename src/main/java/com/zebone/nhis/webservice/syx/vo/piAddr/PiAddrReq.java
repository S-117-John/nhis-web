package com.zebone.nhis.webservice.syx.vo.piAddr;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="request")
public class PiAddrReq {

	@XmlElement(name="head")
	private HeadReq headReq;
	
	@XmlElement(name="address")
	private AddrReq AddrReq;

	public HeadReq getHeadReq() {
		return headReq;
	}

	public void setHeadReq(HeadReq headReq) {
		this.headReq = headReq;
	}

	public AddrReq getAddrReq() {
		return AddrReq;
	}

	public void setAddrReq(AddrReq addrReq) {
		AddrReq = addrReq;
	}
	
}
