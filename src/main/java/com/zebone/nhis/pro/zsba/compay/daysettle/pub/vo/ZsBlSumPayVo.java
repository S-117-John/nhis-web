package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.math.BigDecimal;

public class ZsBlSumPayVo {
	
	public BigDecimal getAmtPrep() {
		return amtPrep;
	}

	public void setAmtPrep(BigDecimal amtPrep) {
		this.amtPrep = amtPrep;
	}

	private BigDecimal amt;//收款金额
	
	private BigDecimal amtBack;//退款金额
	
	private  BigDecimal amtPrep;//预交金
	
	private BigDecimal amtRepair;//欠款补交
	
	private String dtPaymode;//支付方式
	
	private String euPaytype;//业务类型
	
	public BigDecimal getAmtRepair() {
		return amtRepair;
	}

	public void setAmtRepair(BigDecimal amtRepair) {
		this.amtRepair = amtRepair;
	}

	public BigDecimal getAmt() {
		return amt;
	}

	public void setAmt(BigDecimal amt) {
		this.amt = amt;
	}

	public BigDecimal getAmtBack() {
		return amtBack;
	}

	public void setAmtBack(BigDecimal amtBack) {
		this.amtBack = amtBack;
	}



	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}

	public String getEuPaytype() {
		return euPaytype;
	}

	public void setEuPaytype(String euPaytype) {
		this.euPaytype = euPaytype;
	}

	public String getPaymode() {
		return paymode;
	}

	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}

	private String paymode;//支付方式名称


}
