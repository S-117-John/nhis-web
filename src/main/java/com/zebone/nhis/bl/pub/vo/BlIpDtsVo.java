package com.zebone.nhis.bl.pub.vo;

import java.util.List;

public class BlIpDtsVo {
	 private String pkPv;
     private String pkSettle;
     private String codeBill;
     private String name;
     private double ratioSelf;
     private double amtHp;
     private double amtPi;
     private String dateCg;
     private String nameCg;
     private String spec;
     private String pkUnit;
     private String quan;
     private double amount;
	 private double amountPi;
     private String nameDept;
     private String codeHp;
     private String itemCode;
     private String dateHap;
     private String uName;
     private String pkItemcate;
     private List<BlIpDtsVo> blIpDtInfo ;
     private String flagSettle;

	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	public String getItemCode() {
		return itemCode;
	}


	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}


	public String getPkItemcate() {
		return pkItemcate;
	}


	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}


	public String getuName() {
		return uName;
	}


	public void setuName(String uName) {
		this.uName = uName;
	}


	public String getPkPv() {
		return pkPv;
	}


	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}


	public String getPkSettle() {
		return pkSettle;
	}


	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}


	public String getCodeBill() {
		return codeBill;
	}


	public void setCodeBill(String codeBill) {
		this.codeBill = codeBill;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public double getRatioSelf() {
		return ratioSelf;
	}


	public void setRatioSelf(double ratioSelf) {
		this.ratioSelf = ratioSelf;
	}


	public double getAmtHp() {
		return amtHp;
	}


	public void setAmtHp(double amtHp) {
		this.amtHp = amtHp;
	}


	public double getAmtPi() {
		return amtPi;
	}


	public void setAmtPi(double amtPi) {
		this.amtPi = amtPi;
	}


	public String getDateCg() {
		return dateCg;
	}


	public void setDateCg(String dateCg) {
		this.dateCg = dateCg;
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


	public String getPkUnit() {
		return pkUnit;
	}


	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}


	public String getQuan() {
		return quan;
	}


	public void setQuan(String quan) {
		this.quan = quan;
	}

	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getNameDept() {
		return nameDept;
	}


	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}


	public String getCodeHp() {
		return codeHp;
	}


	public void setCodeHp(String codeHp) {
		this.codeHp = codeHp;
	}


	public String getDateHap() {
		return dateHap;
	}


	public void setDateHap(String dateHap) {
		this.dateHap = dateHap;
	}


	public List<BlIpDtsVo> getBlIpDtInfo() {
		return blIpDtInfo;
	}


	public void setBlIpDtInfo(List<BlIpDtsVo> blIpDtInfo) {this.blIpDtInfo = blIpDtInfo;}

	public double getAmountPi() {return amountPi;}

	public void setAmountPi(double amountPi) {this.amountPi = amountPi;}
}
