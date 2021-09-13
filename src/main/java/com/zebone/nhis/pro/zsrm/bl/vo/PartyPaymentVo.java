package com.zebone.nhis.pro.zsrm.bl.vo;

import com.alibaba.fastjson.annotation.JSONField;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * 第三方支付数据
 */
public class PartyPaymentVo {

	// 清算日期
	@JsonProperty("settDate")
	private String settDate;

	// 商户编号
	private String settMerId;

	// 商户名称
	private String settMerName;

	// 支付方式、业务类型名称(银联)
	private String settBusiName;

	// 交易类型名称
	private String trxName;

	// 交易日期
	private String trxDate;

	// 交易时间
	private String trxTime;

	// 终端编号
	private String settTermNo;

	// 自费交易金额、交易金额(银联)
	private Double amount;

	// 清算金额
	private Double trxAmount;

	// 手续费
	private Double feeAmount;

	// 入账金额
	private Double payAmount;

	// 终端流水号
	private String traceNo;

	// 检索参考号
	private String refNo;

	// 商户订单号
	private String merOrderNo;

	// 患者姓名
	private String userName;

	// 支付交易流水号
	private String tradeNo;

	// 交易状态
	private String tradeState;
	// 退款单号
	private String refundNo;
	// 卡号
	private String cardNo;

	// 卡类型
	// 01：贷记卡 02：准贷记卡 03：借记卡；04：预付卡；09：存折；
	// YB：医保 Y1：Y1：实体社保卡；Y2：电子社保卡；Y3：医保电子凭证
	private String cardType;
	// 发卡行代码 扫码QR
	// Q0123101=支付宝钱包 Q0134403=微信钱包 ZUNP0001=银联二维码
	private String issBankCode;
	// 发卡行名称
	private String issBankName;

	// 海鹚  支付类型 1-收款 2-退款
	private String orderType;
	// 海鹚  交易渠道 WXPay：微信支付 ZFBPay：支付宝
	private String payMode;
	// 海鹚  患者姓名
	private String patName;

	//订单状态
	private String orderStatus;

	public String getSettDate() {
		return settDate;
	}

	public void setSettDate(String settDate) {
		this.settDate = settDate;
	}

	public String getSettMerId() {
		return settMerId;
	}

	public void setSettMerId(String settMerId) {
		this.settMerId = settMerId;
	}

	public String getSettMerName() {
		return settMerName;
	}

	public void setSettMerName(String settMerName) {
		this.settMerName = settMerName;
	}

	public String getSettBusiName() {
		return settBusiName;
	}

	public void setSettBusiName(String settBusiName) {
		this.settBusiName = settBusiName;
	}

	public String getTrxName() {
		return trxName;
	}

	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}

	public String getTrxDate() {
		return trxDate;
	}

	public void setTrxDate(String trxDate) {
		this.trxDate = trxDate;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getSettTermNo() {
		return settTermNo;
	}

	public void setSettTermNo(String settTermNo) {
		this.settTermNo = settTermNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getTrxAmount() {
		return trxAmount;
	}

	public void setTrxAmount(Double trxAmount) {
		this.trxAmount = trxAmount;
	}

	public Double getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(Double feeAmount) {
		this.feeAmount = feeAmount;
	}

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getRefNo() {
		return refNo;
	}

	public void setRefNo(String refNo) {
		this.refNo = refNo;
	}

	public String getMerOrderNo() {
		return merOrderNo;
	}

	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeState() {
		return tradeState;
	}

	public void setTradeState(String tradeState) {
		this.tradeState = tradeState;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getIssBankCode() {
		return issBankCode;
	}

	public void setIssBankCode(String issBankCode) {
		this.issBankCode = issBankCode;
	}

	public String getIssBankName() {
		return issBankName;
	}

	public void setIssBankName(String issBankName) {
		this.issBankName = issBankName;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getPatName() {
		return patName;
	}

	public void setPatName(String patName) {
		this.patName = patName;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

}
