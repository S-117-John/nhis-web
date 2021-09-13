package com.zebone.nhis.compay.pub.syx.vo;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;

public class CostZtFeeVo {
	/* 名称 */
	@Field(value = "NAME")
	private String name;
	/* 数量 */
	@Field(value = "QUAN")
	private Double quan;
	
	/* 金额 */
	@Field(value = "AMOUNT")
	private Double amount;

	/* 单价 */
	@Field(value = "PRICE")
	private Double price;
	
	/*计划执行时间 */
	@Field(value = "DATEPLAN")
	private Date dateplan;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDateplan() {
		return dateplan;
	}

	public void setDateplan(Date dateplan) {
		this.dateplan = dateplan;
	}	
	
	
}
