package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OpvisitInfoVo {
	
	@XmlElement(name="visitId")
	private String visitId;
	
	@XmlElement(name="patientId")
	private String patientId;
	
	@XmlElement(name="deptKey")
	private String deptKey;
	
	@XmlElement(name="deptName")
	private String deptName;
	
	@XmlElement(name="doctor")
	private String doctor;
	
	@XmlElement(name="visitDate")
	private String visitDate;
	
	@XmlElement(name="patientName")
	private String patientName;
	
	@XmlElement(name="sex")
	private String sex;
	
	@XmlElement(name="birthday")
	private String birthday;
	
	@XmlElement(name="age")
	private String age;
	
	@XmlElement(name="clinicNo")
	private String clinicNo;
	
	@XmlElement(name="clinicCardNo")
	private String clinicCardNo;
	
	@XmlElement(name="idNumber")
	private String idNumber;
	
	@XmlElement(name="telphone")
	private String telphone;
	
	@XmlElement(name="maritalStatus")
	private String maritalStatus;
	
	@XmlElement(name="vocation")
	private String vocation;
	
	@XmlElement(name="nationality")
	private String nationality;
	
	@XmlElement(name="nation")
	private String nation;
	
	@XmlElement(name="workUnit")
	private String workUnit;
	
	@XmlElement(name="education")
	private String education;
	
	@XmlElement(name="presentAddrProvince")
    private String presentAddrProvince;
    
	@XmlElement(name="presentAddrCity")
    private String presentAddrCity;	
    
	@XmlElement(name="presentAddrArea")
    private String presentAddrArea;
    
	@XmlElement(name="presentAddrDetail")
    private String presentAddrDetail;
	
	@XmlElement(name="resideAddrProv")
    private String resideAddrProv;
	
	@XmlElement(name="resideAddrCity")
    private String resideAddrCity;
	
	@XmlElement(name="resideAddrArea")
    private String resideAddrArea;
	
	@XmlElement(name="resideAddrDetail")
    private String resideAddrDetail;
    
	@XmlElement(name="rstatus")
    private String rstatus;
	
	@XmlElement(name="dateSource")
	private String dateSource;
	
	
	public String getDateSource() {
		return dateSource;
	}

	public void setDateSource(String dateSource) {
		this.dateSource = dateSource;
	}

	public String getVisitId() {
		return visitId;
	}

	public void setVisitId(String visitId) {
		this.visitId = visitId;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getDeptKey() {
		return deptKey;
	}

	public void setDeptKey(String deptKey) {
		this.deptKey = deptKey;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctor() {
		return doctor;
	}

	public void setDoctor(String doctor) {
		this.doctor = doctor;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getClinicNo() {
		return clinicNo;
	}

	public void setClinicNo(String clinicNo) {
		this.clinicNo = clinicNo;
	}

	public String getClinicCardNo() {
		return clinicCardNo;
	}

	public void setClinicCardNo(String clinicCardNo) {
		this.clinicCardNo = clinicCardNo;
	}

	public String getIdNumber() {
		return idNumber;
	}

	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getVocation() {
		return vocation;
	}

	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getPresentAddrProvince() {
		return presentAddrProvince;
	}

	public void setPresentAddrProvince(String presentAddrProvince) {
		this.presentAddrProvince = presentAddrProvince;
	}

	public String getPresentAddrCity() {
		return presentAddrCity;
	}

	public void setPresentAddrCity(String presentAddrCity) {
		this.presentAddrCity = presentAddrCity;
	}

	public String getResideAddrProv() {
		return resideAddrProv;
	}

	public void setResideAddrProv(String resideAddrProv) {
		this.resideAddrProv = resideAddrProv;
	}

	public String getResideAddrCity() {
		return resideAddrCity;
	}

	public void setResideAddrCity(String resideAddrCity) {
		this.resideAddrCity = resideAddrCity;
	}

	public String getResideAddrArea() {
		return resideAddrArea;
	}

	public void setResideAddrArea(String resideAddrArea) {
		this.resideAddrArea = resideAddrArea;
	}

	public String getResideAddrDetail() {
		return resideAddrDetail;
	}

	public void setResideAddrDetail(String resideAddrDetail) {
		this.resideAddrDetail = resideAddrDetail;
	}

	public String getPresentAddrArea() {
		return presentAddrArea;
	}

	public void setPresentAddrArea(String presentAddrArea) {
		this.presentAddrArea = presentAddrArea;
	}

	public String getPresentAddrDetail() {
		return presentAddrDetail;
	}

	public void setPresentAddrDetail(String presentAddrDetail) {
		this.presentAddrDetail = presentAddrDetail;
	}

	public String getRstatus() {
		return rstatus;
	}

	public void setRstatus(String rstatus) {
		this.rstatus = rstatus;
	}
		
}
