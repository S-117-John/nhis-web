package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("asLocatedEntityPartOf")
public class AsLocatedEntityPartOf {
	@XStreamAsAttribute
	private String classCode;
	
	private Location location;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Location getLocation() {
		if(location==null)location=new Location();
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}
	
}
