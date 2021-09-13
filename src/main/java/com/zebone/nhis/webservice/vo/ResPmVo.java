package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlElement;

public class ResPmVo {
	
	private String codeOp;
	
	private String codePi;
	
	private String namePi;
	
	private String pkPi;
	
	@XmlElement(name="codeOp")
	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	@XmlElement(name="codePi")
	public String getCodePi() {
		return codePi;
	}

	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	@XmlElement(name="namePi")
	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
    @XmlElement(name="pkPi")
	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	
}
