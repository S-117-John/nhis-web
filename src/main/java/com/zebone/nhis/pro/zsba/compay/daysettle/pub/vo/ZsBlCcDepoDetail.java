package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

import java.util.Date;

public class ZsBlCcDepoDetail {

	
	private String pkSettle;
	private String codePi;
	private String namePi;
	private Double amountSt; //结算金额
	private Double amountInv;//发票金额
	private Date dateSt;
	private String codeInv;
	private String flagCanc;
	private String pkSettleCanc;
	private String note;
	private String codeIp;
	private String codeSt;
	private String flag;//是否作废 1：作废 0：未作废
	private String euStresult;//结算类型
	
	public String getEuStresult() {
		return euStresult;
	}
	public void setEuStresult(String euStresult) {
		this.euStresult = euStresult;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getCodeSt() {
		return codeSt;
	}
	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
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
	public Double getAmountSt() {
		return amountSt;
	}
	public void setAmountSt(Double amountSt) {
		this.amountSt = amountSt;
	}
	public Double getAmountInv() {
		return amountInv;
	}
	public void setAmountInv(Double amountInv) {
		this.amountInv = amountInv;
	}
	public Date getDateSt() {
		return dateSt;
	}
	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}
	public String getCodeInv() {
		return codeInv;
	}
	public void setCodeInv(String codeInv) {
		this.codeInv = codeInv;
	}
	public String getFlagCanc() {
		return flagCanc;
	}
	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}
	public String getPkSettleCanc() {
		return pkSettleCanc;
	}
	public void setPkSettleCanc(String pkSettleCanc) {
		this.pkSettleCanc = pkSettleCanc;
	}


}
