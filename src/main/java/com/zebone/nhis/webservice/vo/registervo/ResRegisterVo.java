package com.zebone.nhis.webservice.vo.registervo;

import javax.xml.bind.annotation.XmlElement;

public class ResRegisterVo {
	
	private String pkSch;
	
	private String pkRegistered;
	
	private String pkPatient;
	
	private String codePatient;
	
	private String namePatient;
	
	private String levelPatient;
	
	private String age;
	
	private String dateRegistered;
	
	private String pkDeptRegistered;
	
	private String pkClinicRegistered;
	
	private String pkDoctorRegistered;
	
	private String cateRegistered;
	
	private String cardNo;
	
	private String registeredNo;
	
	private String isPreRegistered;
	
	private String medicalInsurance;
	
	private String flagSch;
	
	private String visiting;
	
	private String cateDept;
	
	private String beginTime;
	
	private String endTime;
	
	@XmlElement(name = "pkSch")
	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	@XmlElement(name = "endTime")
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@XmlElement(name = "beginTime")
	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	@XmlElement(name = "pkRegistered")
	public String getPkRegistered() {
		return pkRegistered;
	}

	public void setPkRegistered(String pkRegistered) {
		this.pkRegistered = pkRegistered;
	}
	
	@XmlElement(name = "pkPatient")
	public String getPkPatient() {
		return pkPatient;
	}

	public void setPkPatient(String pkPatient) {
		this.pkPatient = pkPatient;
	}
    
	@XmlElement(name = "codePatient")
	public String getCodePatient() {
		return codePatient;
	}

	public void setCodePatient(String codePatient) {
		this.codePatient = codePatient;
	}
	
	@XmlElement(name = "namePatient")
	public String getNamePatient() {
		return namePatient;
	}

	public void setNamePatient(String namePatient) {
		this.namePatient = namePatient;
	}
    
	@XmlElement(name = "levelPatient")
	public String getLevelPatient() {
		return levelPatient;
	}

	public void setLevelPatient(String levelPatient) {
		this.levelPatient = levelPatient;
	}
    
	@XmlElement(name = "age")
	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
    
	@XmlElement(name = "dateRegistered")
	public String getDateRegistered() {
		return dateRegistered;
	}

	public void setDateRegistered(String dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
    
	@XmlElement(name = "pkDeptRegistered")
	public String getPkDeptRegistered() {
		return pkDeptRegistered;
	}

	public void setPkDeptRegistered(String pkDeptRegistered) {
		this.pkDeptRegistered = pkDeptRegistered;
	}
    
	@XmlElement(name = "pkClinicRegistered")
	public String getPkClinicRegistered() {
		return pkClinicRegistered;
	}

	public void setPkClinicRegistered(String pkClinicRegistered) {
		this.pkClinicRegistered = pkClinicRegistered;
	}
    
	@XmlElement(name = "pkDoctorRegistered")
	public String getPkDoctorRegistered() {
		return pkDoctorRegistered;
	}

	public void setPkDoctorRegistered(String pkDoctorRegistered) {
		this.pkDoctorRegistered = pkDoctorRegistered;
	}

	@XmlElement(name = "cateRegistered")
	public String getCateRegistered() {
		return cateRegistered;
	}

	public void setCateRegistered(String cateRegistered) {
		this.cateRegistered = cateRegistered;
	}
	
	@XmlElement(name = "cardNo")
	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
    
	@XmlElement(name = "registeredNo")
	public String getRegisteredNo() {
		return registeredNo;
	}

	public void setRegisteredNo(String registeredNo) {
		this.registeredNo = registeredNo;
	}
	
	@XmlElement(name = "isPreRegistered")
	public String getIsPreRegistered() {
		return isPreRegistered;
	}

	public void setIsPreRegistered(String isPreRegistered) {
		this.isPreRegistered = isPreRegistered;
	}
    
	@XmlElement(name = "medicalInsurance")
	public String getMedicalInsurance() {
		return medicalInsurance;
	}

	public void setMedicalInsurance(String medicalInsurance) {
		this.medicalInsurance = medicalInsurance;
	}
	
	@XmlElement(name = "flagSch")
	public String getFlagSch() {
		return flagSch;
	}

	public void setFlagSch(String flagSch) {
		this.flagSch = flagSch;
	}
    
	@XmlElement(name = "visiting")
	public String getVisiting() {
		return visiting;
	}

	public void setVisiting(String visiting) {
		this.visiting = visiting;
	}
    
	@XmlElement(name = "cateDept")
	public String getCateDept() {
		return cateDept;
	}

	public void setCateDept(String cateDept) {
		this.cateDept = cateDept;
	}

	
}
