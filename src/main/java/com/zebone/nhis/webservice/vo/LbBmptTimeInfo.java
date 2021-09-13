package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "TimeInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptTimeInfo {
	//号源唯一标识
	@XmlElement(name="SourceID")
	private String SourceID;
	//号源日期
	@XmlElement(name="ServiceDate")
	private String ServiceDate;
	//科室编码
	@XmlElement(name="DepartmentCode")
	private String DepartmentCode;
	//医生编码
	@XmlElement(name="DoctorCode")
	private String DoctorCode;
	//号源就诊开始时间
	@XmlElement(name="StartTime")
	private String StartTime;
	//号源就诊结束时间
	@XmlElement(name="EndTime")
	private String EndTime;
	//该时段可预约数量
	@XmlElement(name="AvailableNum")
	private String AvailableNum;
	//该时段起始诊号
	@XmlElement(name="Num")
	private String Num;
	public String getSourceID() {
		return SourceID;
	}
	public void setSourceID(String sourceID) {
		SourceID = sourceID;
	}
	public String getServiceDate() {
		return ServiceDate;
	}
	public void setServiceDate(String serviceDate) {
		ServiceDate = serviceDate;
	}
	public String getDepartmentCode() {
		return DepartmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}
	public String getDoctorCode() {
		return DoctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		DoctorCode = doctorCode;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public String getAvailableNum() {
		return AvailableNum;
	}
	public void setAvailableNum(String availableNum) {
		AvailableNum = availableNum;
	}
	public String getNum() {
		return Num;
	}
	public void setNum(String num) {
		Num = num;
	}
}
