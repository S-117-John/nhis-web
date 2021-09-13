package com.zebone.nhis.webservice.vo.tmisvo.employee;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 输血返回构造xml
 * @author frank
 *职工信息
 */
@XmlRootElement(name = "SickRoomInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ResponseStaffInfoVo {
	@XmlElement(name = "HospHISCode")
	public String hospHISCode;
	
	@XmlElement(name = "HospName")
	public String hospName;
	
	@XmlElement(name = "Name")
	public String name;
	
	@XmlElement(name = "Code")
	public String code;
	
	@XmlElement(name = "DeptCode")
	public String deptCode;
	
	@XmlElement(name = "DoctorTypeID")
	public String doctorTypeID;
	
	@XmlElement(name = "SexID")
	public String sexID;
	
	public String getSexID() {
		return sexID;
	}
	public void setSexID(String sexID) {
		this.sexID = sexID;
	}
	public String getHospHISCode() {
		return hospHISCode;
	}
	public void setHospHISCode(String hospHISCode) {
		this.hospHISCode = hospHISCode;
	}
	public String getHospName() {
		return hospName;
	}
	public void setHospName(String hospName) {
		this.hospName = hospName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public String getDoctorTypeID() {
		return doctorTypeID;
	}
	public void setDoctorTypeID(String doctorTypeID) {
		this.doctorTypeID = doctorTypeID;
	}
	
	
}
