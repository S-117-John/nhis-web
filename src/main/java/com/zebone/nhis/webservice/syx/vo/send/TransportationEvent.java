package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("transportationEvent")
public class TransportationEvent {
	private Code code;
	
	private Location location;

	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Location getLocation() {
		if(location==null) location=new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
