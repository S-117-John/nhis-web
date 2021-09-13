package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("assignedEntity")
public class AssignedEntity {
	@XStreamAsAttribute
	private String classCode;

	private Id id ;

	private Code code;
	
	private AssignedPerson assignedPerson ;
	
	private Name name;
	private Addr addr;
	private Telecom telecom;
	private StatusCode statusCode;
	private EffectiveTime effectiveTime;
	private RepresentedOrganization representedOrganization;
	private AssignedPrincipalOrganization assignedPrincipalOrganization;
	private ContactParty contactParty;
	
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public AssignedPerson getAssignedPerson() {
		if(assignedPerson==null)assignedPerson=new AssignedPerson();
		return assignedPerson;
	}

	public void setAssignedPerson(AssignedPerson assignedPerson) {
		this.assignedPerson = assignedPerson;
	}
	public Code getCode() {
		if(code==null)code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Addr getAddr() {
		if(addr==null)addr=new Addr();
		return addr;
	}

	public void setAddr(Addr addr) {
		this.addr = addr;
	}
	
	
	public Telecom getTelecom() {
		if(telecom==null) telecom=new Telecom();
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	
	public StatusCode getStatusCode() {
		if(statusCode==null) statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null) effectiveTime=new EffectiveTime();
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public RepresentedOrganization getRepresentedOrganization() {
		if(representedOrganization==null)representedOrganization=new RepresentedOrganization();
		return representedOrganization;
	}

	public void setRepresentedOrganization(
			RepresentedOrganization representedOrganization) {
		this.representedOrganization = representedOrganization;
	}

	public AssignedPrincipalOrganization getAssignedPrincipalOrganization() {
		if(assignedPrincipalOrganization==null) assignedPrincipalOrganization=new AssignedPrincipalOrganization();
		return assignedPrincipalOrganization;
	}

	public void setAssignedPrincipalOrganization(
			AssignedPrincipalOrganization assignedPrincipalOrganization) {
		this.assignedPrincipalOrganization = assignedPrincipalOrganization;
	}

	public ContactParty getContactParty() {
		if(contactParty==null)contactParty=new ContactParty();
		return contactParty;
	}

	public void setContactParty(ContactParty contactParty) {
		this.contactParty = contactParty;
	}

	
    
}
