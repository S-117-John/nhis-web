package com.zebone.nhis.scm.purchase.vo;

/*
 * 采购查询-按供应商查询对应的药品明细
 */
public class PuSearchQueryDrugBySupplyerResult {
	
	private String code;//编码
	
	private String name;//名称
	
	private String spec;//规格
	
	private String pkFactory;//生产厂家
	
	private String pkUnitPack;//单位
	
	private String priceCost;//成本单价 number
	
	private String quanPack;//数量 number
	
	private String amountCost;//金额 number
	
	private String dateSt;//入库日期  date

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

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public String getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(String priceCost) {
		this.priceCost = priceCost;
	}

	public String getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(String quanPack) {
		this.quanPack = quanPack;
	}

	public String getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(String amountCost) {
		this.amountCost = amountCost;
	}

	public String getDateSt() {
		return dateSt;
	}

	public void setDateSt(String dateSt) {
		this.dateSt = dateSt;
	}
	
	
}
