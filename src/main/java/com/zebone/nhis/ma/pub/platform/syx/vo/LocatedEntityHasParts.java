package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("locatedEntityHasParts")
public class LocatedEntityHasParts {
	@XStreamAsAttribute
	private String classCode;
	
	private LocatedPlace locatedPlace;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public LocatedPlace getLocatedPlace() {
		if(locatedPlace==null)locatedPlace=new LocatedPlace();
		return locatedPlace;
	}

	public void setLocatedPlace(LocatedPlace locatedPlace) {
		this.locatedPlace = locatedPlace;
	}
	
	
	
}
