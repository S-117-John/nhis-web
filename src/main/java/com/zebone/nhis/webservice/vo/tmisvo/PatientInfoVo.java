package com.zebone.nhis.webservice.vo.tmisvo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "PatientInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class PatientInfoVo {
	
	//医院His编码
	@XmlElement(name = "HospHISCode")
	private String hospHISCode;
	//病案号
	@XmlElement(name = "CaseNum")
	private String caseNum;
	
	@XmlElement(name = "InHospitalID")
	public String inHospitalID;
	
	@XmlElement(name = "VisitID")
	public String visitID;
	
	@XmlElement(name = "Name")
	public String name;
	
	@XmlElement(name = "DeptHISCode")
	public String deptHISCode;
	
	@XmlElement(name = "BedAreaCode")
	public String bedAreaCode;
	
	@XmlElement(name = "BedNumber")
	public String bedNumber;
	
	@XmlElement(name = "SexID")
	public String sexID;
	
	@XmlElement(name = "MobilePhone")
	public String mobilePhone;
	
	@XmlElement(name = "NationalityID")
	public String nationalityID;
	
	@XmlElement(name = "NationID")
	public String nationID;
	
	@XmlElement(name = "Age")
	public String age;
	
	@XmlElement(name = "AgeTypeID")
	public String ageTypeID;
	
	@XmlElement(name = "AgeText")
	public String ageText;
	
	@XmlElement(name = "CardTypeID")
	public String cardTypeID;
	
	@XmlElement(name = "IDCard")
	public String iDCard;
	
	@XmlElement(name = "ApanageID")
	public String apanageID;
	
	@XmlElement(name = "ABO")
	public String aBO;
	
	@XmlElement(name = "RH")
	public String rH;
	
	@XmlElement(name = "BloodRemark")
	public String bloodRemark;
	
	@XmlElement(name = "HistoryID")
	public String historyID;
	
	@XmlElement(name = "DiagnosisDescribe")
	public String diagnosisDescribe;

	@XmlElement(name = "Pregnant")
	public String pregnant;

	@XmlElement(name = "Birth")
	public String birth;
	
	@XmlElement(name = "IsSubBloodType")
	public String isSubBloodType;
	
	@XmlElement(name = "AntiScreeningResult")
	public String antiScreeningResult;
	
	@XmlElement(name = "Birthday")
	public String birthday;
	
	@XmlElement(name = "UpdateTime")
	public String updateTime;
	
	@XmlElement(name = "AnomalyAntiScreeningResult")
	public String anomalyAntiScreeningResult;
	
	@XmlElement(name = "InHospStatusID")
	public String inHospStatusID;

	public String getHospHISCode() {
		return hospHISCode;
	}

	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}

	public String getCaseNum() {
		return caseNum;
	}

	public void setCaseNum(String caseNum) {
		this.caseNum = caseNum;
	}

	public String getInHospitalID() {
		return inHospitalID;
	}

	public void setInHospitalID(String inHospitalID) {
		this.inHospitalID = inHospitalID;
	}

	public String getVisitID() {
		return visitID;
	}

	public void setVisitID(String visitID) {
		this.visitID = visitID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptHISCode() {
		return deptHISCode;
	}

	public void setDeptHISCode(String deptHISCode) {
		this.deptHISCode = deptHISCode;
	}

	public String getBedAreaCode() {
		return bedAreaCode;
	}

	public void setBedAreaCode(String bedAreaCode) {
		this.bedAreaCode = bedAreaCode;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getSexID() {
		return sexID;
	}

	public void setSexID(String sexID) {
		this.sexID = sexID;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getNationalityID() {
		return nationalityID;
	}

	public void setNationalityID(String nationalityID) {
		this.nationalityID = nationalityID;
	}

	public String getNationID() {
		return nationID;
	}

	public void setNationID(String nationID) {
		this.nationID = nationID;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getAgeTypeID() {
		return ageTypeID;
	}

	public void setAgeTypeID(String ageTypeID) {
		this.ageTypeID = ageTypeID;
	}

	public String getAgeText() {
		return ageText;
	}

	public void setAgeText(String ageText) {
		this.ageText = ageText;
	}

	public String getCardTypeID() {
		return cardTypeID;
	}

	public void setCardTypeID(String cardTypeID) {
		this.cardTypeID = cardTypeID;
	}

	public String getiDCard() {
		return iDCard;
	}

	public void setiDCard(String iDCard) {
		this.iDCard = iDCard;
	}

	public String getApanageID() {
		return apanageID;
	}

	public void setApanageID(String apanageID) {
		this.apanageID = apanageID;
	}

	public String getaBO() {
		return aBO;
	}

	public void setaBO(String aBO) {
		this.aBO = aBO;
	}

	public String getrH() {
		return rH;
	}

	public void setrH(String rH) {
		this.rH = rH;
	}

	public String getBloodRemark() {
		return bloodRemark;
	}

	public void setBloodRemark(String bloodRemark) {
		this.bloodRemark = bloodRemark;
	}

	public String getHistoryID() {
		return historyID;
	}

	public void setHistoryID(String historyID) {
		this.historyID = historyID;
	}

	public String getDiagnosisDescribe() {
		return diagnosisDescribe;
	}

	public void setDiagnosisDescribe(String diagnosisDescribe) {
		this.diagnosisDescribe = diagnosisDescribe;
	}

	public String getPregnant() {
		return pregnant;
	}

	public void setPregnant(String pregnant) {
		this.pregnant = pregnant;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getIsSubBloodType() {
		return isSubBloodType;
	}

	public void setIsSubBloodType(String isSubBloodType) {
		this.isSubBloodType = isSubBloodType;
	}

	public String getAntiScreeningResult() {
		return antiScreeningResult;
	}

	public void setAntiScreeningResult(String antiScreeningResult) {
		this.antiScreeningResult = antiScreeningResult;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getAnomalyAntiScreeningResult() {
		return anomalyAntiScreeningResult;
	}

	public void setAnomalyAntiScreeningResult(String anomalyAntiScreeningResult) {
		this.anomalyAntiScreeningResult = anomalyAntiScreeningResult;
	}

	public String getInHospStatusID() {
		return inHospStatusID;
	}

	public void setInHospStatusID(String inHospStatusID) {
		this.inHospStatusID = inHospStatusID;
	}
	
	
	
}
