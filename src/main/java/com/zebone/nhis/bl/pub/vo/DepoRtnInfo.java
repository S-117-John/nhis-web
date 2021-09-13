package com.zebone.nhis.bl.pub.vo;

public class DepoRtnInfo {

	public String getReptNo() {
		return reptNo;
	}
	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}
	public String getPkEmpinvoice() {
		return pkEmpinvoice;
	}
	public void setPkEmpinvoice(String pkEmpinvoice) {
		this.pkEmpinvoice = pkEmpinvoice;
	}
	private String reptNo;//退款使用的票据号
	private String pkEmpinvoice;//退款使用的领用主键     
}
