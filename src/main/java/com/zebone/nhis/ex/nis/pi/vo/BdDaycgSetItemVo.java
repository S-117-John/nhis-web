package com.zebone.nhis.ex.nis.pi.vo;

import com.zebone.nhis.common.module.base.bd.price.BdDaycgSetItem;

public class BdDaycgSetItemVo extends BdDaycgSetItem {
	private String name;
	private String code;
	private Double price;

	
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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

}
