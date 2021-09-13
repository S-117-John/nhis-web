package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("transcriber")
public class Transcriber {
	@XStreamAsAttribute
	private String typeCode;
	
    private Time time;
    
    private AssignedEntity assignedEntity;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Time getTime() {
		if(time == null) {
			time = new Time();
		}
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public AssignedEntity getAssignedEntity() {
		if(assignedEntity == null) {
			assignedEntity = new AssignedEntity();
		}
		return assignedEntity;
	}

	public void setAssignedEntity(AssignedEntity assignedEntity) {
		this.assignedEntity = assignedEntity;
	}
    
    

}
