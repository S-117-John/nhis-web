package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("departedBy")
public class DepartedBy {
	private TransportationEvent transportationEvent;
	
	private Transportation transportation; 

	public Transportation getTransportation() {
		if(transportation == null) {
			transportation = new Transportation();
		}
		return transportation;
	}

	public void setTransportation(Transportation transportation) {
		this.transportation = transportation;
	}

	public TransportationEvent getTransportationEvent() {
		if(transportationEvent==null) transportationEvent=new TransportationEvent();
		return transportationEvent;
	}

	public void setTransportationEvent(TransportationEvent transportationEvent) {
		this.transportationEvent = transportationEvent;
	}
	
}
