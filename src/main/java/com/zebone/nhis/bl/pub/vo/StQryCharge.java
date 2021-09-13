package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;


public class StQryCharge {
	
	private String nameCg;
	private String spec;
	private String unit;
	private BigDecimal price;
	private BigDecimal quan;
	private BigDecimal amount;
	private BigDecimal amountPi;
	private String pkBill;
	private String nameBill;
	private String codeBill;
	private String nameEmpApp;
	private String nameDeptEx;
	private BigDecimal ylQuan;
	private String nameSupply;
	private String NameOrd;
	private String winnoConf;

	public String getNameOrd() {
		return NameOrd;
	}

	public void setNameOrd(String nameOrd) {
		NameOrd = nameOrd;
	}

	public BigDecimal getYlQuan() {
		return ylQuan;
	}

	public void setYlQuan(BigDecimal ylQuan) {
		this.ylQuan = ylQuan;
	}

	public String getNameSupply() {
		return nameSupply;
	}

	public void setNameSupply(String nameSupply) {
		this.nameSupply = nameSupply;
	}

	public String getNameEmpApp() {
		return nameEmpApp;
	}
	public void setNameEmpApp(String nameEmpApp) {
		this.nameEmpApp = nameEmpApp;
	}
	public String getNameDeptEx() {
		return nameDeptEx;
	}
	public void setNameDeptEx(String nameDeptEx) {
		this.nameDeptEx = nameDeptEx;
	}
	public String getPkBill() {
		return pkBill;
	}
	public void setPkBill(String pkBill) {
		this.pkBill = pkBill;
	}

	public String getNameBill() {
		return nameBill;
	}

	public void setNameBill(String nameBill) {
		this.nameBill = nameBill;
	}

	public String getCodeBill() {
		return codeBill;
	}

	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}

	public String getNameCg() {
		return nameCg;
	}

	public void setNameCg(String nameCg) {
		this.nameCg = nameCg;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getQuan() {
		return quan;
	}

	public void setQuan(BigDecimal quan) {
		this.quan = quan;
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

	public String getWinnoConf() {
		return winnoConf;
	}

	public void setWinnoConf(String winnoConf) {
		this.winnoConf = winnoConf;
	}

}
