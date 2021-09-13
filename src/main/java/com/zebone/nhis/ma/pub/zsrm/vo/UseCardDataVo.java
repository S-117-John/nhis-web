package com.zebone.nhis.ma.pub.zsrm.vo;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(value = XmlAccessType.FIELD)
public class UseCardDataVo {
	//电子健康卡ID
	@XmlElement(name = "useCardDataVo")
	private PiA1028RegVo useCardDataVo;

	public PiA1028RegVo getUseCardDataVo() {
		return useCardDataVo;
	}

	public void setUseCardDataVo(PiA1028RegVo useCardDataVo) {
		this.useCardDataVo = useCardDataVo;
	}

	public UseCardDataVo() {
	}

	public UseCardDataVo(PiA1028RegVo useCardDataVo) {
		this.useCardDataVo = useCardDataVo;
	}
}
