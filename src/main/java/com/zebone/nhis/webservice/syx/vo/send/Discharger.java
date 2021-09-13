package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("discharger")
public class Discharger {
	@XStreamAsAttribute
	private String typeCode;
	
	private Time time;
	
	private AssignedPerson assignedPerson;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Time getTime() {
		if(time==null) time=new Time();
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public AssignedPerson getAssignedPerson() {
		if(assignedPerson==null) assignedPerson=new  AssignedPerson();
		return assignedPerson;
	}

	public void setAssignedPerson(AssignedPerson assignedPerson) {
		this.assignedPerson = assignedPerson;
	}
	
	
}
