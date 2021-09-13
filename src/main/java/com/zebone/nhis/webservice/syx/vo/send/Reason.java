package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("reason")
public class Reason {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String contextConductionInd;
	@XStreamAsAttribute
	private String contextControlCode;
	
	private ObservationDx observationDx;
	
	private Observation observation;
	
	public Observation getObservation() {
		if(observation==null)observation = new Observation();
		return observation;
	}
	public void setObservation(Observation observation) {
		this.observation = observation;
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
	public ObservationDx getObservationDx() {
		if(observationDx==null)observationDx=new ObservationDx();
		return observationDx;
	}
	public void setObservationDx(ObservationDx observationDx) {
		this.observationDx = observationDx;
	}
	public String getContextControlCode() {
		return contextControlCode;
	}
	public void setContextControlCode(String contextControlCode) {
		this.contextControlCode = contextControlCode;
	}
	
	
}
