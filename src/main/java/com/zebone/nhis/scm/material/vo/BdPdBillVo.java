package com.zebone.nhis.scm.material.vo;

public class BdPdBillVo {
	
	//物品编码
	private String pkPd;
	
	//名称
	private String name;
	
	//规格
	private String spec;
	
	//厂家主键
	private String pkFactory;
	
	//厂家名称
	private String factoryName;
	
	//单位主键
	private String pkUnitPack;
	
	//单位名称
	private String unitName;
	
	//初期数量
	private Long cqQuan;
	
	//初期金额
	private Double cqAmt;
	
	//收入数量
	private Long srQuan;
	
	//收入金额
	private Double srAmt;
	
	//支出数量
	private Long zcQuan;
	
	//支出金额
	private Double zcAmt;
	
	//结存数量
	private Long jcQuan;
	
	//结存金额
	private Double jcAmt;
	
	//调价金额
	private Double tjAmt;

	public Double getTjAmt() {
		return tjAmt;
	}

	public void setTjAmt(Double tjAmt) {
		this.tjAmt = tjAmt;
	}

	public Long getJcQuan() {
		return jcQuan;
	}

	public void setJcQuan(Long jcQuan) {
		this.jcQuan = jcQuan;
	}

	public Double getJcAmt() {
		return jcAmt;
	}

	public void setJcAmt(Double jcAmt) {
		this.jcAmt = jcAmt;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
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

	public Long getCqQuan() {
		return cqQuan;
	}

	public void setCqQuan(Long cqQuan) {
		this.cqQuan = cqQuan;
	}

	public Double getCqAmt() {
		return cqAmt;
	}

	public void setCqAmt(Double cqAmt) {
		this.cqAmt = cqAmt;
	}

	public Long getSrQuan() {
		return srQuan;
	}

	public void setSrQuan(Long srQuan) {
		this.srQuan = srQuan;
	}

	public Double getSrAmt() {
		return srAmt;
	}

	public void setSrAmt(Double srAmt) {
		this.srAmt = srAmt;
	}

	public Long getZcQuan() {
		return zcQuan;
	}

	public void setZcQuan(Long zcQuan) {
		this.zcQuan = zcQuan;
	}

	public Double getZcAmt() {
		return zcAmt;
	}

	public void setZcAmt(Double zcAmt) {
		this.zcAmt = zcAmt;
	}
	
	

	
	
	
}
