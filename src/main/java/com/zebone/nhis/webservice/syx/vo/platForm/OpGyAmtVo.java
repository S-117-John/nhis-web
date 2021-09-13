package com.zebone.nhis.webservice.syx.vo.platForm;

import java.math.BigDecimal;

public class OpGyAmtVo {
	private BigDecimal amountSt = BigDecimal.ZERO;;// 结算金额
	private BigDecimal amountPi = BigDecimal.ZERO;;// 患者自付金额
	private BigDecimal amountInsu = BigDecimal.ZERO;// 医保支付金额
	private BigDecimal amountDrug =BigDecimal.ZERO;//医保支付药费
	private BigDecimal discAmount = BigDecimal.ZERO;;// 患者优惠金额
	private BigDecimal accountPrepaid = BigDecimal.ZERO;;// 账户已付
	private String pkDisc;
	private String nameDiag;
	public BigDecimal getAmountSt() {
		return amountSt;
	}
	public void setAmountSt(BigDecimal amountSt) {
		this.amountSt = amountSt;
	}
	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	public BigDecimal getAmountInsu() {
		return amountInsu;
	}
	public void setAmountInsu(BigDecimal amountInsu) {
		this.amountInsu = amountInsu;
	}
	public BigDecimal getAmountDrug() {
		return amountDrug;
	}
	public void setAmountDrug(BigDecimal amountDrug) {
		this.amountDrug = amountDrug;
	}
	public BigDecimal getDiscAmount() {
		return discAmount;
	}
	public void setDiscAmount(BigDecimal discAmount) {
		this.discAmount = discAmount;
	}
	public BigDecimal getAccountPrepaid() {
		return accountPrepaid;
	}
	public void setAccountPrepaid(BigDecimal accountPrepaid) {
		this.accountPrepaid = accountPrepaid;
	}
	public String getPkDisc() {
		return pkDisc;
	}
	public void setPkDisc(String pkDisc) {
		this.pkDisc = pkDisc;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	
	
}
