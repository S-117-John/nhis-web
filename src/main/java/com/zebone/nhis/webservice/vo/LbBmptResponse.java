package com.zebone.nhis.webservice.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 灵璧-便民平台-响应类
 * @author Tongjiaqi
 *
 */
@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbBmptResponse {
	//交易结果：0000：成功
	@XmlElement(name = "ResultCode")
	private String ResultCode;
	//交易结果描述
	@XmlElement(name = "ResultMsg")
	private String ResultMsg;
	//循环总次数
	@XmlElement(name = "CycleNum")
	private int CycleNum;
	//科室集
	@XmlElementWrapper(name = "Departments")
	@XmlElement(name = "Department")
	private List<LbBmptDepartment> Department;
	//排班列表
	@XmlElementWrapper(name = "Schedules")
	@XmlElement(name = "Schedule")
	private List<LbBmptSchedule> Schedule;
	//医生可预约时段列表
	@XmlElementWrapper(name = "TimeInfos")
	@XmlElement(name = "TimeInfo")
    private List<LbBmptTimeInfo> TimeInfo;
	//预约记录列表
    @XmlElementWrapper(name = "OrderInfos")
    @XmlElement(name = "OrderInfo")
	private List<LbBmptOrderInfo> OrderInfo;
	//医生列表
	@XmlElementWrapper(name = "Doctors")
	@XmlElement(name = "Doctor")
	private List<LbBmptDoctor> Doctor;
	
	//预约订单编号
	@XmlElement(name = "OrderCode")
	private String OrderCode;
	//身份证
	@XmlElement(name = "CardNo")
	private String CardNo;
	//就诊日期yyyy-MM-dd
	@XmlElement(name = "ServiceDate")
	private String ServiceDate;
	//就诊时间段hh:mm:ss-hh:mm:ss
	@XmlElement(name = "RangeTime")
	private String RangeTime;
	//挂号费
	@XmlElement(name = "Fee")
	private String Fee;
	//诊号
	@XmlElement(name = "Num")
	private String Num;
	//患者姓名
	@XmlElement(name = "PatientName")
	private String PatientName;
	//科室名称
	@XmlElement(name = "DepartmentName")
	private String DepartmentName;
	//医生名称
	@XmlElement(name = "DoctorName")
	private String DoctorName;
	
	public String getResultCode() {
		return ResultCode;
	}

	public void setResultCode(String resultCode) {
		ResultCode = resultCode;
	}

	public String getResultMsg() {
		return ResultMsg;
	}

	public void setResultMsg(String resultMsg) {
		ResultMsg = resultMsg;
	}

	public int getCycleNum() {
		return CycleNum;
	}

	public void setCycleNum(int cycleNum) {
		CycleNum = cycleNum;
	} 

	public String getOrderCode() {
		return OrderCode;
	}

	public void setOrderCode(String orderCode) {
		OrderCode = orderCode;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getServiceDate() {
		return ServiceDate;
	}

	public void setServiceDate(String serviceDate) {
		ServiceDate = serviceDate;
	}

	public String getRangeTime() {
		return RangeTime;
	}

	public void setRangeTime(String rangeTime) {
		RangeTime = rangeTime;
	}

	public String getFee() {
		return Fee;
	}

	public void setFee(String fee) {
		Fee = fee;
	}

	public String getNum() {
		return Num;
	}

	public void setNum(String num) {
		Num = num;
	}

	public String getPatientName() {
		return PatientName;
	}

	public void setPatientName(String patientName) {
		PatientName = patientName;
	}

	public String getDepartmentName() {
		return DepartmentName;
	}

	public void setDepartmentName(String departmentName) {
		DepartmentName = departmentName;
	}

	public String getDoctorName() {
		return DoctorName;
	}

	public void setDoctorName(String doctorName) {
		DoctorName = doctorName;
	}

	public List<LbBmptDoctor> getDoctor() {
		return Doctor;
	}

	public void setDoctor(List<LbBmptDoctor> doctor) {
		Doctor = doctor;
	}

	public List<LbBmptDepartment> getDepartment() {
		return Department;
	}

	public void setDepartment(List<LbBmptDepartment> department) {
		Department = department;
	}

	public List<LbBmptSchedule> getSchedule() {
		return Schedule;
	}

	public void setSchedule(List<LbBmptSchedule> schedule) {
		Schedule = schedule;
	}

	public List<LbBmptTimeInfo> getTimeInfo() {
		return TimeInfo;
	}

	public void setTimeInfo(List<LbBmptTimeInfo> timeInfo) {
		TimeInfo = timeInfo;
	}

	public List<LbBmptOrderInfo> getOrderInfo() {
		return OrderInfo;
	}

	public void setOrderInfo(List<LbBmptOrderInfo> orderInfo) {
		OrderInfo = orderInfo;
	}

}
