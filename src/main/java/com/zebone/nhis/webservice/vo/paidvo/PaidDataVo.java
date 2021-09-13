package com.zebone.nhis.webservice.vo.paidvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PaidDataVo {
	private List<ResPaidVo> resPaidVos;

	@XmlElement(name = "paidCharge")
	public List<ResPaidVo> getResPaidVos() {
		return resPaidVos;
	}

	public void setResPaidVos(List<ResPaidVo> resPaidVos) {
		this.resPaidVos = resPaidVos;
	}
	
}
