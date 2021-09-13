package com.zebone.nhis.webservice.vo.depositevo;

import javax.xml.bind.annotation.XmlElement;

public class DepositeData {
	
	private DepositeDataVo depositeDateVo;

	@XmlElement(name = "depositeList")
	public DepositeDataVo getDepositeDateVo() {
		return depositeDateVo;
	}

	public void setDepositeDateVo(DepositeDataVo depositeDateVo) {
		this.depositeDateVo = depositeDateVo;
	}
	
}
