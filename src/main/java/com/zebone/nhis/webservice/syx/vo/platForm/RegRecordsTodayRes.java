package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "regListInfo")
public class RegRecordsTodayRes {

	/**
     * HIS系统的订单号
     */
    @XmlElement(name = "orderIdHis")
    private String orderIdHis;
    
    /**
     * 患者姓名
     */
    @XmlElement(name = "patientName")
    private String patientName;
    
    /**
     * 诊疗卡号
     */
    @XmlElement(name = "patientCradNo")
    private String patientCradNo;
    
    /**
     * 性别描述
     */
    @XmlElement(name = "sex")
    private String sex;
    
    /**
     * 年龄
     */
    @XmlElement(name = "age")
    private String age;
    
    /**
     * 挂号来源
     */
    @XmlElement(name = "orderType")
    private String orderType;
    
    /**
     * 挂号日期
     */
    @XmlElement(name = "registerDate")
    private String registerDate;
    
    /**
     * 挂号发生时间
     */
    @XmlElement(name = "registerDateTime")
    private String registerDateTime;
    
    /**
     * 流水号
     */
    @XmlElement(name = "seqno")
    private String seqno;
    
    /**
     * 医生顺序号
     */
    @XmlElement(name = "doctorSeqno")
    private String doctorSeqno;
    
    /**
     * 挂号时段名称
     */
    @XmlElement(name = "timeName")
    private String timeName;
    
    /**
     * 门诊专科
     */
    @XmlElement(name = "deptName")
    private String deptName;
    
    /**
     * 门诊医生
     */
    @XmlElement(name = "doctorName")
    private String doctorName;
    
    /**
     * 挂号类型描述
     */
    @XmlElement(name = "registerType")
    private String registerType;
    
    /**
     * 挂号金额
     */
    @XmlElement(name = "treatFee")
    private String treatFee;
    
    /**
     * HIS系统生成的候诊时间
     */
    @XmlElement(name = "waitTime")
    private String waitTime;
    
    /**
     * HIS系统生成的就诊诊室名称
     */
    @XmlElement(name = "diagnoseRoomName")
    private String diagnoseRoomName;

	public String getOrderIdHis() {
		return orderIdHis;
	}

	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientCradNo() {
		return patientCradNo;
	}

	public void setPatientCradNo(String patientCradNo) {
		this.patientCradNo = patientCradNo;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getRegisterDateTime() {
		return registerDateTime;
	}

	public void setRegisterDateTime(String registerDateTime) {
		this.registerDateTime = registerDateTime;
	}

	public String getSeqno() {
		return seqno;
	}

	public void setSeqno(String seqno) {
		this.seqno = seqno;
	}

	public String getDoctorSeqno() {
		return doctorSeqno;
	}

	public void setDoctorSeqno(String doctorSeqno) {
		this.doctorSeqno = doctorSeqno;
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

	public String getRegisterType() {
		return registerType;
	}

	public void setRegisterType(String registerType) {
		this.registerType = registerType;
	}

	public String getTreatFee() {
		return treatFee;
	}

	public void setTreatFee(String treatFee) {
		this.treatFee = treatFee;
	}

	public String getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(String waitTime) {
		this.waitTime = waitTime;
	}

	public String getDiagnoseRoomName() {
		return diagnoseRoomName;
	}

	public void setDiagnoseRoomName(String diagnoseRoomName) {
		this.diagnoseRoomName = diagnoseRoomName;
	}
    
    
}
