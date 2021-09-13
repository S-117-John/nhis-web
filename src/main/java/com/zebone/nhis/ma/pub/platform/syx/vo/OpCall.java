package com.zebone.nhis.ma.pub.platform.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class OpCall {

	/**
     * 队列号
     */
    @XmlElement(name = "queueNum")
    private String queueNum;
    
    /**
     * 病人ID
     */
    @XmlElement(name = "patientId")
    private String patientId;
    
    /**
     * 病人姓名
     */
    @XmlElement(name = "patientName")
    private String patientName;
    
    /**
     * 叫号时间： yyyy-MM-dd hh:mm:dd
     */
    @XmlElement(name = "callingTime")
    private String callingTime;
    
    /**
     * 叫号状态（0：等候，1：就诊中，2：过号，3：完成，4：取消）
     */
    @XmlElement(name = "callingFlag")
    private String callingFlag;
    
    /**
     * 出诊午别(2 上午，4 下午)
     */
    @XmlElement(name = "clinicTime")
    private String clinicTime;
    
    /**
     * 医生代码
     */
    @XmlElement(name = "doctorCode")
    private String doctorCode;
    
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
    
    /**
     * 诊室代码
     */
    @XmlElement(name = "roomCode")
    private String roomCode;
    
    /**
     * 诊室名称
     */
    @XmlElement(name = "roomName")
    private String roomName;
    
    /**
     * 诊室号
     */
    @XmlElement(name = "roomNo")
    private String roomNo;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "deptCode")
    private String deptCode;
    
    /**
     * 科室名称
     */
    @XmlElement(name = "deptName")
    private String deptName;

	public String getQueueNum() {
		return queueNum;
	}

	public void setQueueNum(String queueNum) {
		this.queueNum = queueNum;
	}

	public String getPatientId() {
		return patientId;
	}

	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getCallingTime() {
		return callingTime;
	}

	public void setCallingTime(String callingTime) {
		this.callingTime = callingTime;
	}

	public String getCallingFlag() {
		return callingFlag;
	}

	public void setCallingFlag(String callingFlag) {
		this.callingFlag = callingFlag;
	}

	public String getClinicTime() {
		return clinicTime;
	}

	public void setClinicTime(String clinicTime) {
		this.clinicTime = clinicTime;
	}

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
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

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getRoomNo() {
		return roomNo;
	}

	public void setRoomNo(String roomNo) {
		this.roomNo = roomNo;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
