package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

public class StQryDepoInfo {
	private String pkDepo;// 交款记录主键
	private BigDecimal amount ; // --金额
	private String paymode; // --付款方式名称
	private String bank ; //--银行
	private String bankNo; //   --凭证号
	private String dtPaymode;// 付款方式编码
	private String pkEmpPay;//收款人
	private String nameEmpPay;//收款人名称
	
	public String getPkDepo() {
		return pkDepo;
	}
	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getPkEmpPay() {
		return pkEmpPay;
	}
	public String getNameEmpPay() {
		return nameEmpPay;
	}
	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}
	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}
	
}
