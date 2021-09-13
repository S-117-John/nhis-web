package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

public class StQryInsuSt {
	 private String hp; // --医保
	 private String payer ; //--付款方
	 private BigDecimal amount ; // --金额
	public String getHp() {
		return hp;
	}
	public void setHp(String hp) {
		this.hp = hp;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	 

}
