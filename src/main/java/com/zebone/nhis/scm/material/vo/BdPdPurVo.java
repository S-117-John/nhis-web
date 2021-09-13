package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdPurVo {
	
	//库单主键
	private String pkPdst;
	
	//供应商主键
	private String pkSupplyer;
	
	//供应商名称
	private String supplyerName;
	
	//物品名称
	private String name;
	
	//规格
	private String spec;
	
	//生产厂家主键
	private String pkFactory;
	
	//生产厂家名称
	private String factoryName;
	
	//单位主键
	private String pkUnitPack;
	
	//单位名称
	private String unitName;
	
	//数量
	private Double quanPack;
	
	//成本金额
	private Double amountCost;
	
	//零售金额
	private Double amount;
	
	//审核日期
	private Date dateChk;
	
	
	public String getSupplyerName() {
		return supplyerName;
	}

	public void setSupplyerName(String supplyerName) {
		this.supplyerName = supplyerName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getPkPdst() {
		return pkPdst;
	}

	public void setPkPdst(String pkPdst) {
		this.pkPdst = pkPdst;
	}

	public String getPkSupplyer() {
		return pkSupplyer;
	}

	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getPkFactory() {
		return pkFactory;
	}

	public void setPkFactory(String pkFactory) {
		this.pkFactory = pkFactory;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public Double getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Double quanPack) {
		this.quanPack = quanPack;
	}

	public Double getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(Double amountCost) {
		this.amountCost = amountCost;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}
	
	
}
