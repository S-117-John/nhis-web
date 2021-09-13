package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

public class StQryInvDt {

	private String pkBill;  //发票项目主键
	private String codeBill;//发票项目编码
	private String nameBill;//发票项目名称
	private BigDecimal amount;    //发票项目金额
	public String getPkBill() {
		return pkBill;
	}
	public void setPkBill(String pkBill) {
		this.pkBill = pkBill;
	}
	public String getCodeBill() {
		return codeBill;
	}
	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}
	public String getNameBill() {
		return nameBill;
	}
	public void setNameBill(String nameBill) {
		this.nameBill = nameBill;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	

}
