package com.zebone.nhis.pro.zsrm.bl.vo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "list")

public class SeabirdOrder {

	// 支付机构流水号
	@XmlElement(name = "agtOrdNum")
	private String agtOrdNum;

	@XmlElement(name = "hisOrdNum")
	private String hisOrdNum;
	@XmlElement(name = "hisJrnNum")
	private String hisJrnNum;
	// 平台订单号,暂不使用
	@XmlElement(name = "psOrdNum")
	private String psOrdNum;
	// 交易类型 1-支付 2-退款
	@XmlElement(name = "orderType")
	private String orderType;
	@XmlElement(name = "payChannel")
	private String payChannel;
	// 交易渠道 WXPay：微信支付 ZFBPay：支付宝
	@XmlElement(name = "payMode")
	private String payMode;
	@XmlElement(name = "payType")
	private String payType;

	// 交易金额
	@XmlElement(name = "payAmt")
	private Double payAmt;
	// 交易时间
	@XmlElement(name = "payTime")
	private String payTime;
	@XmlElement(name = "oldAgtOrdNum")
	private String oldAgtOrdNum;
	@XmlElement(name = "oldPsOrdNum")
	private String oldPsOrdNum;
	@XmlElement(name = "oldHisOrdNum")
	private String oldHisOrdNum;
	@XmlElement(name = "oldHisJrnNum")
	private String oldHisJrnNum;
	@XmlElement(name = "feeType")
	private String feeType;
	@XmlElement(name = "subFeeType")
	private String subFeeType;
	@XmlElement(name = "cardNo")
	private String cardNo;

	// 患者姓名
	@XmlElement(name = "patName")
	private String patName;
	@XmlElement(name = "opnId")
	private String opnId;

	public String getAgtOrdNum() {
		return agtOrdNum;
	}

	public void setAgtOrdNum(String agtOrdNum) {
		this.agtOrdNum = agtOrdNum;
	}

	public String getHisOrdNum() {
		return hisOrdNum;
	}

	public void setHisOrdNum(String hisOrdNum) {
		this.hisOrdNum = hisOrdNum;
	}

	public String getHisJrnNum() {
		return hisJrnNum;
	}

	public void setHisJrnNum(String hisJrnNum) {
		this.hisJrnNum = hisJrnNum;
	}

	public String getPsOrdNum() {
		return psOrdNum;
	}

	public void setPsOrdNum(String psOrdNum) {
		this.psOrdNum = psOrdNum;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayChannel() {
		return payChannel;
	}

	public void setPayChannel(String payChannel) {
		this.payChannel = payChannel;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public Double getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(Double payAmt) {
		this.payAmt = payAmt;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOldAgtOrdNum() {
		return oldAgtOrdNum;
	}

	public void setOldAgtOrdNum(String oldAgtOrdNum) {
		this.oldAgtOrdNum = oldAgtOrdNum;
	}

	public String getOldPsOrdNum() {
		return oldPsOrdNum;
	}

	public void setOldPsOrdNum(String oldPsOrdNum) {
		this.oldPsOrdNum = oldPsOrdNum;
	}

	public String getOldHisOrdNum() {
		return oldHisOrdNum;
	}

	public void setOldHisOrdNum(String oldHisOrdNum) {
		this.oldHisOrdNum = oldHisOrdNum;
	}

	public String getOldHisJrnNum() {
		return oldHisJrnNum;
	}

	public void setOldHisJrnNum(String oldHisJrnNum) {
		this.oldHisJrnNum = oldHisJrnNum;
	}

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public String getSubFeeType() {
		return subFeeType;
	}

	public void setSubFeeType(String subFeeType) {
		this.subFeeType = subFeeType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getOpnId() {
		return opnId;
	}

	public void setOpnId(String opnId) {
		this.opnId = opnId;
	}

}
