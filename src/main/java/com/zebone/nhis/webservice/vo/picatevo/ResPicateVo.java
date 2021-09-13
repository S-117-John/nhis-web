package com.zebone.nhis.webservice.vo.picatevo;

import javax.xml.bind.annotation.XmlElement;

public class ResPicateVo {
	
	private String pkPicate;
	
	private String name;
	
	@XmlElement(name = "pkPicate")
	public String getPkPicate() {
		return pkPicate;
	}

	public void setPkPicate(String pkPicate) {
		this.pkPicate = pkPicate;
	}
	
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
