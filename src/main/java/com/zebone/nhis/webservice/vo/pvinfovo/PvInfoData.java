package com.zebone.nhis.webservice.vo.pvinfovo;

import javax.xml.bind.annotation.XmlElement;

public class PvInfoData {
	
	private PvInfoDataVo pvInfoDataVo;
	
	@XmlElement(name="pvInfoList")
	public PvInfoDataVo getPvInfoDataVo() {
		return pvInfoDataVo;
	}

	public void setPvInfoDataVo(PvInfoDataVo pvInfoDataVo) {
		this.pvInfoDataVo = pvInfoDataVo;
	}

}
