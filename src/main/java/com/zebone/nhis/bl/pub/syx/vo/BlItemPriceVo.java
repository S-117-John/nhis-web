package com.zebone.nhis.bl.pub.syx.vo;

/**
 * 项目价格信息
 * @author c
 *
 */
public class BlItemPriceVo {
	
	//项目单价
	private Double price;
	
	//自付比例
	private Double rate;
	
	//收费项目分类
	private String pkItemcate;
	
	//患者自付金额
	private Double amtHppi;
	
	//单位自付金额
	private Double amtUnit;
	
	//策略类型(0 床位费策略，1特殊项目策略，2高值耗材策略)
	private String euDivtype;
	
	public Double getAmtUnit() {
		return amtUnit;
	}

	public void setAmtUnit(Double amtUnit) {
		this.amtUnit = amtUnit;
	}

	public String getEuDivtype() {
		return euDivtype;
	}

	public void setEuDivtype(String euDivtype) {
		this.euDivtype = euDivtype;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
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

	public Double getAmtHppi() {
		return amtHppi;
	}

	public void setAmtHppi(Double amtHppi) {
		this.amtHppi = amtHppi;
	}
	
	
}
