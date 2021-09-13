package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("specimenNatural")
public class SpecimenNatural {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	
	private Code code;
	
	private Quantity quantity;
	
	private DerivedSpecimen derivedSpecimen;

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

	public Code getCode() {
		if(code == null )code = new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Quantity getQuantity() {
		if(quantity == null) quantity=new Quantity();
		return quantity;
	}

	public void setQuantity(Quantity quantity) {
		this.quantity = quantity;
	}

	public DerivedSpecimen getDerivedSpecimen() {
		if(derivedSpecimen == null) derivedSpecimen = new DerivedSpecimen();
		return derivedSpecimen;
	}

	public void setDerivedSpecimen(DerivedSpecimen derivedSpecimen) {
		this.derivedSpecimen = derivedSpecimen;
	}
	
	
}
