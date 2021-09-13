package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class DoctorListRes {
    
    /**
     * 医生ID
     */
    @XmlElement(name = "doctorCode")
    private String doctorCode;
    
    /**
     * 医生名称
     */
    @XmlElement(name = "doctotName")
    private String doctotName;
    
    /**
     * 医生职称
     */
    @XmlElement(name = "title")
    private String title;
    
    /**
     * 医生简介
     */
    @XmlElement(name = "introduciton")
    private String introduciton;
    
    /**
     * 固定出诊时间:yyyymmdd
     */
    @XmlElement(name = "scheDate")
    private String scheDate;
    
    /**
     * 医生图片地址
     */
    @XmlElement(name = "photoUrl")
    private String photoUrl;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "deptCode")
    private String deptCode;
    
    /**
     * 科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getDoctotName() {
		return doctotName;
	}

	public void setDoctotName(String doctotName) {
		this.doctotName = doctotName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIntroduciton() {
		return introduciton;
	}

	public void setIntroduciton(String introduciton) {
		this.introduciton = introduciton;
	}

	public String getScheDate() {
		return scheDate;
	}

	public void setScheDate(String scheDate) {
		this.scheDate = scheDate;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
}
