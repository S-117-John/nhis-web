package com.zebone.nhis.webservice.vo.ipcgvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class IpCgDataVo {
	private List<ResIpCgVo> resIpCgVo;

	@XmlElement(name = "hospExpe")
	public List<ResIpCgVo> getResIpCgVo() {
		return resIpCgVo;
	}

	public void setResIpCgVo(List<ResIpCgVo> resIpCgVo) {
		this.resIpCgVo = resIpCgVo;
	}

}
