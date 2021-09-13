package com.zebone.nhis.bl.pub.syx.vo;

public class BlSpItemStyVo extends BlItemPriceStyVo  {
	
	//初始比例
	private Double ratioInit;
	
	//报销限额
	private Double amountMax;
	
	//计算方式0患者自付比例1自定义比例
	private String euCalcmode;
	
	//自定义比例
	private Double ratio;
	
	//单位支付比例
	private Double ratioUnit;

	public Double getRatioUnit() {
		return ratioUnit;
	}

	public void setRatioUnit(Double ratioUnit) {
		this.ratioUnit = ratioUnit;
	}

	public Double getRatioInit() {
		return ratioInit;
	}

	public void setRatioInit(Double ratioInit) {
		this.ratioInit = ratioInit;
	}

	public Double getAmountMax() {
		return amountMax;
	}

	public void setAmountMax(Double amountMax) {
		this.amountMax = amountMax;
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
