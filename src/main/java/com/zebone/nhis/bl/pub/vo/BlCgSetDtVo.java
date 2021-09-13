package com.zebone.nhis.bl.pub.vo;

import com.zebone.nhis.common.module.bl.BlCgsetDt;

public class BlCgSetDtVo extends BlCgsetDt {
	private String unit;
	private String spec;
	private double price;
	private String nameItem;
	private String spcodeItem;
	private String flagPd;
	
	//------中二，住院其他记账功能使用--------
	private double priceCost;
	private String codeFreq;
	private String codeSupply;
	private String dosageDef;
	private String pkUnitDef;
	private String pkUnitPd;
	private String packSize;
	//-------------------------------
	
	
	public String getCodeFreq() {
		return codeFreq;
	}

	public String getPackSize() {
		return packSize;
	}

	public void setPackSize(String packSize) {
		this.packSize = packSize;
	}

	public String getPkUnitPd() {
		return pkUnitPd;
	}

	public void setPkUnitPd(String pkUnitPd) {
		this.pkUnitPd = pkUnitPd;
	}

	public void setCodeFreq(String codeFreq) {
		this.codeFreq = codeFreq;
	}

	public String getCodeSupply() {
		return codeSupply;
	}

	public void setCodeSupply(String codeSupply) {
		this.codeSupply = codeSupply;
	}

	public String getDosageDef() {
		return dosageDef;
	}

	public void setDosageDef(String dosageDef) {
		this.dosageDef = dosageDef;
	}

	public String getPkUnitDef() {
		return pkUnitDef;
	}

	public void setPkUnitDef(String pkUnitDef) {
		this.pkUnitDef = pkUnitDef;
	}

	public double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(double priceCost) {
		this.priceCost = priceCost;
	}

	public String getFlagPd() {
		return flagPd;
	}

	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getSpcodeItem() {
		return spcodeItem;
	}

	public void setSpcodeItem(String spcodeItem) {
		this.spcodeItem = spcodeItem;
	}

}
