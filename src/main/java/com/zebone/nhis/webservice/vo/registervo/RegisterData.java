package com.zebone.nhis.webservice.vo.registervo;

import javax.xml.bind.annotation.XmlElement;

public class RegisterData {
	private RegisterDataVo registerDataVo;
    
	@XmlElement(name = "numberList")
	public RegisterDataVo getRegisterDataVo() {
		return registerDataVo;
	}

	public void setRegisterDataVo(RegisterDataVo registerDataVo) {
		this.registerDataVo = registerDataVo;
	}
}
