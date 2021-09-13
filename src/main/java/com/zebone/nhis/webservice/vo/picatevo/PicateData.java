package com.zebone.nhis.webservice.vo.picatevo;

import javax.xml.bind.annotation.XmlElement;

public class PicateData {
	private PicateDataVo picateDataVo;
    
	@XmlElement(name="piClassList")
	public PicateDataVo getPicateDataVo() {
		return picateDataVo;
	}

	public void setPicateDataVo(PicateDataVo picateDataVo) {
		this.picateDataVo = picateDataVo;
	}

}
