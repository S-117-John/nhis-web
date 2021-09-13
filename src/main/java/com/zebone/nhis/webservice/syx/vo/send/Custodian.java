package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("custodian")
public class Custodian {
	@XStreamAsAttribute
	private String typeCode;
	
	private AssignedEntity assignedEntity;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public AssignedEntity getAssignedEntity() {
		if(assignedEntity==null) assignedEntity=new AssignedEntity();
		return assignedEntity;
	}

	public void setAssignedEntity(AssignedEntity assignedEntity) {
		this.assignedEntity = assignedEntity;
	}
	
	
	
}
