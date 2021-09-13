package com.zebone.nhis.common.module.pv;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退号时如果是第三方支付类型 传递入参
 * @author Administrator
 */
public class BlExtPayBankVo {

	/** 老的第三方交易号码 **/
	private String oldBankCode;
	
	/** 新的第三方交易号码 **/
	private String newBankCode;

	/** 交易时间 **/
	private Date bankTime;
	
	/** 交易金额 **/
	private BigDecimal amount;
	
	/** pos机编码**/
	private String outTradeNo;
	
	/** 结算标志**/
	private String payResult;
	
	
	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getPayResult() {
		return payResult;
	}

	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getOldBankCode() {
		return oldBankCode;
	}

	public void setOldBankCode(String oldBankCode) {
		this.oldBankCode = oldBankCode;
	}

	public String getNewBankCode() {
		return newBankCode;
	}

	public void setNewBankCode(String newBankCode) {
		this.newBankCode = newBankCode;
	}

	public Date getBankTime() {
		return bankTime;
	}

	public void setBankTime(Date bankTime) {
		this.bankTime = bankTime;
	}
}
