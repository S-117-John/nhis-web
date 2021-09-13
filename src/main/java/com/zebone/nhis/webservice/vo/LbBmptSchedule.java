package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 排班列表
 * @author tongjiaqi
 *
 */
@XmlRootElement(name = "Schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptSchedule {
	//排班日期
	@XmlElement(name="ServiceDate")
	private String ServiceDate;
	//排班时段,AM或PM
	@XmlElement(name="SessionCode")
	private String SessionCode;
	//排班时段名称,AM：上午PM：下午
	@XmlElement(name="SessionCodeName")
	private String SessionCodeName;
	//科室编码
	@XmlElement(name="DepartmentCode")
	private String DepartmentCode;
	//科室名称
	@XmlElement(name="DepartmentName")
	private String DepartmentName;
	//医生编码
	@XmlElement(name="DoctorCode")
	private String DoctorCode;
	//医生名称
	@XmlElement(name="DoctorName")
	private String DoctorName;
	//医生职称
	@XmlElement(name="DoctorTitle")
	private String DoctorTitle;
	//医生擅长
	@XmlElement(name="DoctorSpec")
	private String DoctorSpec;
	//号类级别返回编号,0：普通号1：专家号
	@XmlElement(name="QueueType")
	private String QueueType;
	//号源总数
	@XmlElement(name="AllNum")
	private String AllNum;
	//挂号费
	@XmlElement(name="AvailableNum")
	private String AvailableNum;
	//挂号费
	@XmlElement(name="Fee")
	private String Fee;
	public String getServiceDate() {
		return ServiceDate;
	}
	public void setServiceDate(String serviceDate) {
		ServiceDate = serviceDate;
	}
	public String getSessionCode() {
		return SessionCode;
	}
	public void setSessionCode(String sessionCode) {
		SessionCode = sessionCode;
	}
	public String getSessionCodeName() {
		return SessionCodeName;
	}
	public void setSessionCodeName(String sessionCodeName) {
		SessionCodeName = sessionCodeName;
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
	public String getQueueType() {
		return QueueType;
	}
	public void setQueueType(String queueType) {
		QueueType = queueType;
	}
	public String getAllNum() {
		return AllNum;
	}
	public void setAllNum(String allNum) {
		AllNum = allNum;
	}
	public String getAvailableNum() {
		return AvailableNum;
	}
	public void setAvailableNum(String availableNum) {
		AvailableNum = availableNum;
	}
	public String getFee() {
		return Fee;
	}
	public void setFee(String fee) {
		Fee = fee;
	}
}
