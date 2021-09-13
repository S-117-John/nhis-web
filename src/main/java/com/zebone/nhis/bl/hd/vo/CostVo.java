package com.zebone.nhis.bl.hd.vo;

import java.math.BigDecimal;

public class CostVo {
	private String pkPv;
	private BigDecimal amount;
	private BigDecimal amountPi;
	private BigDecimal amountHppi;
	private Double amountAdd;
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public BigDecimal getAmountHppi() {
		return amountHppi;
	}
	public void setAmountHppi(BigDecimal amountHppi) {
		this.amountHppi = amountHppi;
	}
	public Double getAmountAdd() {
		return amountAdd;
	}
	public void setAmountAdd(Double amountAdd) {
		this.amountAdd = amountAdd;
	}
	

	
}
