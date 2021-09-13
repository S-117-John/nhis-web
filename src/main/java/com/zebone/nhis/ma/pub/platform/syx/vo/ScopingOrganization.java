package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("scopingOrganization")
public class ScopingOrganization {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	private Id id;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getDeterminerCode() {
		return determinerCode;
	}

	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}
	
	
	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}

	public Id getId() {
		if(id==null) id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

}
