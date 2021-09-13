package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="userInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResIpUserInfo {
	@XmlElement(name="orgAreaCode")
	private String orgAreaCode;
	
	@XmlElement(name="patientId")
	private String patientId;
	
	@XmlElement(name="inPatientID")
	private String inPatientID;

	@XmlElement(name="pkPi")
	private String pkPi;
	
	@XmlElement(name="ipSeqnoText")
	private String ipSeqnoText;
	
	@XmlElement(name="patientName")
	private String patientName;
	
	@XmlElement(name="sexFlag")
	private String sexFlag;
	
	@XmlElement(name="ipTimes")
	private String ipTimes;
	
	@XmlElement(name="doctorEmployeeCode")
	private String doctorEmployeeCode;
	
	@XmlElement(name="doctorEmployeeName")
	private String doctorEmployeeName;
	
	@XmlElement(name="departmentCode")
	private String departmentCode;
	
	@XmlElement(name="departmentName")
	private String departmentName;
	
	@XmlElement(name="sickbedNo")
	private String sickbedNo;
	
	@XmlElement(name="ipFlag")
	private String ipFlag;
	
	@XmlElement(name="inDateTime")
	private String inDateTime;
	
	@XmlElement(name="outDate")
	private String outDate;
	
	@XmlElement(name="inDays")
	private String inDays;
	
	@XmlElement(name="idCardNo")
	private String idCardNo;
	
	@XmlElement(name="nurseCode")
	private String nurseCode;
	
	@XmlElement(name="nurseName")
	private String nurseName;
	
	@XmlElement(name="otherDesc")
	private String otherDesc;
	
	@XmlElement(name="totalAmount")
	private String totalAmount;

	@XmlElement(name="descDiag")
	private String descDiag;

	@XmlElement(name="codeDeptNs")
	private String codeDeptNs;

	@XmlElement(name="nameDeptNs")
	private String nameDeptNs;

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getDescDiag() {
		return descDiag;
	}

	public void setDescDiag(String descDiag) {
		this.descDiag = descDiag;
	}

	public String getCodeDeptNs() {
		return codeDeptNs;
	}

	public void setCodeDeptNs(String codeDeptNs) {
		this.codeDeptNs = codeDeptNs;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getOrgAreaCode() {
		return orgAreaCode;
	}

	public void setOrgAreaCode(String orgAreaCode) {
		this.orgAreaCode = orgAreaCode;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getInPatientID() {
		return inPatientID;
	}

	public void setInPatientID(String inPatientID) {
		this.inPatientID = inPatientID;
	}

	public String getIpSeqnoText() {
		return ipSeqnoText;
	}

	public void setIpSeqnoText(String ipSeqnoText) {
		this.ipSeqnoText = ipSeqnoText;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getSexFlag() {
		return sexFlag;
	}

	public void setSexFlag(String sexFlag) {
		this.sexFlag = sexFlag;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getDoctorEmployeeCode() {
		return doctorEmployeeCode;
	}

	public void setDoctorEmployeeCode(String doctorEmployeeCode) {
		this.doctorEmployeeCode = doctorEmployeeCode;
	}

	public String getDoctorEmployeeName() {
		return doctorEmployeeName;
	}

	public void setDoctorEmployeeName(String doctorEmployeeName) {
		this.doctorEmployeeName = doctorEmployeeName;
	}

	
	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getSickbedNo() {
		return sickbedNo;
	}

	public void setSickbedNo(String sickbedNo) {
		this.sickbedNo = sickbedNo;
	}

	public String getIpFlag() {
		return ipFlag;
	}

	public void setIpFlag(String ipFlag) {
		this.ipFlag = ipFlag;
	}

	public String getInDateTime() {
		return inDateTime;
	}

	public void setInDateTime(String inDateTime) {
		this.inDateTime = inDateTime;
	}

	public String getOutDate() {
		return outDate;
	}

	public void setOutDate(String outDate) {
		this.outDate = outDate;
	}

	public String getInDays() {
		return inDays;
	}

	public void setInDays(String inDays) {
		this.inDays = inDays;
	}

	public String getIdCardNo() {
		return idCardNo;
	}

	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}

	public String getNurseCode() {
		return nurseCode;
	}

	public void setNurseCode(String nurseCode) {
		this.nurseCode = nurseCode;
	}

	public String getNurseName() {
		return nurseName;
	}

	public void setNurseName(String nurseName) {
		this.nurseName = nurseName;
	}

	public String getOtherDesc() {
		return otherDesc;
	}

	public void setOtherDesc(String otherDesc) {
		this.otherDesc = otherDesc;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	
}
