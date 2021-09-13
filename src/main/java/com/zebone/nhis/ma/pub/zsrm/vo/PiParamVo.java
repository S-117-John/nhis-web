package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiParamVo {
	//二维码数据内容
	@XmlElement(name = "ehealth_code")
	private String ehealthCode;

	public String getEhealthCode() {
		return ehealthCode;
	}

	public void setEhealthCode(String ehealthCode) {
		this.ehealthCode = ehealthCode;
	}
}
