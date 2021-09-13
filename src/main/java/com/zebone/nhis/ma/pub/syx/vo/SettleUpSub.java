package com.zebone.nhis.ma.pub.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="subject")
public class SettleUpSub {
	@XmlElement(name="req")
	private SettleUpReq reqVo;

	public SettleUpReq getReqVo() {
		return reqVo;
	}

	public void setReqVo(SettleUpReq reqVo) {
		this.reqVo = reqVo;
	}
	
	
}
