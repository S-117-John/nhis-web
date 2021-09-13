package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class DoctorPlanListRes {

	/**
     * 诊室代码
     */
    @XmlElement(name = "roomCode")
    private String roomCode;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "deptCode")
    private String deptCode;
    
    /**
     * 医生ID
     */
    @XmlElement(name = "doctorCode")
    private String doctorCode;
    
    /**
     * 出诊日期:yyyymmdd
     */
    @XmlElement(name = "clinicDate")
    private String clinicDate;
    
    /**
     * 出诊午别(2 上午，4 下午)
     */
    @XmlElement(name = "clinicTime")
    private String clinicTime;

	public String getDoctorCode() {
		return doctorCode;
	}

	public void setDoctorCode(String doctorCode) {
		this.doctorCode = doctorCode;
	}

	public String getClinicDate() {
		return clinicDate;
	}

	public void setClinicDate(String clinicDate) {
		this.clinicDate = clinicDate;
	}

	public String getClinicTime() {
		return clinicTime;
	}

	public void setClinicTime(String clinicTime) {
		this.clinicTime = clinicTime;
	}

	public String getRoomCode() {
		return roomCode;
	}

	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}


	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

    
}
