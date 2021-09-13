package com.zebone.nhis.webservice.vo.depositevo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class DepositeDataVo {
	
	private List<ResDepositeVo> resDepositeVo;
    
	@XmlElement(name = "deposite")
	public List<ResDepositeVo> getResDepositeVo() {
		return resDepositeVo;
	}

	public void setResDepositeVo(List<ResDepositeVo> resDepositeVo) {
		this.resDepositeVo = resDepositeVo;
	}
	
	
}
