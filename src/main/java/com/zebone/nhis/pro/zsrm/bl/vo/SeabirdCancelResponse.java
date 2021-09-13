package com.zebone.nhis.pro.zsrm.bl.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="Response")
public class SeabirdCancelResponse {
	// 支付机构流水号
	@XmlElement(name = "resultCode")
	private String resultCode;
	@XmlElement(name = "resultDesc")
	private String resultDesc;
	@XmlElement(name = "refundStatus")
	private String refundStatus;
	@XmlElement(name = "refundStatusDes")
	private String refundStatusDes;
	@XmlElement(name = "psRefundOrdNum")
	private String psRefundOrdNum;
	@XmlElement(name = "agtRefundOrdNum")
	private String agtRefundOrdNum;
	@XmlElement(name = "refundAmout")
	private String refundAmout;
	@XmlElement(name = "refundTime")
	private String refundTime;
	
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getResultDesc() {
		return resultDesc;
	}
	public void setResultDesc(String resultDesc) {
		this.resultDesc = resultDesc;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getRefundStatusDes() {
		return refundStatusDes;
	}
	public void setRefundStatusDes(String refundStatusDes) {
		this.refundStatusDes = refundStatusDes;
	}
	public String getPsRefundOrdNum() {
		return psRefundOrdNum;
	}
	public void setPsRefundOrdNum(String psRefundOrdNum) {
		this.psRefundOrdNum = psRefundOrdNum;
	}
	public String getAgtRefundOrdNum() {
		return agtRefundOrdNum;
	}
	public void setAgtRefundOrdNum(String agtRefundOrdNum) {
		this.agtRefundOrdNum = agtRefundOrdNum;
	}
	public String getRefundAmout() {
		return refundAmout;
	}
	public void setRefundAmout(String refundAmout) {
		this.refundAmout = refundAmout;
	}
	public String getRefundTime() {
		return refundTime;
	}
	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}
	
	
}
