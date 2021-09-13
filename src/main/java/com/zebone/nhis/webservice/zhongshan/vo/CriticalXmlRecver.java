package com.zebone.nhis.webservice.zhongshan.vo;

import javax.xml.bind.annotation.XmlElement;

public class CriticalXmlRecver {
	private String type;
	private String code;
	private String name;
	private String note;
	@XmlElement(name="type")
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlElement(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@XmlElement(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name="note")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
}
