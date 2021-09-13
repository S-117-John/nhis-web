package com.zebone.nhis.webservice.vo.hpvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class HpDataVo {
	private List<ResHpVo> resHpVo;
	
	@XmlElement(name = "siInfo")
	public List<ResHpVo> getResHpVo() {
		return resHpVo;
	}

	public void setResHpVo(List<ResHpVo> resHpVo) {
		this.resHpVo = resHpVo;
	}

}
