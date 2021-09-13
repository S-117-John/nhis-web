package com.zebone.nhis.webservice.vo.itemvo;

import javax.xml.bind.annotation.XmlElement;

public class ResItemCateVo {
	private String pkItemcate;
	private String name;
	private String pkParent;
	
	@XmlElement(name = "pkItemcate")
	public String getPkItemcate() {
		return pkItemcate;
	}
	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}
	
	@XmlElement(name = "name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "pkParent")
	public String getPkParent() {
		return pkParent;
	}
	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}
	
	
}
