package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "Doctor")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptDoctor {
	//医生编码
	@XmlElement(name="DoctorCode")
	private String DoctorCode;
	//医生名称
	@XmlElement(name="DoctorName")
	private String DoctorName;
	//科室编码
	@XmlElement(name="DepartmentCode")
	private String DepartmentCode;
	//科室名称
	@XmlElement(name="DepartmentName")
	private String DepartmentName;
	//医生简介
	@XmlElement(name="DoctorDesc")
	private String DoctorDesc;
	//医生职称
	@XmlElement(name="DoctorTitle")
	private String DoctorTitle;
	//医生擅长
	@XmlElement(name="DoctorSpec")
	private String DoctorSpec;
	//医生头像,url地址
	@XmlElement(name="DoctorImg")
	private String DoctorImg;
	//医生性别,1：男；2：女
	@XmlElement(name="DoctorGender")
	private String DoctorGender;
	public String getDoctorCode() {
		return DoctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		DoctorCode = doctorCode;
	}
	public String getDoctorName() {
		return DoctorName;
	}
	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}
	public String getDepartmentCode() {
		return DepartmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}
	public String getDepartmentName() {
		return DepartmentName;
	}
	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}
	public String getDoctorDesc() {
		return DoctorDesc;
	}
	public void setDoctorDesc(String doctorDesc) {
		DoctorDesc = doctorDesc;
	}
	public String getDoctorTitle() {
		return DoctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		DoctorTitle = doctorTitle;
	}
	public String getDoctorSpec() {
		return DoctorSpec;
	}
	public void setDoctorSpec(String doctorSpec) {
		DoctorSpec = doctorSpec;
	}
	public String getDoctorImg() {
		return DoctorImg;
	}
	public void setDoctorImg(String doctorImg) {
		DoctorImg = doctorImg;
	}
	public String getDoctorGender() {
		return DoctorGender;
	}
	public void setDoctorGender(String doctorGender) {
		DoctorGender = doctorGender;
	}
	
}
