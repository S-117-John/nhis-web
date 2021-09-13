package com.zebone.nhis.common.module.ma.tpi.ems;

import java.math.BigDecimal;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * JG PACS申请
 * @author chengjia
 *
 */
@Table(value="Request")
public class JgPacsReq {
	
	@Field(value="PatientID")
	private String PatientID;

	@Field(value="Name")
	private String Name;
	
	@Field(value="NamePY")
	private String NamePY;
	
	@Field(value="Sex")
	private String Sex;
	
	@Field(value="Birthday")
	private String Birthday;
	
	@Field(value="CertificateID")
	private String CertificateID;
	
	@Field(value="Address")
	private String Address;
	
	@Field(value="Telephone")
	private String Telephone;
	
	@Field(value="HISSheetID")
	private String HISSheetID;
	
	@Field(value="FeeTypeID")
	private String FeeTypeID;
	
	@Field(value="ExamTypeID")
	private String ExamTypeID;
	
	@Field(value="ProjectID")
	private String ProjectID;
	
	@Field(value="ProjectName")
	private String ProjectName;
	
	@Field(value="ExamPart")
	private String ExamPart;
	
	@Field(value="DepartmentName")
	private String DepartmentName;
	
	@Field(value="ExamDepartmentID")
	private String ExamDepartmentID;
	
	@Field(value="PatientSource")
	private String PatientSource;
	
	@Field(value="Emergency")
	private String Emergency;
	
	@Field(value="Confidentiality")
	private String Confidentiality;
	
	@Field(value="IsPhysicalCheck")
	private String IsPhysicalCheck;
	
	@Field(value="ReportReturnToDepartmentName")
	private String ReportReturnToDepartmentName;
	
	@Field(value="DoctorName")
	private String DoctorName;
	
	@Field(value="ClinicDiagnose")
	private String ClinicDiagnose;
	
	@Field(value="DoctorDescription")
	private String DoctorDescription;
	
	@Field(value="ExamReason")
	private String ExamReason;
	
	@Field(value="ReqDateTime")
	private String ReqDateTime;
	
	@Field(value="HosArea")
	private String HosArea;
	
	@Field(value="BedNum")
	private String BedNum;
	
	@Field(value="IsBaby")
	private String IsBaby;
	
	@Field(value="NewRequest")
	private String NewRequest;
	
	@Field(value="Fee")
	private Double Fee;

	@Field(value="InOrOutNum")
	private String InOrOutNum;
	
	public String getPatientID() {
		return PatientID;
	}

	public void setPatientID(String patientID) {
		PatientID = patientID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNamePY() {
		return NamePY;
	}

	public void setNamePY(String namePY) {
		NamePY = namePY;
	}

	public String getSex() {
		return Sex;
	}

	public void setSex(String sex) {
		Sex = sex;
	}

	public String getBirthday() {
		return Birthday;
	}

	public void setBirthday(String birthday) {
		Birthday = birthday;
	}

	public String getCertificateID() {
		return CertificateID;
	}

	public void setCertificateID(String certificateID) {
		CertificateID = certificateID;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getTelephone() {
		return Telephone;
	}

	public void setTelephone(String telephone) {
		Telephone = telephone;
	}

	public String getHISSheetID() {
		return HISSheetID;
	}

	public void setHISSheetID(String hISSheetID) {
		HISSheetID = hISSheetID;
	}

	public String getFeeTypeID() {
		return FeeTypeID;
	}

	public void setFeeTypeID(String feeTypeID) {
		FeeTypeID = feeTypeID;
	}

	public String getExamTypeID() {
		return ExamTypeID;
	}

	public void setExamTypeID(String examTypeID) {
		ExamTypeID = examTypeID;
	}

	public String getProjectID() {
		return ProjectID;
	}

	public void setProjectID(String projectID) {
		ProjectID = projectID;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getExamPart() {
		return ExamPart;
	}

	public void setExamPart(String examPart) {
		ExamPart = examPart;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

	public String getExamDepartmentID() {
		return ExamDepartmentID;
	}

	public void setExamDepartmentID(String examDepartmentID) {
		ExamDepartmentID = examDepartmentID;
	}

	public String getPatientSource() {
		return PatientSource;
	}

	public void setPatientSource(String patientSource) {
		PatientSource = patientSource;
	}

	public String getEmergency() {
		return Emergency;
	}

	public void setEmergency(String emergency) {
		Emergency = emergency;
	}

	public String getConfidentiality() {
		return Confidentiality;
	}

	public void setConfidentiality(String confidentiality) {
		Confidentiality = confidentiality;
	}

	public String getIsPhysicalCheck() {
		return IsPhysicalCheck;
	}

	public void setIsPhysicalCheck(String isPhysicalCheck) {
		IsPhysicalCheck = isPhysicalCheck;
	}

	public String getReportReturnToDepartmentName() {
		return ReportReturnToDepartmentName;
	}

	public void setReportReturnToDepartmentName(String reportReturnToDepartmentName) {
		ReportReturnToDepartmentName = reportReturnToDepartmentName;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public String getClinicDiagnose() {
		return ClinicDiagnose;
	}

	public void setClinicDiagnose(String clinicDiagnose) {
		ClinicDiagnose = clinicDiagnose;
	}

	public String getDoctorDescription() {
		return DoctorDescription;
	}

	public void setDoctorDescription(String doctorDescription) {
		DoctorDescription = doctorDescription;
	}

	public String getExamReason() {
		return ExamReason;
	}

	public void setExamReason(String examReason) {
		ExamReason = examReason;
	}

	

	public String getHosArea() {
		return HosArea;
	}

	public void setHosArea(String hosArea) {
		HosArea = hosArea;
	}

	public String getBedNum() {
		return BedNum;
	}

	public void setBedNum(String bedNum) {
		BedNum = bedNum;
	}

	public String getIsBaby() {
		return IsBaby;
	}

	public void setIsBaby(String isBaby) {
		IsBaby = isBaby;
	}

	public String getNewRequest() {
		return NewRequest;
	}

	public void setNewRequest(String newRequest) {
		NewRequest = newRequest;
	}

	public Double getFee() {
		return Fee;
	}

	public void setFee(Double fee) {
		Fee = fee;
	}

	public String getReqDateTime() {
		return ReqDateTime;
	}

	public void setReqDateTime(String reqDateTime) {
		ReqDateTime = reqDateTime;
	}

	public String getInOrOutNum() {
		return InOrOutNum;
	}

	public void setInOrOutNum(String inOrOutNum) {
		InOrOutNum = inOrOutNum;
	}
	
}