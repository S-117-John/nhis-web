package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("component1")
public class Component1 {

	@XStreamAsAttribute
	private String contextConductionInd;
	
	private ProcessStep processStep;

	public String getContextConductionInd() {
		return contextConductionInd;
	}

	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
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
	
	
	
}
