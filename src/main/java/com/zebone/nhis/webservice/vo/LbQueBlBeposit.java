package com.zebone.nhis.webservice.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *住院预交金查询响应
 */
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class LbQueBlBeposit {
	//医院订单流水
	@XmlElement(name="tranSerNo")
	private String tranSerNo;
	//医院发票号
	@XmlElement(name="invoiceNo")
	private String invoiceNo;
	//充值金额
	@XmlElement(name="amount")
	private String amount;
	//可退金额
	@XmlElement(name="remainAmount")
	private String remainAmount;
	//支付方式
	@XmlElement(name="payType")
	private String payType;
	//充值后余额信息
	@XmlElement(name="balance")
	private String balance;
	//描述信息
	@XmlElement(name="description")
	private String description;
	//操作时间
	@XmlElement(name="operateTime")
	private String operateTime;
	//操作员
	@XmlElement(name="operator")
	private String operator;
	//备注
	@XmlElement(name="reserved")
	private String reserved;
	
	public String getTranSerNo() {
		return tranSerNo;
	}
	public void setTranSerNo(String tranSerNo) {
		this.tranSerNo = tranSerNo;
	}
	public String getInvoiceNo() {
		return invoiceNo;
	}
	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getRemainAmount() {
		return remainAmount;
	}
	public void setRemainAmount(String remainAmount) {
		this.remainAmount = remainAmount;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOperateTime() {
		return operateTime;
	}
	public void setOperateTime(String operateTime) {
		this.operateTime = operateTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	
}
