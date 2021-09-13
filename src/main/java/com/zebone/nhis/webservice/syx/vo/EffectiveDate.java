package com.zebone.nhis.webservice.syx.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "EFFECTIVE_DATE")
@XmlAccessorType(XmlAccessType.FIELD)
public class EffectiveDate {


	@XmlElement(name = "EFFECTIVE_START_DATE")
	private String effectiveStartDate;
	@XmlElement(name = "EFFECTIVE_END_DATE")
	private String effectiveEndDate;
	public String getEffectiveStartDate() {
		return effectiveStartDate;
	}
	public void setEffectiveStartDate(String effectiveStartDate) {
		this.effectiveStartDate = effectiveStartDate;
	}
	public String getEffectiveEndDate() {
		return effectiveEndDate;
	}
	public void setEffectiveEndDate(String effectiveEndDate) {
		this.effectiveEndDate = effectiveEndDate;
	}
	
	
}
