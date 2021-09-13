package com.zebone.nhis.bl.pub.syx.vo;

/**
 * 项目策略价格信息定义vo
 * @author c
 *
 */
public class BlItemPriceStyVo {
	
	//住院自付比例
	private Double rateIp;
	
	//门诊自付比例
	private Double rateOp;
	
	//急诊自付比例
	private Double rateEr;
	
	//体检自付比例
	private Double ratePe;
	
	//自付比例
	private Double ratePi;
	
	//计算方式(0自定义价格，1自定义自付比例，2患者自付比例，3患者自付乘以自定义比例)
	private String euDivide;
	
	//自定义价格
	private Double amount;
	
	//自定义比例
	private Double rate;
	
	//收费项目分类
	private String pkItemcate;
	
	//支付类型
	private String dtHptype;
	
	//收费项目分类
	private String pkItem;
	
	//医保主键
	private String pkHp;
	
	

	public String getPkHp() {
		return pkHp;
	}

	public void setPkHp(String pkHp) {
		this.pkHp = pkHp;
	}

	public String getDtHptype() {
		return dtHptype;
	}

	public void setDtHptype(String dtHptype) {
		this.dtHptype = dtHptype;
	}

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public Double getRatePi() {
		return ratePi;
	}

	public void setRatePi(Double ratePi) {
		this.ratePi = ratePi;
	}

	public Double getRateIp() {
		return rateIp;
	}

	public void setRateIp(Double rateIp) {
		this.rateIp = rateIp;
	}

	public Double getRateOp() {
		return rateOp;
	}

	public void setRateOp(Double rateOp) {
		this.rateOp = rateOp;
	}

	public Double getRateEr() {
		return rateEr;
	}

	public void setRateEr(Double rateEr) {
		this.rateEr = rateEr;
	}

	public Double getRatePe() {
		return ratePe;
	}

	public void setRatePe(Double ratePe) {
		this.ratePe = ratePe;
	}

	public String getEuDivide() {
		return euDivide;
	}

	public void setEuDivide(String euDivide) {
		this.euDivide = euDivide;
	}


	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getPkItemcate() {
		return pkItemcate;
	}

	public void setPkItemcate(String pkItemcate) {
		this.pkItemcate = pkItemcate;
	}
	
	
	
	
}
