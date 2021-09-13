package com.zebone.nhis.webservice.vo.hospinfovo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class HospInfoDataVo {
	private List<ResHospInfoVo> resHospInfoVo;

	@XmlElement(name = "hospInfo")
	public List<ResHospInfoVo> getResHospInfoVo() {
		return resHospInfoVo;
	}

	public void setResHospInfoVo(List<ResHospInfoVo> resHospInfoVo) {
		this.resHospInfoVo = resHospInfoVo;
	}

}
