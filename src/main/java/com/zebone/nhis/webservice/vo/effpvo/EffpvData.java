package com.zebone.nhis.webservice.vo.effpvo;

import javax.xml.bind.annotation.XmlElement;

public class EffpvData {
	
	private EffpvDataVo effpvDataVo;
	
	@XmlElement(name="effPvInfoList")
	public EffpvDataVo getEffpvDataVo() {
		return effpvDataVo;
	}

	public void setEffpvDataVo(EffpvDataVo effpvDataVo) {
		this.effpvDataVo = effpvDataVo;
	}
	
}
