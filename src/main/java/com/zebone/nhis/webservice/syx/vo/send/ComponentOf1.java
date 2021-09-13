package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("componentOf1")
public class ComponentOf1 {
	@XStreamAsAttribute
	private String contextConductionInd;
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String contextControlCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private Encounter encounter;
	
	private ProcessStep processStep;
	
	private Subject subject;
		
	public String getXSI_NIL() {
		return XSI_NIL;
	}
	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	public Subject getSubject() {
		if(subject == null) {
			subject = new Subject();
		}
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public ProcessStep getProcessStep() {
		if(processStep == null) {
			processStep = new ProcessStep();
		}		
		return processStep;
	}
	public void setProcessStep(ProcessStep processStep) {
		this.processStep = processStep;
	}
	public String getContextConductionInd() {
		return contextConductionInd;
	}
	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getContextControlCode() {
		return contextControlCode;
	}
	public void setContextControlCode(String contextControlCode) {
		this.contextControlCode = contextControlCode;
	}
	public Encounter getEncounter() {
		if(encounter==null) encounter=new Encounter();
		return encounter;
	}
	public void setEncounter(Encounter encounter) {
		this.encounter = encounter;
	}
	
}
