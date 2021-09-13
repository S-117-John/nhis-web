package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "OrderInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptOrderInfo {
	//预约订单编号
	@XmlElement(name="OrderCode")
	private String OrderCode;
	//就诊开始时间yyyy-MM-dd hh:mm:ss
	@XmlElement(name="StartDate")
	private String StartDate;
	//就诊结束时间yyyy-MM-dd hh:mm:ss
	@XmlElement(name="EndDate")
	private String EndDate;
	//排班时段代码
	@XmlElement(name="SessionCode")
	private String SessionCode;
	//创建时间
	@XmlElement(name="OrderDate")
	private String OrderDate;
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
	//总费用
	@XmlElement(name="RegFee")
	private String RegFee;
	//患者姓名
	@XmlElement(name="PatientName")
	private String PatientName;
	//手机号
	@XmlElement(name="Phone")
	private String Phone;
	//订单状态返回编号	,0：预约；1：已就诊；2：作废；3：爽约；4：停诊
	@XmlElement(name="OrderStatus")
	private String OrderStatus;
	public String getOrderCode() {
		return OrderCode;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public String getStartDate() {
		return StartDate;
	}
	public void setStartDate(String startDate) {
		StartDate = startDate;
	}
	public String getEndDate() {
		return EndDate;
	}
	public void setEndDate(String endDate) {
		EndDate = endDate;
	}
	public String getSessionCode() {
		return SessionCode;
	}
	public void setSessionCode(String sessionCode) {
		SessionCode = sessionCode;
	}
	public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
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
	public String getRegFee() {
		return RegFee;
	}
	public void setRegFee(String regFee) {
		RegFee = regFee;
	}
	public String getPatientName() {
		return PatientName;
	}
	public void setPatientName(String patientName) {
		PatientName = patientName;
	}
	public String getPhone() {
		return Phone;
	}
	public void setPhone(String phone) {
		Phone = phone;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	
}
