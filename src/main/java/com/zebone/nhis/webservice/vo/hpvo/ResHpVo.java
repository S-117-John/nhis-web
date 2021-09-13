package com.zebone.nhis.webservice.vo.hpvo;

import javax.xml.bind.annotation.XmlElement;

public class ResHpVo {
	
	private String pkHp;

	private String name;
	
	@XmlElement(name="pkHp")
	public String getPkHp() {
		return pkHp;
	}
	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
	
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
