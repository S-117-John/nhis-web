package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdOrd;

public class BdOrdAndPkDeptVo extends BdOrd{

	private String pkDept;

	private String setMeal;

	private String price;

	private String priceChd;

	private String priceVip;
	
	private String pkOrg;
	
	private String nameOrg;

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getSetMeal() {
		return setMeal;
	}

	public void setSetMeal(String setMeal) {
		this.setMeal = setMeal;
	}

	public String getPrice() {return price;}

	public void setPrice(String price) {this.price = price;}

	public String getPriceChd() {return priceChd;}

	public void setPriceChd(String priceChd) {this.priceChd = priceChd;}

	public String getPriceVip() {return priceVip;}

	public void setPriceVip(String priceVip) {this.priceVip = priceVip;}

	public String getPkOrg() {
		return pkOrg;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	
}
