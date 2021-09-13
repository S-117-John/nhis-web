package com.zebone.nhis.ma.pub.platform.syx.vo;

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
