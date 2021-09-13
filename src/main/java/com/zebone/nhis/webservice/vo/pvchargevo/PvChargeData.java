package com.zebone.nhis.webservice.vo.pvchargevo;

import javax.xml.bind.annotation.XmlElement;

public class PvChargeData {
	private PvChargeDataVo pvChargeDataVo;
	
	@XmlElement(name = "pvChargeList")
	public PvChargeDataVo getPvChargeDataVo() {
		return pvChargeDataVo;
	}

	public void setPvChargeDataVo(PvChargeDataVo pvChargeDataVo) {
		this.pvChargeDataVo = pvChargeDataVo;
	}
}
