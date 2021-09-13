package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiVo extends  HeadVo {
	@XmlElement(name = "Body")
	private PiRegRegisterVo piRegRegisterVo;

	public PiRegRegisterVo getPiRegRegisterVo() {
		return piRegRegisterVo;
	}

	public void setPiRegRegisterVo(PiRegRegisterVo piRegRegisterVo) {
		this.piRegRegisterVo = piRegRegisterVo;
	}
}
