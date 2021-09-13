package com.zebone.nhis.bl.pub.syx.vo;

public class BlHvItemStyVo extends BlItemPriceStyVo {
	
	//下限
	private Double priceMin;
	
	//上限
	private Double priceMax;
	
	//初始化自付比例
	private Double ratioInit;

	//计算方式
	private String euCalcmode;

	//余额计算比例
	private Double ratio;

	public Double getPriceMin() {
		return priceMin;
	}

	public void setPriceMin(Double priceMin) {
		this.priceMin = priceMin;
	}

	public Double getPriceMax() {
		return priceMax;
	}

	public void setPriceMax(Double priceMax) {
		this.priceMax = priceMax;
	}

	public Double getRatioInit() {
		return ratioInit;
	}

	public void setRatioInit(Double ratioInit) {
		this.ratioInit = ratioInit;
	}

	public String getEuCalcmode() {
		return euCalcmode;
	}

	public void setEuCalcmode(String euCalcmode) {
		this.euCalcmode = euCalcmode;
	}

	public Double getRatio() {
		return ratio;
	}

	public void setRatio(Double ratio) {
		this.ratio = ratio;
	}
	
	

}
