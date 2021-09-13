package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("patient")
public class Patient {
	@XStreamAsAttribute
   private String classCode;
	
	private SubjectOf1 subjectOf1;
	private Id id ;
	private Telecom telecom;
	private StatusCode statusCode ;
	
	private EffectiveTime effectiveTime ;
	
	private PatientPerson patientPerson ;
	
	private ProviderOrganization providerOrganization;
	
	private CoveredPartyOf coveredPartyOf ;
	
	@XStreamAsAttribute
	private String patie;
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

	
	public Telecom getTelecom() {
		if(telecom==null)telecom=new Telecom();
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	public StatusCode getStatusCode() {
		if(statusCode==null)statusCode=new StatusCode();
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public EffectiveTime getEffectiveTime() {
		if(effectiveTime==null)effectiveTime=new EffectiveTime();
		return effectiveTime;
	}

	public void setEffectiveTime(EffectiveTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public PatientPerson getPatientPerson() {
		if(patientPerson == null)patientPerson=new PatientPerson();
		return patientPerson;
	}

	public void setPatientPerson(PatientPerson patientPerson) {
		this.patientPerson = patientPerson;
	}

	public ProviderOrganization getProviderOrganization() {
		if(providerOrganization==null)providerOrganization=new ProviderOrganization();
		return providerOrganization;
	}

	public void setProviderOrganization(ProviderOrganization providerOrganization) {
		this.providerOrganization = providerOrganization;
	}

	public CoveredPartyOf getCoveredPartyOf() {
		if(coveredPartyOf==null)coveredPartyOf=new CoveredPartyOf();
		return coveredPartyOf;
	}

	public void setCoveredPartyOf(CoveredPartyOf coveredPartyOf) {
		this.coveredPartyOf = coveredPartyOf;
	}

	public String getPatie() {
		return patie;
	}

	public void setPatie(String patie) {
		this.patie = patie;
	}

	public SubjectOf1 getSubjectOf1() {
		if(subjectOf1==null)subjectOf1=new SubjectOf1();
		return subjectOf1;
	}

	public void setSubjectOf1(SubjectOf1 subjectOf1) {
		this.subjectOf1 = subjectOf1;
	}
	
}
