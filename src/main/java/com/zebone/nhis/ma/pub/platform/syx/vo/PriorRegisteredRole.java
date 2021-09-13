package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("priorRegisteredRole")
public class PriorRegisteredRole {
	@XStreamAsAttribute
	private String classCode;
	
	private Id id;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		if(id==null)id = new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}
	
	
}
