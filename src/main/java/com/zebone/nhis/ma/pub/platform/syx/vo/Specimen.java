package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("specimen")
public class Specimen {
	@XStreamAsAttribute
	private String classCode;
	
	@XStreamAsAttribute
	private String typeCode;
	
	private Specimen specimen;
	private Id id;
	private Code code;
	private SpecimenNatural specimenNatural;
	private SubjectOf1 subjectOf1;	
	
	
	
	public SubjectOf1 getSubjectOf1() {
		if(subjectOf1 == null) {
			subjectOf1 = new SubjectOf1();
		}
		return subjectOf1;
	}
	public void setSubjectOf1(SubjectOf1 subjectOf1) {
		this.subjectOf1 = subjectOf1;
	}
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public Id getId() {
		if(id==null)id = new Id();
		return id;
	}
	public void setId(Id id) {
		this.id = id;
	}
	public Code getCode() {
		if(code == null)code = new Code();
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public Specimen getSpecimen() {
		if(specimen == null)specimen = new Specimen();
		return specimen;
	}
	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}
	public SpecimenNatural getSpecimenNatural() {
		if(specimenNatural == null) specimenNatural = new SpecimenNatural();
		return specimenNatural;
	}
	public void setSpecimenNatural(SpecimenNatural specimenNatural) {
		this.specimenNatural = specimenNatural;
	}
	
}
