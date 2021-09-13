package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("subject1")
public class Subject1 {
	@XStreamAsAttribute
	private String typeCode;
	private ValueSet valueSet;
	
	private Patient patient ;
	private AssignedEntity assignedEntity;
	private PriorRegisteredRole priorRegisteredRole;
	public Patient getPatient() {
		if(patient==null) patient=new Patient();
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public ValueSet getValueSet() {
		if(valueSet==null)valueSet=new ValueSet();
		return valueSet;
	}

	public void setValueSet(ValueSet valueSet) {
		this.valueSet = valueSet;
	}

	public AssignedEntity getAssignedEntity() {
		if(assignedEntity==null)assignedEntity=new AssignedEntity();
		return assignedEntity;
	}

	public void setAssignedEntity(AssignedEntity assignedEntity) {
		this.assignedEntity = assignedEntity;
	}

	public PriorRegisteredRole getPriorRegisteredRole() {
		if(priorRegisteredRole==null)priorRegisteredRole = new PriorRegisteredRole();
		return priorRegisteredRole;
	}

	public void setPriorRegisteredRole(PriorRegisteredRole priorRegisteredRole) {
		this.priorRegisteredRole = priorRegisteredRole;
	}
	
	
}
