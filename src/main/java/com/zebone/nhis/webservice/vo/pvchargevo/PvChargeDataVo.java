package com.zebone.nhis.webservice.vo.pvchargevo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PvChargeDataVo {
	private List<ResPvChargeVo> resPvChargeVos;
    
	@XmlElement(name = "pvCharge")
	public List<ResPvChargeVo> getResPvChargeVos() {
		return resPvChargeVos;
	}

	public void setResPvChargeVos(List<ResPvChargeVo> resPvChargeVos) {
		this.resPvChargeVos = resPvChargeVos;
	}
	
	
}
