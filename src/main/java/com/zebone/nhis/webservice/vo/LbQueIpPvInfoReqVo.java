package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="request")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueIpPvInfoReqVo {
	@XmlElement(name="patientId")
	private String patientId;
	@XmlElement(name="startDate")
	private String startDate;
	@XmlElement(name="endDate")
	private String endDate;
	@XmlElement(name="deviceId")
	private String deviceId;
	@XmlElement(name="settleFlag")
	private String settleFlag;
	@XmlElement(name="cardType")
	private String cardType;
	@XmlElement(name="cardNo")
	private String cardNo;
	@XmlElement(name="dtCardType")
	private String dtCardType;
	@XmlElement(name="dtcardNo")
	private String dtcardNo;
	public String getPatientId() {
		return patientId;
	}
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}
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
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getSettleFlag() {
		return settleFlag;
	}
	public void setSettleFlag(String settleFlag) {
		this.settleFlag = settleFlag;
	}
	public String getCardType() {
		return cardType;
	}
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getDtCardType() {
		return dtCardType;
	}
	public void setDtCardType(String dtCardType) {
		this.dtCardType = dtCardType;
	}
	public String getDtcardNo() {
		return dtcardNo;
	}
	public void setDtcardNo(String dtcardNo) {
		this.dtcardNo = dtcardNo;
	}
}
