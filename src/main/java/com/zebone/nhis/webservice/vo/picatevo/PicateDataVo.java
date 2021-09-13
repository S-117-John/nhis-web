package com.zebone.nhis.webservice.vo.picatevo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class PicateDataVo {
	
	private List<ResPicateVo> resPicateVo;
	
	@XmlElement(name = "piClass")
	public List<ResPicateVo> getResPicateVo() {
		return resPicateVo;
	}

	public void setResPicateVo(List<ResPicateVo> resPicateVo) {
		this.resPicateVo = resPicateVo;
	}

}
