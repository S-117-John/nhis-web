package com.zebone.nhis.bl.pub.syx.vo;

import java.util.Date;

/**
 * 收费记录
 * 
 * @author
 * 
 */
public class OtherChargeRecordVo {

	private String pkCgip;
	private String namePi;
	private String nameCg;
	private String spec;
	private Double price;
	private Double quan;
	private Double amount;
	private String pkUnit;
	private String nameEmpEx;
	private Date dateCg;
	private String nameEmpCg;
	private String unitname;
	private String pkDeptCg;
	private String pkPv;
	private String barcode;
	private String cateName;
	private String nameDeptEx;
	private String pkCgOp;
	private String pkCnord;
	private String flagSettle;

	public String getNameDeptEx() {
		return nameDeptEx;
	}

	public void setNameDeptEx(String nameDeptEx) {
		this.nameDeptEx = nameDeptEx;
	}

	public String getCateName() {
		return cateName;
	}
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkDeptCg() {
		return pkDeptCg;
	}
	public void setPkDeptCg(String pkDeptCg) {
		this.pkDeptCg = pkDeptCg;
	}
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
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
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getQuan() {
		return quan;
	}
	public void setQuan(Double quan) {
		this.quan = quan;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public String getNameEmpEx() {
		return nameEmpEx;
	}
	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}
	public Date getDateCg() {
		return dateCg;
	}
	public void setDateCg(Date dateCg) {
		this.dateCg = dateCg;
	}
	public String getNameEmpCg() {
		return nameEmpCg;
	}
	public void setNameEmpCg(String nameEmpCg) {
		this.nameEmpCg = nameEmpCg;
	}
	public String getUnitname() {
		return unitname;
	}
	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public String getPkCgOp() {
		return pkCgOp;
	}

	public void setPkCgOp(String pkCgOp) {
		this.pkCgOp = pkCgOp;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}
	
}
