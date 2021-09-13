package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

public class BlDepositVo {
	public Date datePay ; /// --日期
    public String reptNo   ;///--收据号
    public String dtPaymode ;///--支付方式
    public String amount   ;///--金额
    public String dtBank  ;///--银行
    public String bankNo ; ///--凭证号
    public String nameEmpPay;
    public String note ;
	public Date getDatePay() {
		return datePay;
	}
	public void setDatePay(Date datePay) {
		this.datePay = datePay;
	}
	public String getReptNo() {
		return reptNo;
	}
	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}
	public String getDtPaymode() {
		return dtPaymode;
	}
	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDtBank() {
		return dtBank;
	}
	public void setDtBank(String dtBank) {
		this.dtBank = dtBank;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getNameEmpPay() {
		return nameEmpPay;
	}
	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
    
	
    
}
