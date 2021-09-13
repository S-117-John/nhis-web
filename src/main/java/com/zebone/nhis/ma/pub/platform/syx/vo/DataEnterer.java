package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("dataEnterer")
public class DataEnterer {
	@XStreamAsAttribute
	private String typeCode;
	
	private AssignedPerson assignedPerson;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public AssignedPerson getAssignedPerson() {
		if(assignedPerson==null) {
			assignedPerson = new AssignedPerson();
		}
		return assignedPerson;
	}

	public void setAssignedPerson(AssignedPerson assignedPerson) {
		
		this.assignedPerson = assignedPerson;
	}
	
	

}
