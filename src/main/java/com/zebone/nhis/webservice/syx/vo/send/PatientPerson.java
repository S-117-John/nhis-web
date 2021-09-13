package com.zebone.nhis.webservice.syx.vo.send;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("patientPerson")
public class PatientPerson {
	@XStreamAsAttribute
	private String classCode;
	@XStreamAsAttribute
	private String determinerCode;
	@XStreamAsAttribute
	private String XSI_NIL;
	
	private Id id ;
	private IdCategory idCategory ;
	private Name name;
	private Telecom telecom;
	private AdministrativeGenderCode administrativeGenderCode ;
	private BirthTime birthTime;
	private Addr addr;
	private MaritalStatusCode maritalStatusCode ;
	private EthnicGroupCode ethnicGroupCode;
	private AsEmployee asEmployee ;
	@XStreamImplicit
	private List<AsOtherIDs> asOtherIDs ;// 
	private AsOtherIDs asOtherID;
	private PersonalRelationship personalRelationship ;
	
	private AsCitizen asCitizen;
	
	private ContactParty contactParty;
	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public IdCategory getIdCategory() {
		if(idCategory==null)idCategory=new IdCategory();
		return idCategory;
	}

	public void setIdCategory(IdCategory idCategory) {
		this.idCategory = idCategory;
	}

	public Name getName() {
		if(name==null)name=new Name();
		return name;
	}

	public void setName(Name name) {
		this.name = name;
	}

	public Telecom getTelecom() {
		if(telecom==null)telecom=new Telecom();
		return telecom;
	}

	public void setTelecom(Telecom telecom) {
		this.telecom = telecom;
	}

	public AdministrativeGenderCode getAdministrativeGenderCode() {
		if(administrativeGenderCode==null)administrativeGenderCode=new AdministrativeGenderCode();
		return administrativeGenderCode;
	}

	public void setAdministrativeGenderCode(
			AdministrativeGenderCode administrativeGenderCode) {
		this.administrativeGenderCode = administrativeGenderCode;
	}

	public BirthTime getBirthTime() {
		if(birthTime==null)birthTime=new BirthTime();
		return birthTime;
	}

	public void setBirthTime(BirthTime birthTime) {
		this.birthTime = birthTime;
	}

	public Addr getAddr() {
		if(addr==null) addr=new Addr();
		return addr;
	}

	public void setAddr(Addr addr) {
		this.addr = addr;
	}

	public MaritalStatusCode getMaritalStatusCode() {
		if(maritalStatusCode==null)maritalStatusCode=new MaritalStatusCode();
		return maritalStatusCode;
	}

	public void setMaritalStatusCode(MaritalStatusCode maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}

	public EthnicGroupCode getEthnicGroupCode() {
		if(ethnicGroupCode==null) ethnicGroupCode=new EthnicGroupCode();
		return ethnicGroupCode;
	}

	public void setEthnicGroupCode(EthnicGroupCode ethnicGroupCode) {
		this.ethnicGroupCode = ethnicGroupCode;
	}

	public AsEmployee getAsEmployee() {
		if(asEmployee==null) asEmployee=new AsEmployee();
		return asEmployee;
	}

	public void setAsEmployee(AsEmployee asEmployee) {
		this.asEmployee = asEmployee;
	}

	public List<AsOtherIDs> getAsOtherIDs() {
		if(asOtherIDs==null)asOtherIDs=new ArrayList<AsOtherIDs>();
		return asOtherIDs;
	}

	
	public AsOtherIDs getAsOtherID() {
		if(asOtherID==null) asOtherID=new AsOtherIDs();
		return asOtherID;
	}

	public void setAsOtherID(AsOtherIDs asOtherID) {
		this.asOtherID = asOtherID;
	}

	public void setAsOtherIDs(List<AsOtherIDs> asOtherIDs) {
		this.asOtherIDs = asOtherIDs;
	}

	public PersonalRelationship getPersonalRelationship() {
		if(personalRelationship==null)personalRelationship=new PersonalRelationship();
		return personalRelationship;
	}

	public void setPersonalRelationship(
			PersonalRelationship personalRelationship) {
		this.personalRelationship = personalRelationship;
	}

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

	public String getXSI_NIL() {
		return XSI_NIL;
	}

	public void setXSI_NIL(String xSI_NIL) {
		XSI_NIL = xSI_NIL;
	}

	public AsCitizen getAsCitizen() {
		if(asCitizen== null) asCitizen = new AsCitizen();
		return asCitizen;
	}

	public void setAsCitizen(AsCitizen asCitizen) {
		this.asCitizen = asCitizen;
	}

	public ContactParty getContactParty() {
		if(contactParty== null) contactParty = new ContactParty();
		return contactParty;
	}

	public void setContactParty(ContactParty contactParty) {
		this.contactParty = contactParty;
	}
	
	
}
