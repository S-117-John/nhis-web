package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="req")
@XmlAccessorType( XmlAccessType.FIELD)
public class PayDepoReq {
	
	 @XmlElement(name="inpatientId")
	 private String inpatientId;
	 
	 @XmlElement(name="orderId")
	 private String orderId;
	 
	 @XmlElement(name="payNum")
	 private String payNum;
	 
	 @XmlElement(name="agtOrdNum")
	 private String agtOrdNum;
	 
	 @XmlElement(name="agtCode")
	 private String agtCode;
	 
	 @XmlElement(name="payAmout")
	 private Double payAmout;
	 
	 @XmlElement(name="payMode")
	 private String payMode;
	 
	 @XmlElement(name="paySource")
	 private String paySource;
	 
	 @XmlElement(name="payTime")
	 private String payTime;
	 
	public String getInpatientId() {
		return inpatientId;
	}
	public void setInpatientId(String inpatientId) {
		this.inpatientId = inpatientId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getPayNum() {
		return payNum;
	}
	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}
	public String getAgtOrdNum() {
		return agtOrdNum;
	}
	public void setAgtOrdNum(String agtOrdNum) {
		this.agtOrdNum = agtOrdNum;
	}
	public String getAgtCode() {
		return agtCode;
	}
	public void setAgtCode(String agtCode) {
		this.agtCode = agtCode;
	}
	public Double getPayAmout() {
		return payAmout;
	}
	public void setPayAmout(Double payAmout) {
		this.payAmout = payAmout;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getPaySource() {
		return paySource;
	}
	public void setPaySource(String paySource) {
		this.paySource = paySource;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	
}
