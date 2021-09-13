package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("locatedPlace")
public class LocatedPlace {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	
	private Id id;
	
	private Name name;
	
	private LocatedEntityHasParts locatedEntityHasParts;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getDeterminerCode() {
		return determinerCode;
	}

	public void setDeterminerCode(String determinerCode) {
		this.determinerCode = determinerCode;
	}

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public LocatedEntityHasParts getLocatedEntityHasParts() {
		if(locatedEntityHasParts==null)locatedEntityHasParts=new LocatedEntityHasParts();
		return locatedEntityHasParts;
	}

	public void setLocatedEntityHasParts(LocatedEntityHasParts locatedEntityHasParts) {
		this.locatedEntityHasParts = locatedEntityHasParts;
	}
	
}
