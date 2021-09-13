package com.zebone.nhis.webservice.vo.prechargevo;

import javax.xml.bind.annotation.XmlElement;

public class ResPreChargeVo {
	
	private String pkDepo;
	
	private String codeDepo;
    
	@XmlElement(name = "codeDepo")
	public String getCodeDepo() {
		return codeDepo;
	}

	public void setCodeDepo(String codeDepo) {
		this.codeDepo = codeDepo;
	}

	@XmlElement(name = "pkDepo")
	public String getPkDepo() {
		return pkDepo;
	}

	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}
	
	
   
}
