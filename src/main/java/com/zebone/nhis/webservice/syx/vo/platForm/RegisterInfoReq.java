package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RegisterInfoReq {

	/**
     * 开始日期（YYYY-MM-DD）
     */
    @XmlElement(name = "startDate")
    private String startDate;
    
    /**
     * 结束日期（YYYY-MM-DD）
     */
    @XmlElement(name = "endDate")
    private String endDate;
    
    /**
     * 科室代码
     */
    @XmlElement(name = "seconddepId")
    private String seconddepId;
    /**
     * 医生代码
     */
    @XmlElement(name = "type")
    private String type;
    
	
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getSeconddepId() {
		return seconddepId;
	}
	public void setSeconddepId(String seconddepId) {
		this.seconddepId = seconddepId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
