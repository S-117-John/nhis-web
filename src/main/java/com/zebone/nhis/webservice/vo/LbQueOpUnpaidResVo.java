package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueOpUnpaidResVo {
	public RespCommonVo getRespCommon() {
		return respCommon;
	}
	public void setRespCommon(RespCommonVo respCommon) {
		this.respCommon = respCommon;
	}
	public LbQueOpUnpaidResTemVo getItems() {
		return payInfos;
	}
	public void setItems(LbQueOpUnpaidResTemVo items) {
		this.payInfos = items;
	}
	@XmlElement(name = "respCommon")
    private RespCommonVo respCommon;
	@XmlElement(name = "payInfos")
	private LbQueOpUnpaidResTemVo payInfos;
}
