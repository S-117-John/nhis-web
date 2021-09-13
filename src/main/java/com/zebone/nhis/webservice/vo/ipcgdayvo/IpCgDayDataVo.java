package com.zebone.nhis.webservice.vo.ipcgdayvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class IpCgDayDataVo {
	private List<ResIpCgDayVo> resIpCgDayVo;

	@XmlElement(name = "dayDeta")
	public List<ResIpCgDayVo> getResIpCgDayVo() {
		return resIpCgDayVo;
	}

	public void setResIpCgDayVo(List<ResIpCgDayVo> resIpCgDayVo) {
		this.resIpCgDayVo = resIpCgDayVo;
	}

}
