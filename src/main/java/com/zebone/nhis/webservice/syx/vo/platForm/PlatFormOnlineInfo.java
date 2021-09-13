package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PlatFormOnlineInfo {

	/**
	 * 患者登记主键
	 * */
	@XmlElement(name = "pkPv")
	private String pkPv;

	/**
	 * 就医登记号
	 */
	@XmlElement(name = "codeHpst")
	private String codeHpst;

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeHpst() {
		return codeHpst;
	}

	public void setCodeHpst(String codeHpst) {
		this.codeHpst = codeHpst;
	}
	
	
}
