package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("location1")
public class Location1 {
	@XStreamAsAttribute
	private String typeCode;
	
	private Time time;
	
	private StatusCode statusCode;
	
	private ServiceDeliveryLocation serviceDeliveryLocation;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public Time getTime() {
		if(time==null) time=new Time();
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public ServiceDeliveryLocation getServiceDeliveryLocation() {
		if(serviceDeliveryLocation==null)serviceDeliveryLocation=new ServiceDeliveryLocation();
		return serviceDeliveryLocation;
	}

	public void setServiceDeliveryLocation(
			ServiceDeliveryLocation serviceDeliveryLocation) {
		this.serviceDeliveryLocation = serviceDeliveryLocation;
	}
	
	
}
