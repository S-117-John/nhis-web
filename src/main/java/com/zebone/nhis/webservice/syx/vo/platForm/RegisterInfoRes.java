package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "regInfo")
public class RegisterInfoRes {

	/**
     * 医生ID
     */
    @XmlElement(name = "doctorId")
    private String doctorId;
    
    /**
     * 医生名称
     */
    @XmlElement(name = "doctorName")
    private String doctorName;
    
    /**
     * 出诊专科ID
     */
    @XmlElement(name = "deptId")
    private String deptId;
    
    /**
     * 出诊科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;

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

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
    
    
}
