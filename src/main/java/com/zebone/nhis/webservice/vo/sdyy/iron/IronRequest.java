package com.zebone.nhis.webservice.vo.sdyy.iron;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class IronRequest {
	@XmlElement(name="head")
	private IronReqHead reqHead;
	
	@XmlElement(name="body")
	private IronReqBody reqBody;

	public IronReqHead getReqHead() {
		return reqHead;
	}

	public void setReqHead(IronReqHead reqHead) {
		this.reqHead = reqHead;
	}

	public IronReqBody getReqBody() {
		return reqBody;
	}

	public void setReqBody(IronReqBody reqBody) {
		this.reqBody = reqBody;
	}
}
