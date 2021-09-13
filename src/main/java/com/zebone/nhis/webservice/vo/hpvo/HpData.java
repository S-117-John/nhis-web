package com.zebone.nhis.webservice.vo.hpvo;

import javax.xml.bind.annotation.XmlElement;

public class HpData {
	private HpDataVo hpDataVo;
	
	@XmlElement(name="siInfoList")
	public HpDataVo getHpDataVo() {
		return hpDataVo;
	}

	public void setHpDataVo(HpDataVo hpDataVo) {
		this.hpDataVo = hpDataVo;
	}

}
