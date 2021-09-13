package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("subjectOf1")
public class SubjectOf1 {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String contextControlCode;
	
	private SpecimenProcessStep specimenProcessStep;
	
	private QueryMatchObservation queryMatchObservation;
	
	
	public SpecimenProcessStep getSpecimenProcessStep() {
		if(specimenProcessStep == null) specimenProcessStep =new SpecimenProcessStep();
		return specimenProcessStep;
	}
	public void setSpecimenProcessStep(SpecimenProcessStep specimenProcessStep) {
		this.specimenProcessStep = specimenProcessStep;
	}
	public String getContextControlCode() {
		return contextControlCode;
	}
	public void setContextControlCode(String contextControlCode) {
		this.contextControlCode = contextControlCode;
	}
	public QueryMatchObservation getQueryMatchObservation() {
		if(queryMatchObservation==null)queryMatchObservation=new QueryMatchObservation();
		return queryMatchObservation;
	}
	public void setQueryMatchObservation(QueryMatchObservation queryMatchObservation) {
		this.queryMatchObservation = queryMatchObservation;
	}
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	
	
	
}	
