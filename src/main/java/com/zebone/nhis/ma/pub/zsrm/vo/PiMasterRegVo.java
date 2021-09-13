package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiMasterRegVo extends  HeadVo {
	@XmlElement(name = "Body")
	private PiRegVo piRegVo;

	public PiRegVo getPiRegVo() {
		return piRegVo;
	}

	public void setPiRegVo(PiRegVo piRegVo) {
		this.piRegVo = piRegVo;
	}
}
