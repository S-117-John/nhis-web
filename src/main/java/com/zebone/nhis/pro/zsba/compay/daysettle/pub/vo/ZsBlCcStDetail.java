package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.Date;

public class ZsBlCcStDetail {
	
	private String pkDepo;
    private String codePi;
    private String namePi;
    private Double amount;
    private String paymode;
    private String reptNo;
    private Date datePay;
    private String euDirect;
    private String pkDepoBack;
    private String note;
    private String codeIp;
    private String euDptype;
    
	public String getEuDptype() {
		return euDptype;
	}
	public void setEuDptype(String euDptype) {
		this.euDptype = euDptype;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkDepo() {
		return pkDepo;
	}
	public void setPkDepo(String pkDepo) {
		this.pkDepo = pkDepo;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPaymode() {
		return paymode;
	}
	public void setPaymode(String paymode) {
		this.paymode = paymode;
	}
	public String getReptNo() {
		return reptNo;
	}
	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}
	public Date getDatePay() {
		return datePay;
	}
	public void setDatePay(Date datePay) {
		this.datePay = datePay;
	}
	public String getEuDirect() {
		return euDirect;
	}
	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}
	public String getPkDepoBack() {
		return pkDepoBack;
	}
	public void setPkDepoBack(String pkDepoBack) {
		this.pkDepoBack = pkDepoBack;
	}

}
