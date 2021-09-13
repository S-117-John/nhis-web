package com.zebone.nhis.pro.zsba.compay.daysettle.pub.vo;

public class ZsDepositInv {
	public String getBegincoe() {
		return begincoe;
	}
	public void setBegincoe(String begincoe) {
		this.begincoe = begincoe;
	}
	public String getEndcoe() {
		return endcoe;
	}
	public void setEndcoe(String endcoe) {
		this.endcoe = endcoe;
	}
	public String getPkEmpinvoicePrep() {
		return pkEmpinvoicePrep;
	}
	public void setPkEmpinvoicePrep(String pkEmpinvoicePrep) {
		this.pkEmpinvoicePrep = pkEmpinvoicePrep;
	}
	private String begincoe;//收款使用的开始收据号
	private String endcoe;//收款使用的结束收据号
	private String pkEmpinvoicePrep;//收款使用的领用主键
}
