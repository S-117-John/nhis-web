package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdItem;

public class BdOrdItemExt extends BdOrdItem {
	
    /**扩展字段 取自bd_item表**/
    private String name;
    
    private double price;
    
    private String spec;

    private String priceUnit;

    private String pname;

	private double ratioChildren;

	private double amountChildren;

	private double ratioSpec;

	private double amountSpec;

	private String euCdmode;
	private String euSpmode;

	/**
	 * 收费项目编码
	 * */
	private String code;
	
	/**
	 * 收费项目编码标准码
	 * */
	private String codeStd;

	public double getRatioChildren() {
		return ratioChildren;
	}

	public void setRatioChildren(double ratioChildren) {
		this.ratioChildren = ratioChildren;
	}

	public double getAmountChildren() {
		return amountChildren;
	}

	public void setAmountChildren(double amountChildren) {
		this.amountChildren = amountChildren;
	}

	public double getRatioSpec() {
		return ratioSpec;
	}

	public void setRatioSpec(double ratioSpec) {
		this.ratioSpec = ratioSpec;
	}

	public double getAmountSpec() {
		return amountSpec;
	}

	public void setAmountSpec(double amountSpec) {
		this.amountSpec = amountSpec;
	}

	public String getEuCdmode() {
		return euCdmode;
	}

	public void setEuCdmode(String euCdmode) {
		this.euCdmode = euCdmode;
	}

	public String getEuSpmode() {
		return euSpmode;
	}

	public void setEuSpmode(String euSpmode) {
		this.euSpmode = euSpmode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public String getPriceUnit() {
		return priceUnit;
	}

	public void setPriceUnit(String priceUnit) {
		this.priceUnit = priceUnit;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getCodeStd() {
		return codeStd;
	}

	public void setCodeStd(String codeStd) {
		this.codeStd = codeStd;
	}
	
}
