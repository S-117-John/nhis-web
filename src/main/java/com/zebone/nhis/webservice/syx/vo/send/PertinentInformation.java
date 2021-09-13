package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("pertinentInformation")
public class PertinentInformation {
	@XStreamAsAttribute
	private String typeCode;
	@XStreamAsAttribute
	private String contextConductionInd;
	private Organizer organizer;
	
	private Observation observation;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getContextConductionInd() {
		return contextConductionInd;
	}

	public void setContextConductionInd(String contextConductionInd) {
		this.contextConductionInd = contextConductionInd;
	}

	public Observation getObservation() {
		if(observation==null) observation=new Observation();
		return observation;
	}

	public void setObservation(Observation observation) {
		this.observation = observation;
	}

	public Organizer getOrganizer() {
		if(organizer==null)organizer = new Organizer();
		return organizer;
	}

	public void setOrganizer(Organizer organizer) {
		this.organizer = organizer;
	}

	
	
	
}
