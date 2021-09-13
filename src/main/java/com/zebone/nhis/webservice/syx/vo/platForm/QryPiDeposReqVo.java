package com.zebone.nhis.webservice.syx.vo.platForm;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="req")
public class QryPiDeposReqVo {
	@XmlElement(name="inpatientId")
	private String inpatientId;
	
	@XmlElement(name="paySource")
	private String paySource;
	
	@XmlElement(name="beginDate")
	private String beginDate;
	
	@XmlElement(name="endDate")
	private String endDate;
	
	@XmlElement(name="orderId")
	private String orderId;
	
	@XmlElement(name="feekindId")
	private String feekindId;
	
	private String paySourceValue;
	
	private String note;
	
	public String getInpatientId() {
		return inpatientId;
	}
	public void setInpatientId(String inpatientId) {
		this.inpatientId = inpatientId;
	}
	public String getPaySource() {
		return paySource;
	}
	public void setPaySource(String paySource) {
		this.paySource = paySource;
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPaySourceValue() {
		return paySourceValue;
	}
	public void setPaySourceValue(String paySourceValue) {
		this.paySourceValue = paySourceValue;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getFeekindId() {
		return feekindId;
	}
	public void setFeekindId(String feekindId) {
		this.feekindId = feekindId;
	}
	
	
}
