package com.zebone.nhis.webservice.vo.paidfeevo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PaidFeeDataVo {
	private List<ResPaidFeeVo> resPaidFeeVo;

	@XmlElement(name = "paidCharge")
	public List<ResPaidFeeVo> getResPaidFeeVo() {
		return resPaidFeeVo;
	}

	public void setResPaidFeeVo(List<ResPaidFeeVo> resPaidFeeVo) {
		this.resPaidFeeVo = resPaidFeeVo;
	}

}
