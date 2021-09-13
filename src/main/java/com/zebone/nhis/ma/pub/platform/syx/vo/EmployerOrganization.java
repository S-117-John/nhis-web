package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("employerOrganization")
public class EmployerOrganization {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	private Name name ;
	private ContactParty contactParty ;

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

	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public ContactParty getContactParty() {
		if(contactParty==null)contactParty=new ContactParty();
		return contactParty;
	}

	public void setContactParty(ContactParty contactParty) {
		this.contactParty = contactParty;
	}

}
