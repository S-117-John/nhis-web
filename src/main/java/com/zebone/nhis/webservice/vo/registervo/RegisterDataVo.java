package com.zebone.nhis.webservice.vo.registervo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class RegisterDataVo {
    private List<ResRegisterVo> resvos;
    
	@XmlElement(name = "number")
	public List<ResRegisterVo> getResvos() {
		return resvos;
	}

	public void setResvos(List<ResRegisterVo> resvos) {
		this.resvos = resvos;
	}
     
}
