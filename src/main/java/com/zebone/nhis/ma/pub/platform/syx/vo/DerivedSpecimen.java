package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("derivedSpecimen")
public class DerivedSpecimen {
	@XStreamAsAttribute
	private String classCode;
	
	private Id id;
	
	private Code code;
	
	private SpecimenNatural specimenNatural;

	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		if(id== null )id = new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public Code getCode() {
		if(code==null )code =new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public SpecimenNatural getSpecimenNatural() {
		if(specimenNatural==null) specimenNatural= new SpecimenNatural();
		return specimenNatural;
	}

	public void setSpecimenNatural(SpecimenNatural specimenNatural) {
		this.specimenNatural = specimenNatural;
	}
	
	
}
