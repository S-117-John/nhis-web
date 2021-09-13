package com.zebone.nhis.pro.zsrm.bl.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="request")
public class SeabirdCancelRequest {
	
	@XmlElement(name = "serviceCode")
	private String serviceCode;
	@XmlElement(name = "resultCode")
	private String resultCode;
	@XmlElement(name = "partnerId")
	private String partnerId;
	@XmlElement(name = "timeStamp")
	private String timeStamp;
	@XmlElement(name = "password")
	private String password;
	@XmlElement(name = "hisRefundOrdNum")
	private String hisRefundOrdNum;
	@XmlElement(name = "agtOrdNum")
	private String agtOrdNum;
	@XmlElement(name = "refundAmout")
	private int refundAmout;
	@XmlElement(name = "refundDesc")
	private String refundDesc;
	@XmlElement(name = "checkOutNo")
	private String checkOutNo;
	
	
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getHisRefundOrdNum() {
		return hisRefundOrdNum;
	}
	public void setHisRefundOrdNum(String hisRefundOrdNum) {
		this.hisRefundOrdNum = hisRefundOrdNum;
	}
	public String getAgtOrdNum() {
		return agtOrdNum;
	}
	public void setAgtOrdNum(String agtOrdNum) {
		this.agtOrdNum = agtOrdNum;
	}

	public int getRefundAmout() {
		return refundAmout;
	}
	public void setRefundAmout(int refundAmout) {
		this.refundAmout = refundAmout;
	}
	public String getRefundDesc() {
		return refundDesc;
	}
	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}
	public String getCheckOutNo() {
		return checkOutNo;
	}
	public void setCheckOutNo(String checkOutNo) {
		this.checkOutNo = checkOutNo;
	}
	
	
}
