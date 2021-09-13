package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("component")
public class Component {
	@XStreamAsAttribute
	private String typeCode;
	private Observation observation;
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Observation getObservation() {
		if(observation==null)observation=new Observation();
		return observation;
	}
	public void setObservation(Observation observation) {
		this.observation = observation;
	}
	
	
}
