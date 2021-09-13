package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DoctorInfo {
	
	/**
     * 科室代码，如果科室代码为“-2”则相当于查询参与外部预约挂号的所有门诊专科的科室信息
     */
    @XmlElement(name = "deptId")
    private String deptId;
    
	/**
     * 医生代码
     */
    @XmlElement(name = "doctorId")
    private String doctorId;
    
    /**
     * 医生名称
     */
    @XmlElement(name = "doctorName", required = true)
    private String doctorName;
    
    /**
     * 医生拼音码
     */
    @XmlElement(name = "doctorSpellCode")
    private String doctorSpellCode;
    
    /**
     * 医生职称
     */
    @XmlElement(name = "title")
    private String title;
    
    /**
     * 医生性别 M-男性 F-女性
     */
    @XmlElement(name = "gender")
    private String gender;
    
    /**
     * 医生简介
     */
    @XmlElement(name = "drDesc")
    private String drDesc;
    
    /**
     * 医生登录名LoginName
     */
    @XmlElement(name = "loginName")
    private String loginName;

    
    
    
	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(String doctorId) {
		this.doctorId = doctorId;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDoctorSpellCode() {
		return doctorSpellCode;
	}

	public void setDoctorSpellCode(String doctorSpellCode) {
		this.doctorSpellCode = doctorSpellCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDrDesc() {
		return drDesc;
	}

	public void setDrDesc(String drDesc) {
		this.drDesc = drDesc;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
    
    
    

}
