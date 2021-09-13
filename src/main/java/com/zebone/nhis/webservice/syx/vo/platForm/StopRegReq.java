package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StopRegReq {

	/**
     * 排班开始日期（YYYY-MM-DD）
     */
    @XmlElement(name = "beginDate")
    private String beginDate;
    
    /**
     * 排班结束日期（YYYY-MM-DD）
     */
    @XmlElement(name = "endDate")
    private String endDate;
    
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
	public String getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
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
    
}
