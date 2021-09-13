package com.zebone.nhis.webservice.vo.hospinfobedvo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class HospInfoBedDataVo {
	private List<ResHospInfoBedVo> resHospInfoBedVo;
    
	@XmlElement(name = "hospInfo")
	public List<ResHospInfoBedVo> getResHospInfoBedVo() {
		return resHospInfoBedVo;
	}

	public void setResHospInfoBedVo(List<ResHospInfoBedVo> resHospInfoBedVo) {
		this.resHospInfoBedVo = resHospInfoBedVo;
	}

	
}
