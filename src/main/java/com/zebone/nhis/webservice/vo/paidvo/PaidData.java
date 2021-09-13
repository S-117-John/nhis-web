package com.zebone.nhis.webservice.vo.paidvo;

import javax.xml.bind.annotation.XmlElement;

public class PaidData {
    private PaidDataVo paidDataVo;
    
	@XmlElement(name = "paidChargeList")
	public PaidDataVo getPaidDataVo() {
		return paidDataVo;
	}

	public void setPaidDataVo(PaidDataVo paidDataVo) {
		this.paidDataVo = paidDataVo;
	}
    
}
