package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class MtlDeptPdStVo {
	
	//业务类型
	private String dtSttype;
	
	//业务类型名称
	private String sttypeName;
	
	//编码
	private String code;
	
	//物品名称
	private String name;
	
	//规格
	private String spec;
	
	private String spcode;
	
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
	private Date dateSt;
	
	//单据号
	private String codeSt;

	private String pkStoreSt;

	private String storeStName;
	
	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getSttypeName() {
		return sttypeName;
	}

	public void setSttypeName(String sttypeName) {
		this.sttypeName = sttypeName;
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

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
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

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getPkStoreSt() {
		return pkStoreSt;
	}

	public void setPkStoreSt(String pkStoreSt) {
		this.pkStoreSt = pkStoreSt;
	}

	public String getStoreStName() {
		return storeStName;
	}

	public void setStoreStName(String storeStName) {
		this.storeStName = storeStName;
	}
	
	
}
