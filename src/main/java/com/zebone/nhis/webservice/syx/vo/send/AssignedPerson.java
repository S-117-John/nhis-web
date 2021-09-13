package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("assignedPerson")
public class AssignedPerson {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Id id;
	private AssignedPerson assignedPerson;
	@XStreamAsAttribute
	private String XSI_NIL;
	private Name name;
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

	public Name getName() {
		if(name==null) name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}

	public AssignedPerson getAssignedPerson() {
		if(assignedPerson==null)assignedPerson=new AssignedPerson();
		return assignedPerson;
	}

	public void setAssignedPerson(AssignedPerson assignedPerson) {
		this.assignedPerson = assignedPerson;
	}
	
}
