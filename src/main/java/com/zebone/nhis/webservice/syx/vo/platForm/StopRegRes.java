package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Item")
public class StopRegRes {

	/**
     * 停诊记录ID
     */
    @XmlElement(name = "stopRegId")
    private String stopRegId;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "deptId")
    private String deptId;
    
    /**
     * 医生代码
     */
    @XmlElement(name = "doctorId")
    private String doctorId;
    
    /**
     * 停诊日期（YYYY-MM-DD）
     */
    @XmlElement(name = "stopDate")
    private String stopDate;
    
    /**
     * 时段ID
     */
    @XmlElement(name = "timeID")
    private String timeID;
    
    /**
     * 听诊原因
     */
    @XmlElement(name = "reason")
    private String reason;

	public String getStopRegId() {
		return stopRegId;
	}

	public void setStopRegId(String stopRegId) {
		this.stopRegId = stopRegId;
	}

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

	public String getStopDate() {
		return stopDate;
	}

	public void setStopDate(String stopDate) {
		this.stopDate = stopDate;
	}

	public String getTimeID() {
		return timeID;
	}

	public void setTimeID(String timeID) {
		this.timeID = timeID;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
