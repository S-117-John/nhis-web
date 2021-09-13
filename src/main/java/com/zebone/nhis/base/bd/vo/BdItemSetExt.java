package com.zebone.nhis.base.bd.vo;

import java.util.Date;

import com.zebone.nhis.common.module.base.bd.srv.BdItemSet;

public class BdItemSetExt extends  BdItemSet{

	/**
	 *  pkItemChild子项收费项目编码
	 */
	private String itemCode;
	
	/**
	 * pkItemChild子项收费项目名称
	 */
	private String itemName;
	
	/**
	 * pkItemChild子项收费项目参考价
	 */
	private double itemPrice;
	
	/**
	 * pkItemChild子项收费项目参考价*数量
	 */
	private double itemHjPrice;

	
	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public double getItemHjPrice() {
		return itemHjPrice;
	}

	public void setItemHjPrice(double itemHjPrice) {
		this.itemHjPrice = itemHjPrice;
	}
}
