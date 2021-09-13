package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DeptRegInfo {
	/**
	 * 科室
	 */
	@XmlElement(name = "deptId")
	private String deptId;
	/**
	 * 号源开始日期
	 */
	@XmlElement(name = "startDate")
	private Date startDate; 
	/**
	 * 号源结束日期
	 */
	@XmlElement(name = "endDate")
	private Date endDate;
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
	 * 医生职称
	 */
	@XmlElement(name = "title")
	private String title;
	
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
