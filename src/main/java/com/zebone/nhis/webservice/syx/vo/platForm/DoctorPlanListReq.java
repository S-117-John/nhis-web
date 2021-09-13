package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DoctorPlanListReq {

	/**
     * 当前日期：yyyymmdd
     */
    @XmlElement(name = "dateWork")
    private String dateWork;
    
    /*
     * 出诊午别：0101上午，0102下午
     */
    @XmlElement(name = "clinicTime")
    private String clinicTime;

	public String getDateWork() {
		return dateWork;
	}

	public void setDateWork(String dateWork) {
		this.dateWork = dateWork;
	}

	public String getClinicTime() {
		return clinicTime;
	}

	public void setClinicTime(String clinicTime) {
		this.clinicTime = clinicTime;
	}

	
    
    
}
