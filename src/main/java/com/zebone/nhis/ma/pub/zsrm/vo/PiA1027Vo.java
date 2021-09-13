package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Data")
@XmlAccessorType(value = XmlAccessType.FIELD)
public class PiA1027Vo extends  HeadVo {
	@XmlElement(name = "Body")
	private PiA1027RegVo piA1027RegVo;

	public PiA1027RegVo getPiA1027RegVo() {
		return piA1027RegVo;
	}

	public void setPiA1027RegVo(PiA1027RegVo piA1027RegVo) {
		this.piA1027RegVo = piA1027RegVo;
	}
}
