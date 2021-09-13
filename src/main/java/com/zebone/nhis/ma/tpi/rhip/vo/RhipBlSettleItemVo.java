package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlSettle;

/**
 * 结算记录billVO
 * @author chengjia
 *
 */
public class RhipBlSettleItemVo{
	private String billCode;
	private String billName;
	private String invCodes;
	private String quan;
	private String amount;
		
	
	public String getBillCode() {
		return billCode;
	}

	public void setBillCode(String billCode) {
		this.billCode = billCode;
	}

	public String getQuan() {
		return quan;
	}

	public void setQuan(String quan) {
		this.quan = quan;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBillName() {
		return billName;
	}

	public void setBillName(String billName) {
		this.billName = billName;
	}

	public String getInvCodes() {
		return invCodes;
	}

	public void setInvCodes(String invCodes) {
		this.invCodes = invCodes;
	}


	
}
