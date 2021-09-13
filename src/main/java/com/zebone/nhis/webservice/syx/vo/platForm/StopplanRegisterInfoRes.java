package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "regInfo")
public class StopplanRegisterInfoRes {

	/**
     * HIS系统的订单号
     */
    @XmlElement(name = "orderIDHIS")
    private String orderIDHIS;
    
    /**
     * 预约日期
     */
    @XmlElement(name = "registerDate")
    private String registerDate;
    
    /**
     * 时段名称
     */
    @XmlElement(name = "timeName")
    private String timeName;
    
    /**
     * 预约科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;
    
    /**
     * 预约医生姓名
     */
    @XmlElement(name = "doctorName")
    private String doctorName;
    
    /**
     * 患者姓名
     */
    @XmlElement(name = "patientName")
    private String patientName;
    
    /**
     * 流水号
     */
    @XmlElement(name = "seqNO")
    private String seqNO;
    
    /**
     * 预约来源
     */
    @XmlElement(name = "orderType")
    private String orderType;
    
    /**
     * 联系号码
     */
    @XmlElement(name = "phone")
    private String phone;
    
    /**
     * 移动电话
     */
    @XmlElement(name = "cellPhone")
    private String cellPhone;

	public String getOrderIDHIS() {
		return orderIDHIS;
	}

	public void setOrderIDHIS(String orderIDHIS) {
		this.orderIDHIS = orderIDHIS;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getSeqNO() {
		return seqNO;
	}

	public void setSeqNO(String seqNO) {
		this.seqNO = seqNO;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	
}
