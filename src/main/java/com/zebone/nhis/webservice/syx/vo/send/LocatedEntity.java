package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("locatedEntity")
public class LocatedEntity {
	@XStreamAsAttribute
	private String classCode;
	
	private Id id;
	
	private Location location;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		if(id==null) id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Location getLocation() {
		if(location==null) location=new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
	
}
