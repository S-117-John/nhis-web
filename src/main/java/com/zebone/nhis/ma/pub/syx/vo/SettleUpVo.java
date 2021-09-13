package com.zebone.nhis.ma.pub.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="request")
public class SettleUpVo {

	@XmlElement(name="reqType")
	private String reqType;

	@XmlElement(name="data")
	private SettleUpReq settleUpReq;

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public SettleUpReq getSettleUpReq() {
		return settleUpReq;
	}

	public void setSettleUpReq(SettleUpReq settleUpReq) {
		this.settleUpReq = settleUpReq;
	}
}
