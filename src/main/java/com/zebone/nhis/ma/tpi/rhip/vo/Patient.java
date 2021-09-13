
package com.zebone.nhis.ma.tpi.rhip.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name = "Patient")
@XmlAccessorType(XmlAccessType.FIELD)
public class Patient {

    @XmlElement(name = "PersonName", required = true)
    private String personName;
    @XmlElement(name = "SexCode", required = true)
    private String sexCode;
    @XmlElement(name = "Birthday", required = true)
    private String birthday;
    @XmlElement(name = "IdCard")
    private String idCard;
    @XmlElement(name = "IdType")
    private String idType;
    @XmlElement(name = "CardNo")
    private String cardNo;
    @XmlElement(name = "CardType")
    private String cardType;
    @XmlElement(name = "RegisteredPermanent")
    private String registeredPermanent;
    @XmlElement(name = "AddressType")
    private String addressType;
    @XmlElement(name = "Address")
    private String address;
    @XmlElement(name = "ContactNo")
    private String contactNo;
    @XmlElement(name = "NationalityCode")
    private String nationalityCode;
    @XmlElement(name = "NationCode")
    private String nationCode;
    @XmlElement(name = "BloodTypeCode")
    private String bloodTypeCode;
    @XmlElement(name = "RhBloodCode")
    private String rhBloodCode;
    @XmlElement(name = "MaritalStatusCode")
    private String maritalStatusCode;
    @XmlElement(name = "StartWorkDate")
    private String startWorkDate;
    @XmlElement(name = "WorkCode")
    private String workCode;
    @XmlElement(name = "EducationCode")
    private String educationCode;
    @XmlElement(name = "InsuranceCode")
    private String insuranceCode;
    @XmlElement(name = "InsuranceType")
    private String insuranceType;
    @XmlElement(name = "WorkPlace")
    private String workPlace;
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public void setSexCode(String sexCode) {
		this.sexCode = sexCode;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public void setRegisteredPermanent(String registeredPermanent) {
		this.registeredPermanent = registeredPermanent;
	}
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	public void setNationalityCode(String nationalityCode) {
		this.nationalityCode = nationalityCode;
	}
	public void setNationCode(String nationCode) {
		this.nationCode = nationCode;
	}
	public void setBloodTypeCode(String bloodTypeCode) {
		this.bloodTypeCode = bloodTypeCode;
	}
	public void setRhBloodCode(String rhBloodCode) {
		this.rhBloodCode = rhBloodCode;
	}
	public void setMaritalStatusCode(String maritalStatusCode) {
		this.maritalStatusCode = maritalStatusCode;
	}
	public void setStartWorkDate(String startWorkDate) {
		this.startWorkDate = startWorkDate;
	}
	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}
	public void setEducationCode(String educationCode) {
		this.educationCode = educationCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public void setInsuranceType(String insuranceType) {
		this.insuranceType = insuranceType;
	}
	public void setWorkPlace(String workPlace) {
		this.workPlace = workPlace;
	}
    

    
}
