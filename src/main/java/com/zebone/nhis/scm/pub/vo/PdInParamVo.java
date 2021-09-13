package com.zebone.nhis.scm.pub.vo;

import java.util.Date;

public class PdInParamVo {

	private String pkPv;// 就诊主键

	private String pkCnOrd;// 医嘱主键

	private String pkPdapdt;// 退药请领明细主键

	private String pkPd;// 物品主键

	private String batchNo;// 物品批号
	
	private Date dateExpire;//失效日期

	private Double priceCost;// 成本价

	private Double price;// 零售价

	private Double quanPack;// 数量（包装单位）

	private Integer packSize;// 包装量

	private String pkUnitPack;// 仓库物品中对应的包装单位

	private Double quanMin;// 数量（基本单位）

	private Double amount;
	

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getPkPv() {

		return pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public Integer getPackSize() {

		return packSize;
	}

	public void setPackSize(Integer packSize) {

		this.packSize = packSize;
	}

	public String getPkCnOrd() {

		return pkCnOrd;
	}

	public void setPkCnOrd(String pkCnOrd) {

		this.pkCnOrd = pkCnOrd;
	}

	public String getPkPdapdt() {

		return pkPdapdt;
	}

	public String getPkUnitPack() {

		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {

		this.pkUnitPack = pkUnitPack;
	}

	public void setPkPdapdt(String pkPdapdt) {

		this.pkPdapdt = pkPdapdt;
	}

	public String getPkPd() {

		return pkPd;
	}

	public void setPkPd(String pkPd) {

		this.pkPd = pkPd;
	}

	public String getBatchNo() {

		return batchNo;
	}

	public void setBatchNo(String batchNo) {

		this.batchNo = batchNo;
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

	public Double getQuanPack() {

		return quanPack;
	}

	public void setQuanPack(Double quanPack) {

		this.quanPack = quanPack;
	}

	public Double getQuanMin() {

		return quanMin;
	}

	public void setQuanMin(Double quanMin) {

		this.quanMin = quanMin;
	}

	public Double getAmount() {

		return amount;
	}

	public void setAmount(Double amount) {

		this.amount = amount;
	}

}
