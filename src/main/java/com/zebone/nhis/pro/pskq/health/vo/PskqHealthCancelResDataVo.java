package com.zebone.nhis.pro.pskq.health.vo;

import java.math.BigDecimal;

public class PskqHealthCancelResDataVo {
	/**
	 * 退费发票号
	 */
	private String apprtNo;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 交易流水号
	 */
	private String trandIdNo;
	
	/**
	 * 退费时间
	 */
	private String datePay;
	
	/**
	 * 医保退费金额
	 */
	private BigDecimal amountInsu;
	
	/**
	 * 自费退费金额
	 */
	private BigDecimal amountPi;

	public String getApprtNo() {
		return apprtNo;
	}

	public void setApprtNo(String apprtNo) {
		this.apprtNo = apprtNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTrandIdNo() {
		return trandIdNo;
	}

	public void setTrandIdNo(String trandIdNo) {
		this.trandIdNo = trandIdNo;
	}

	public String getDatePay() {
		return datePay;
	}

	public void setDatePay(String datePay) {
		this.datePay = datePay;
	}

	public BigDecimal getAmountInsu() {
		return amountInsu;
	}

	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}

	public BigDecimal getAmountPi() {
		return amountPi;
	}

	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	
	
}
