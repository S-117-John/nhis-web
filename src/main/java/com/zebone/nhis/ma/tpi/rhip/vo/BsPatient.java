package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Bs_Patient 病人基本信息
 * @author kong
 *
 */
@XmlRootElement(name = "Bs_Patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class BsPatient {

	
	@XmlElementWrapper(name="CardInfos")
	@XmlElement(name = "CardInfo")
	protected List<CardInfo> cardInfo;
	
	@XmlElementWrapper(name="AddressInfos")
	@XmlElement(name = "AddressInfo")
	protected List<AddressInfo> addressInfo;
	
	@XmlElement(name = "IdType", required = true)
    protected String idtype;
	
	@XmlElement(name = "IdCard", required = true)
    protected String idcard;
	
	@XmlElement(name = "PersonName", required = true)
    protected String personname;
	
	@XmlElement(name = "Birthday", required = true)
    protected String birthday;
	
	@XmlElement(name = "SexCode", required = true)
    protected String sexcode;
	
	@XmlElement(name = "BloodTypeCode")
    protected String bloodtypecode;
	
	@XmlElement(name = "RhBloodCode")
    protected String rhbloodcode;
	
	@XmlElement(name = "PersonName_PYCode")
    protected String personnamepycode;
	
	@XmlElement(name = "NationCode", required = true)
    protected String nationCode;
	
	@XmlElement(name = "NationalityCode", required = true)
    protected String nationalitycode;
	
	@XmlElement(name = "MaritalStatusCode")
    protected String maritalStatuscode;
	
	@XmlElement(name = "WorkCode")
    protected String workcode;
	
	@XmlElement(name = "EducationCode")
    protected String educationcode;
	
	@XmlElement(name = "WorkPlace")
    protected String workplace;
	
	@XmlElement(name = "ContactTypeCode")
    protected String contacttypecode;
	
	@XmlElement(name = "ContactNo")
    protected String contactno;
	
	@XmlElement(name = "STARTWORKDATE")
    protected String startworkdate;
	
	@XmlElement(name = "EmergencyIdType")
    protected String emergencyidtype;
	
	@XmlElement(name = "EmergencyIdCard")
    protected String emergencyidcard;
	
	@XmlElement(name = "EmergencyName")
    protected String emergencyname;
	
	@XmlElement(name = "EmergencyRelations")
    protected String emergencyrelations;
	
	@XmlElement(name = "EmergencyContactNo")
    protected String emergencycontactno;
	
	@XmlElement(name = "SourceId", required = true)
    protected String sourceid;
	
	@XmlElement(name = "LocalId", required = true)
    protected String localid;
	
	@XmlElement(name = "ManageUnit", required = true)
    protected String manageunit;

	
	@XmlAttribute(name = "Name")
	protected String name;

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getSexcode() {
		return sexcode;
	}

	public void setSexcode(String sexcode) {
		this.sexcode = sexcode;
	}

	public String getBloodtypecode() {
		return bloodtypecode;
	}

	public void setBloodtypecode(String bloodtypecode) {
		this.bloodtypecode = bloodtypecode;
	}

	public String getRhbloodcode() {
		return rhbloodcode;
	}

	public void setRhbloodcode(String rhbloodcode) {
		this.rhbloodcode = rhbloodcode;
	}

	public String getPersonnamepycode() {
		return personnamepycode;
	}

	public void setPersonnamepycode(String personnamepycode) {
		this.personnamepycode = personnamepycode;
	}

	public String getNationCode() {
		return nationCode;
	}

	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}

	public String getNationalitycode() {
		return nationalitycode;
	}

	public void setNationalitycode(String nationalitycode) {
		this.nationalitycode = nationalitycode;
	}

	public String getMaritalStatuscode() {
		return maritalStatuscode;
	}

	public void setMaritalStatuscode(String maritalStatuscode) {
		this.maritalStatuscode = maritalStatuscode;
	}

	public String getWorkcode() {
		return workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

	public String getEducationcode() {
		return educationcode;
	}

	public void setEducationcode(String educationcode) {
		this.educationcode = educationcode;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getContacttypecode() {
		return contacttypecode;
	}

	public void setContacttypecode(String contacttypecode) {
		this.contacttypecode = contacttypecode;
	}

	public String getContactno() {
		return contactno;
	}

	public void setContactno(String contactno) {
		this.contactno = contactno;
	}

	public String getStartworkdate() {
		return startworkdate;
	}

	public void setStartworkdate(String startworkdate) {
		this.startworkdate = startworkdate;
	}

	public String getEmergencyidtype() {
		return emergencyidtype;
	}

	public void setEmergencyidtype(String emergencyidtype) {
		this.emergencyidtype = emergencyidtype;
	}

	public String getEmergencyidcard() {
		return emergencyidcard;
	}

	public void setEmergencyidcard(String emergencyidcard) {
		this.emergencyidcard = emergencyidcard;
	}

	public String getEmergencyname() {
		return emergencyname;
	}

	public void setEmergencyname(String emergencyname) {
		this.emergencyname = emergencyname;
	}

	public String getEmergencyrelations() {
		return emergencyrelations;
	}

	public void setEmergencyrelations(String emergencyrelations) {
		this.emergencyrelations = emergencyrelations;
	}

	public String getEmergencycontactno() {
		return emergencycontactno;
	}

	public void setEmergencycontactno(String emergencycontactno) {
		this.emergencycontactno = emergencycontactno;
	}

	public String getSourceid() {
		return sourceid;
	}

	public void setSourceid(String sourceid) {
		this.sourceid = sourceid;
	}

	public String getLocalid() {
		return localid;
	}

	public void setLocalid(String localid) {
		this.localid = localid;
	}

	public String getManageunit() {
		return manageunit;
	}

	public void setManageunit(String manageunit) {
		this.manageunit = manageunit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<CardInfo> getCardInfo() {
		return cardInfo;
	}

	public void setCardInfo(List<CardInfo> cardInfo) {
		this.cardInfo = cardInfo;
	}

	public List<AddressInfo> getAddressInfo() {
		return addressInfo;
	}

	public void setAddressInfo(List<AddressInfo> addressInfo) {
		this.addressInfo = addressInfo;
	}
	
	
	
}


