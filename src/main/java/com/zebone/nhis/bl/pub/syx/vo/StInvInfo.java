package com.zebone.nhis.bl.pub.syx.vo;

import java.math.BigDecimal;

public class StInvInfo {
	private String pkEmpinvoice;//票据领用主键
	private String pkInvcate;//票据分类主键
	private  String endcode;//终止号码
	private  String begincode;//起始号码
	private  BigDecimal cnt;//发票张数
	
	public String getPkEmpinvoice() {
		return pkEmpinvoice;
	}
	public void setPkEmpinvoice(String pkEmpinvoice) {
		this.pkEmpinvoice = pkEmpinvoice;
	}
	public String getPkInvcate() {
		return pkInvcate;
	}
	public void setPkInvcate(String pkInvcate) {
		this.pkInvcate = pkInvcate;
	}
	public String getEndcode() {
		return endcode;
	}
	public void setEndcode(String endcode) {
		this.endcode = endcode;
	}
	public String getBegincode() {
		return begincode;
	}
	public void setBegincode(String begincode) {
		this.begincode = begincode;
	}
	public BigDecimal getCnt() {
		return cnt;
	}
	public void setCnt(BigDecimal cnt) {
		this.cnt = cnt;
	}
}
