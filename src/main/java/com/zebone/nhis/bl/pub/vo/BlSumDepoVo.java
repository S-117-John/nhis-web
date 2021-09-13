package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

public class BlSumDepoVo {
	
	private String pkEmpOpera;
	
	private String nameEmpOpera;
	
	public String getNameEmpOpera() {
		return nameEmpOpera;
	}

	public void setNameEmpOpera(String nameEmpOpera) {
		this.nameEmpOpera = nameEmpOpera;
	}



	private BigDecimal amtSettle;//结算金额
	
	public String getPkEmpOpera() {
		return pkEmpOpera;
	}

	public void setPkEmpOpera(String pkEmpOpera) {
		this.pkEmpOpera = pkEmpOpera;
	}

	

	private BigDecimal amtPrep;//收预交金

	private BigDecimal amtPrepRt;//退预交金
	
	private BigDecimal amtInsu;//医保金额
	
	private BigDecimal amtAr;//欠款金额
	
	private BigDecimal amtRepair;//欠费补交
	
	private BigDecimal amtSor;
	
	private BigDecimal amtCa;
	
	private BigDecimal amtPay;//现金支付金额
	

	public BigDecimal getAmtPay() {
		return amtPay;
	}

	public void setAmtPay(BigDecimal amtPay) {
		this.amtPay = amtPay;
	}

	public BigDecimal getAmtSor() {
		return amtSor;
	}

	public void setAmtSor(BigDecimal amtSor) {
		this.amtSor = amtSor;
	}

	public BigDecimal getAmtCa() {
		return amtCa;
	}

	public void setAmtCa(BigDecimal amtCa) {
		this.amtCa = amtCa;
	}

	public BigDecimal getAmtAr() {
		return amtAr;
	}

	public void setAmtAr(BigDecimal amtAr) {
		this.amtAr = amtAr;
	}

	public BigDecimal getAmtRepair() {
		return amtRepair;
	}

	public void setAmtRepair(BigDecimal amtRepair) {
		this.amtRepair = amtRepair;
	}

	public BigDecimal getAmtSettle() {
		return amtSettle;
	}

	public void setAmtSettle(BigDecimal amtSettle) {
		this.amtSettle = amtSettle;
	}

	public BigDecimal getAmtPrep() {
		return amtPrep;
	}

	public void setAmtPrep(BigDecimal amtPrep) {
		this.amtPrep = amtPrep;
	}

	public BigDecimal getAmtPrepRt() {
		return amtPrepRt;
	}

	public void setAmtPrepRt(BigDecimal amtPrepRt) {
		this.amtPrepRt = amtPrepRt;
	}

	public BigDecimal getAmtInsu() {
		return amtInsu;
	}

	public void setAmtInsu(BigDecimal amtInsu) {
		this.amtInsu = amtInsu;
	}



}
