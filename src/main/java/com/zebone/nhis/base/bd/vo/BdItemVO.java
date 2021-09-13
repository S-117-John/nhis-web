package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;

public class BdItemVO extends BdItem {
	private String unitname;

	private String catename;

	private String catecode;

	public String getCatename() {
		return catename;
	}

	public void setCatename(String catename) {
		this.catename = catename;
	}

	public String getCatecode() {
		return catecode;
	}

	public void setCatecode(String catecode) {
		this.catecode = catecode;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	/**
	 * 收费数量
	 */
	private Double amount;

	/**
	 * 关联主键
	 */
	private String pkOrdItem;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPkOrdItem() {
		return pkOrdItem;
	}

	public void setPkOrdItem(String pkOrdItem) {
		this.pkOrdItem = pkOrdItem;
	}
}
