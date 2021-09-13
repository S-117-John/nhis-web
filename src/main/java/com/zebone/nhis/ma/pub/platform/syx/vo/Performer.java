package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("performer")
public class Performer {
	@XStreamAsAttribute
	private String typeCode;
	private Time time;
	private AssignedEntity assignedEntity;
	
	private AssignedPerson assignedPerson;
	
	public AssignedPerson getAssignedPerson() {
		if(assignedPerson == null) {
			assignedPerson = new AssignedPerson();
		}
		return assignedPerson;
	}
	public void setAssignedPerson(AssignedPerson assignedPerson) {
		this.assignedPerson = assignedPerson;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Time getTime() {
		if(time==null)time=new Time();
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	public AssignedEntity getAssignedEntity() {
		if(assignedEntity==null) assignedEntity=new AssignedEntity();
		return assignedEntity;
	}
	public void setAssignedEntity(AssignedEntity assignedEntity) {
		this.assignedEntity = assignedEntity;
	}
	
}
