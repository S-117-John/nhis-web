package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptRequest {
	//医院唯一标识
	@XmlElement(name="HospitalMark")
	private String HospitalMark;
	//开始日期
	@XmlElement(name="StartDate")
	private String StartDate;
	//结束日期
	@XmlElement(name="EndDate")
	private String EndDate;
	//科室编码
	@XmlElement(name="DepartmentCode")
	private String DepartmentCode;
	//排班时段代码
	@XmlElement(name="SessionCode")
	private String SessionCode;
	//医生编码
	@XmlElement(name="DoctorCode")
	private String DoctorCode;
	//号源日期
	@XmlElement(name="ServiceDate")
	private String ServiceDate;
	//就诊开始时间
	@XmlElement(name="StartTime")
	private String StartTime;
	//就诊结束时间
	@XmlElement(name="EndTime")
	private String EndTime;
	//患者姓名
	@XmlElement(name="PatientName")
	private String PatientName;
	//手机号
	@XmlElement(name="Phone")
	private String Phone;
	//身份证
	@XmlElement(name="CardNo")
	private String CardNo;
	//预约订单编号
	@XmlElement(name="OrderCode")
	private String OrderCode;
	//日期类型
	@XmlElement(name="DateFlag")
	private String DateFlag;
	public String getHospitalMark() {
		return HospitalMark;
	}
	public void setHospitalMark(String hospitalMark) {
		HospitalMark = hospitalMark;
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
	public String getDepartmentCode() {
		return DepartmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		DepartmentCode = departmentCode;
	}
	public String getSessionCode() {
		return SessionCode;
	}
	public void setSessionCode(String sessionCode) {
		SessionCode = sessionCode;
	}
	public String getDoctorCode() {
		return DoctorCode;
	}
	public void setDoctorCode(String doctorCode) {
		DoctorCode = doctorCode;
	}
	public String getServiceDate() {
		return ServiceDate;
	}
	public void setServiceDate(String serviceDate) {
		ServiceDate = serviceDate;
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
	public String getCardNo() {
		return CardNo;
	}
	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}
	public String getOrderCode() {
		return OrderCode;
	}
	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}
	public String getDateFlag() {
		return DateFlag;
	}
	public void setDateFlag(String dateFlag) {
		DateFlag = dateFlag;
	}
}
