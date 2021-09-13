package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("contactParty")
public class ContactParty {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	private Telecom telecom ;
	
	private ContactPerson contactPerson;
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Telecom getTelecom() {
		if(telecom==null)telecom=new Telecom();
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	public ContactPerson getContactPerson() {
		if(contactPerson==null)contactPerson=new ContactPerson();
		return contactPerson;
	}

	public void setContactPerson(ContactPerson contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}
	
}
