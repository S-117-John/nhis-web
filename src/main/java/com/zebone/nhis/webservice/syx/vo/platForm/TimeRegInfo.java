package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class TimeRegInfo {
	/**
	 * 时段名称
	 */
	@XmlElement(name = "timeName")
	private String timeName;
	/**
	 * 时段ID
	 */
	@XmlElement(name = "timeId")
	private String timeId;
	/**
	 * 出诊状态
	 */
	@XmlElement(name = "statusType")
	private String statusType;
	/**
	 * 该时段可预约的总号源数
	 */
	@XmlElement(name = "regTotalCount")
	private String regTotalCount;
	/**
	 * 该时段剩余号源数
	 */
	@XmlElement(name = "regLeaveCount")
	private String regLeaveCount;
	/**
	 * 挂号费
	 */
	@XmlElement(name = "regFee")
	private String regFee;
	/**
	 * 诊疗费
	 */
	@XmlElement(name = "treatFee")
	private String treatFee;
	/**
	 * 是否有分时
	 */
	@XmlElement(name = "isTimeReg")
	private String isTimeReg;
	
	/**
	 * 科室
	 */
	@XmlElement(name = "deptId")
	private String deptId;
	
	/**
	 * 医生
	 */
	@XmlElement(name = "doctorId")
	private String doctorId;
	
	/**
	 * 出诊日期
	 */
	private Date regDate;
	/**
	 * 开始时间
	 */
	@XmlElement(name = "startTime")
    private String startTime;
    /**
     * 结束时间
     */
	@XmlElement(name = "endTime")
    private String endTime;	
	
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
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTimeName() {
		return timeName;
	}
	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}
	public String getTimeId() {
		return timeId;
	}
	public void setTimeId(String timeId) {
		this.timeId = timeId;
	}
	public String getStatusType() {
		return statusType;
	}
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}
	public String getRegTotalCount() {
		return regTotalCount;
	}
	public void setRegTotalCount(String regTotalCount) {
		this.regTotalCount = regTotalCount;
	}
	public String getRegLeaveCount() {
		return regLeaveCount;
	}
	public void setRegLeaveCount(String regLeaveCount) {
		this.regLeaveCount = regLeaveCount;
	}
	public String getRegFee() {
		return regFee;
	}
	public void setRegFee(String regFee) {
		this.regFee = regFee;
	}
	public String getTreatFee() {
		return treatFee;
	}
	public void setTreatFee(String treatFee) {
		this.treatFee = treatFee;
	}
	public String getIsTimeReg() {
		return isTimeReg;
	}
	public void setIsTimeReg(String isTimeReg) {
		this.isTimeReg = isTimeReg;
	}
	
	

}
