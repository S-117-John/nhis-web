package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

public class ZsbaBackDepoInfo {

	private String sfName;//身份
	private String codeIp;//住院号
	private String namePi;//患者姓名
	private String reptNo;//发票号
	private String amount;//金额
	private String note;//备注 默认为‘退预交金’
	public String getSfName() {
		return sfName;
	}
	public void setSfName(String sfName) {
		this.sfName = sfName;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getReptNo() {
		return reptNo;
	}
	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	
}
