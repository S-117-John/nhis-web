package com.zebone.nhis.webservice.vo.chagsetvo;

import javax.xml.bind.annotation.XmlElement;

public class ResOpSettleVo {
	private String pkSettle;
	
	private String codeSt;
    
	@XmlElement(name = "codeSt")
	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	@XmlElement(name = "pkSettle")
	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
}
