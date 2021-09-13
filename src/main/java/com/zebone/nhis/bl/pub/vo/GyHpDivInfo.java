package com.zebone.nhis.bl.pub.vo;

import java.math.BigDecimal;

/**
 * 公医高值耗材策略信息
 * @author 32916
 *
 */
public class GyHpDivInfo {
	private BigDecimal priceMin;
	private BigDecimal priceMax;
	private BigDecimal ratioInit;
	private BigDecimal ratio;
	private BigDecimal ratioPi;
	private String euCalcmode;
	public BigDecimal getPriceMin() {
		return priceMin;
	}
	public void setPriceMin(BigDecimal priceMin) {
		this.priceMin = priceMin;
	}
	public BigDecimal getPriceMax() {
		return priceMax;
	}
	public void setPriceMax(BigDecimal priceMax) {
		this.priceMax = priceMax;
	}
	public BigDecimal getRatioInit() {
		return ratioInit;
	}
	public void setRatioInit(BigDecimal ratioInit) {
		this.ratioInit = ratioInit;
	}
	public BigDecimal getRatio() {
		return ratio;
	}
	public void setRatio(BigDecimal ratio) {
		this.ratio = ratio;
	}
	public BigDecimal getRatioPi() {
		return ratioPi;
	}
	public void setRatioPi(BigDecimal ratioPi) {
		this.ratioPi = ratioPi;
	}
	public String getEuCalcmode() {
		return euCalcmode;
	}
	public void setEuCalcmode(String euCalcmode) {
		this.euCalcmode = euCalcmode;
	}
	
}
