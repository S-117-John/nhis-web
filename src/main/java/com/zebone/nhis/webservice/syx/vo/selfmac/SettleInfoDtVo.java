package com.zebone.nhis.webservice.syx.vo.selfmac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettleInfoDtVo {
	
	@XmlElement(name="patientName")
	private String patientName;
	
	@XmlElement(name="payAmout")
	private String payAmout;
	
	@XmlElement(name="detailFee")
	private String detailFee;
	
	
	@XmlElement(name="detailId")
	private String detailId;
	
	@XmlElement(name="detailCode")
	private String detailCode;
	
	@XmlElement(name="detailName")
	private String detailName;
	
	@XmlElement(name="detailSpec")
	private String detailSpec;
	
	@XmlElement(name="detailCount")
	private String detailCount;
	
	@XmlElement(name="detailUnit")
	private String detailUnit;
	
	@XmlElement(name="detailAmout")
	private String detailAmout;
	
	@XmlElement(name="nameLevel")
	private String nameLevel;

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

	public String getDetailFee() {
		return detailFee;
	}

	public void setDetailFee(String detailFee) {
		this.detailFee = detailFee;
	}

	public String getDetailId() {
		return detailId;
	}

	public void setDetailId(String detailId) {
		this.detailId = detailId;
	}

	public String getDetailCode() {
		return detailCode;
	}

	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}

	public String getDetailName() {
		return detailName;
	}

	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	public String getDetailSpec() {
		return detailSpec;
	}

	public void setDetailSpec(String detailSpec) {
		this.detailSpec = detailSpec;
	}

	public String getDetailCount() {
		return detailCount;
	}

	public void setDetailCount(String detailCount) {
		this.detailCount = detailCount;
	}

	public String getDetailUnit() {
		return detailUnit;
	}

	public void setDetailUnit(String detailUnit) {
		this.detailUnit = detailUnit;
	}

	public String getDetailAmout() {
		return detailAmout;
	}

	public void setDetailAmout(String detailAmout) {
		this.detailAmout = detailAmout;
	}

	public String getNameLevel() {
		return nameLevel;
	}

	public void setNameLevel(String nameLevel) {
		this.nameLevel = nameLevel;
	}  
}
