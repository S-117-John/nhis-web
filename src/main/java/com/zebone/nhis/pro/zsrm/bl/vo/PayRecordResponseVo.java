package com.zebone.nhis.pro.zsrm.bl.vo;

/**
 * 第三方支付数据
 */
public class PayRecordResponseVo {
	/**
	 * 支付方式、业务类型名称(银联)
	 */
	private String settBusiName;

	// 交易日期
	private String trxDate;

	// 交易时间
	private String trxTime;

	// 入账金额
	private Double payAmount;

	// 商户订单号
	private String merOrderNo;

	// 支付交易流水号
	private String tradeNo;

	public String getSettBusiName() {
		return settBusiName;
	}

	public void setSettBusiName(String settBusiName) {
		this.settBusiName = settBusiName;
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

	public Double getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}

	public String getMerOrderNo() {
		return merOrderNo;
	}

	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
}
