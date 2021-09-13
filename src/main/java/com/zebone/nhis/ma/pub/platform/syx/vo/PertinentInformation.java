package com.zebone.nhis.ma.pub.platform.syx.vo;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

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
