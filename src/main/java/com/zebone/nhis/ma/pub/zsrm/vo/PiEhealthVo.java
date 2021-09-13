package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiEhealthVo extends  HeadVo {
	@XmlElement(name = "Body")
	private PiParamVo PiParamVo;

	public com.zebone.nhis.ma.pub.zsrm.vo.PiParamVo getPiParamVo() {
		return PiParamVo;
	}

	public void setPiParamVo(com.zebone.nhis.ma.pub.zsrm.vo.PiParamVo piParamVo) {
		PiParamVo = piParamVo;
	}
}
