package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegInfo {
	
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
	@XmlElement(name = "doctorTitle")
	private String doctorTitle;
	/**
	 * 科室代码
	 */
	@XmlElement(name = "deptId")
	private String deptId;
	/**
	 * 科室名称
	 */
	@XmlElement(name = "deptName")
	private String deptName;
	/**
	 * 出诊日期集合
	 */	
	
	@XmlElement(name="timeRegInfoList")
	private List<TimeRegInfoList> timeRegInfoList;
	/**
	 * 号源开始日期
	 */
	private Date startDate;
	/**
	 * 号源结束日期
	 */
	private Date endDate;
			
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
	public String getDoctorTitle() {
		
		return doctorTitle;
	}
	public void setDoctorTitle(String doctorTitle) {
		if(doctorTitle.equals("-1")){
			this.doctorTitle = "";
		}else{			
			this.doctorTitle = doctorTitle;
		}
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
	public List<TimeRegInfoList> getTimeRegInfoList() {
		return timeRegInfoList;
	}
	public void setTimeRegInfoList(List<TimeRegInfoList> timeRegInfoList) {
		this.timeRegInfoList = timeRegInfoList;
	}
	
	
	

}
