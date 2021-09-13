package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

public class BlPubPriceVo {

	/**
	 * 收费项目主键
	 */
	private String pkItem;

	/**
	 * 原始单价
	 */
	private BigDecimal priceOrg;

	/**
	 * 子项目对应的数量
	 */
	private BigDecimal quanChild;

	/**
	 * 当前单价
	 */
	private BigDecimal price;

	/**
	 * 优惠类型
	 */
	private String pkDisc;

	/**
	 * 优惠比例
	 */
	private BigDecimal ratioDisc;

	/**
	 * 自费比例
	 */
	private BigDecimal ratioSelf;
	
	////以下是中山二院添加特殊定价信息
	private Double ratioChildren;//六岁以下儿童加价比例
	private Double ratioSpec;//特诊加价比例
	private String flagHppi;//患者自付金额标志--广州公医添加
	private BigDecimal amtHppi;//(患者自付金额)  --广州公医添加
	
	public String getFlagHppi() {
		return flagHppi;
	}

	public void setFlagHppi(String flagHppi) {
		this.flagHppi = flagHppi;
	}

	public BigDecimal getAmtHppi() {
		return amtHppi;
	}

	public void setAmtHppi(BigDecimal amtHppi) {
		this.amtHppi = amtHppi;
	}

	public Double getRatioChildren() {
		return ratioChildren;
	}

	public void setRatioChildren(Double ratioChildren) {
		this.ratioChildren = ratioChildren;
	}

	public Double getRatioSpec() {
		return ratioSpec;
	}

	public void setRatioSpec(Double ratioSpec) {
		this.ratioSpec = ratioSpec;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public BigDecimal getPriceOrg() {
		return priceOrg;
	}

	public void setPriceOrg(BigDecimal priceOrg) {
		this.priceOrg = priceOrg;
	}

	public BigDecimal getQuanChild() {
		return quanChild;
	}

	public void setQuanChild(BigDecimal quanChild) {
		this.quanChild = quanChild;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getPkDisc() {
		return pkDisc;
	}

	public void setPkDisc(String pkDisc) {
		this.pkDisc = pkDisc;
	}

	public BigDecimal getRatioDisc() {
		return ratioDisc;
	}

	public void setRatioDisc(BigDecimal ratioDisc) {
		this.ratioDisc = ratioDisc;
	}

	public BigDecimal getRatioSelf() {
		return ratioSelf;
	}

	public void setRatioSelf(BigDecimal ratioSelf) {
		this.ratioSelf = ratioSelf;
	}


	

}
