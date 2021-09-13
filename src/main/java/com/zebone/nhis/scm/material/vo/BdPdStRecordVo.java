package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdStRecordVo {
	
	//业务类型
	private String dtSttype;
	
	//业务类型名称
	private String sttypeName;
	
	//物品名称
	private String name;
	
	//规格
	private String spec;
	
	//生产厂家
	private String pkFactory;
	
	//生产厂家名称
	private String factoryName;
	
	//单位
	private String pkUnitPack;
	
	//单位名称
	private String unitName;
	
	//数量
	private Long quanPack;
	
	//购入金额
	private Double amountCost;
	
	//零售金额
	private Double amount;
	
	//库单日期
	private Date dateChk;

	public Long getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Long quanPack) {
		this.quanPack = quanPack;
	}

	public String getSttypeName() {
		return sttypeName;
	}

	public void setSttypeName(String sttypeName) {
		this.sttypeName = sttypeName;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
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

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
