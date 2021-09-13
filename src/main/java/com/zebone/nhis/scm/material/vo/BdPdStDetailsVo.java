package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdStDetailsVo {
	
	//库单明细主键
	private String pkPdstdt;
	
	//物品主键
	private String pkPd;
	
	//物品编码
	private String code;
	
	//物品名称
	private String name;
	
	//规格
	private String spec;
	
	//生产厂家主键
	private String pkFactory;
	
	//厂家名称
	private String factoryName;
	
	//单位主键
	private String pkUnitPack;
	
	//单位名称
	private String unitName;
	
	//数量_当前
	private Long quanPack; 
	
	//数量_基本
	private Long quanMin;
	
	//购入金额
	private Double amountCost;
	
	//购入单价
	private Double priceCost;
	
	//零售单价
	private Double price;
	
	//零售金额
	private Double amount;
	
	//批号
	private String batchNo;
	
	//失效日期
	private Date dateExpire;
	
	//拼音码
	private String spcode;
	
	//生产日期
	private Date DateFac;
	

	public Date getDateFac() {
		return DateFac;
	}

	public void setDateFac(Date dateFac) {
		DateFac = dateFac;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public Double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getPkPdstdt() {
		return pkPdstdt;
	}

	public void setPkPdstdt(String pkPdstdt) {
		this.pkPdstdt = pkPdstdt;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Long getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(Long quanPack) {
		this.quanPack = quanPack;
	}

	public Long getQuanMin() {
		return quanMin;
	}

	public void setQuanMin(Long quanMin) {
		this.quanMin = quanMin;
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

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	
	
}
