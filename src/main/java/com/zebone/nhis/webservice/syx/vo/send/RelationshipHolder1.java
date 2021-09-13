package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("relationshipHolder1")
public class RelationshipHolder1 {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Name name ;

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

	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

}
