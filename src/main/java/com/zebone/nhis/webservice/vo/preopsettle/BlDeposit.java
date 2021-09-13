package com.zebone.nhis.webservice.vo.preopsettle;

public class BlDeposit {
	String amount = ""; 
    String dtPaymode = "";  
    String note = "";
    String payInfo = "";
    String pkSettle = "";
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getDtPaymode() {
		return dtPaymode;
	}
	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
    
}
