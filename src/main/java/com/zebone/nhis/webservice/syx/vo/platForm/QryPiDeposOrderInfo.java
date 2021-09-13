package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="orderDetailInfo")
public class QryPiDeposOrderInfo {
	@XmlElement(name="orderIdHis")
	private String orderIdHis;
	
	@XmlElement(name="payTime")
	private String payTime;
	
	@XmlElement(name="payMode")
	private String payMode;
	
	@XmlElement(name="payAmout")
	private Double payAmout;
	
	@XmlElement(name="statusFlag")
	private String statusFlag;
	
	@XmlElement(name="foregiftReceiptNo")
	private String foregiftReceiptNo;
	
	@XmlElement(name="orderDesc")
	private String orderDesc;
	
	@XmlElement(name="feekindId")
	private String feekindId;
	
	@XmlElement(name="feekindName")
	private String feekindName;
	
	@XmlElement(name="amount")
	private Double amount;
	
	@XmlElement(name="ItemInfo")
	private List<QryPiItemInfo> itemInfos;
	
 	public String getOrderIdHis() {
		return orderIdHis;
	}
	public void setOrderIdHis(String orderIdHis) {
		this.orderIdHis = orderIdHis;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public Double getPayAmout() {
		return payAmout;
	}
	public void setPayAmout(Double payAmout) {
		this.payAmout = payAmout;
	}
	public String getStatusFlag() {
		return statusFlag;
	}
	public void setStatusFlag(String statusFlag) {
		this.statusFlag = statusFlag;
	}
	public String getForegiftReceiptNo() {
		return foregiftReceiptNo;
	}
	public void setForegiftReceiptNo(String foregiftReceiptNo) {
		this.foregiftReceiptNo = foregiftReceiptNo;
	}
	public String getOrderDesc() {
		return orderDesc;
	}
	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}
	public String getFeekindId() {
		return feekindId;
	}
	public void setFeekindId(String feekindId) {
		this.feekindId = feekindId;
	}
	public String getFeekindName() {
		return feekindName;
	}
	public void setFeekindName(String feekindName) {
		this.feekindName = feekindName;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public List<QryPiItemInfo> getItemInfos() {
		return itemInfos;
	}
	public void setItemInfos(List<QryPiItemInfo> itemInfos) {
		this.itemInfos = itemInfos;
	}
}
