package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("arrivedBy")
public class ArrivedBy {
	
	private TransportationIntent transportationIntent;

	public TransportationIntent getTransportationIntent() {
		if(transportationIntent == null) {
			transportationIntent = new TransportationIntent();
		}
		return transportationIntent;
	}

	public void setTransportationIntent(TransportationIntent transportationIntent) {
		this.transportationIntent = transportationIntent;
	}

	
}
