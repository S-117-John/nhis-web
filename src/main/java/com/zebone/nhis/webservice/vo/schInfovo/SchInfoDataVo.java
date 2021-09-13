package com.zebone.nhis.webservice.vo.schInfovo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class SchInfoDataVo {
	private List<ResSchInfoVo> resSchInfoVo;

	@XmlElement(name = "doctInfo")
	public List<ResSchInfoVo> getResSchInfoVo() {
		return resSchInfoVo;
	}

	public void setResSchInfoVo(List<ResSchInfoVo> resSchInfoVo) {
		this.resSchInfoVo = resSchInfoVo;
	}

}
