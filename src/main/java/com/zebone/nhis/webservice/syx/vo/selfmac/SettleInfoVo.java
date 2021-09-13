package com.zebone.nhis.webservice.syx.vo.selfmac;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SettleInfoVo {

	@XmlElement(name="ipSeqnoText")
	private String ipSeqnoText;
	
	@XmlElement(name="ipTimes")
	private String ipTimes;
	
	@XmlElement(name="patientName")
	private String patientName;
	
	@XmlElement(name="patientSex")
	private String patientSex;
	
	@XmlElement(name="patientHp")
	private String patientHp;
	
	@XmlElement(name="pkHp")
	private String pkHp;
	
	@XmlElement(name="deptName")
	private String deptName;
	
	@XmlElement(name="bedNo")
	private String bedNo;
	
	@XmlElement(name="stCode")
	private String stCode;
	
	@XmlElement(name="payAmout")
	private String payAmout;
	
	@XmlElement(name="beginDate")
	private String beginDate;
	
	@XmlElement(name="endDate")
	private String endDate;
	
	@XmlElement(name="cntPrintList")
	private String cntPrintList;

	public String getIpSeqnoText() {
		return ipSeqnoText;
	}

	public void setIpSeqnoText(String ipSeqnoText) {
		this.ipSeqnoText = ipSeqnoText;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientSex() {
		return patientSex;
	}

	public void setPatientSex(String patientSex) {
		this.patientSex = patientSex;
	}

	public String getPatientHp() {
		return patientHp;
	}

	public void setPatientHp(String patientHp) {
		this.patientHp = patientHp;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getStCode() {
		return stCode;
	}

	public void setStCode(String stCode) {
		this.stCode = stCode;
	}

	public String getPayAmout() {
		return payAmout;
	}

	public void setPayAmout(String payAmout) {
		this.payAmout = payAmout;
	}

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

	public String getCntPrintList() {
		return cntPrintList;
	}

	public void setCntPrintList(String cntPrintList) {
		this.cntPrintList = cntPrintList;
	}

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}
}
