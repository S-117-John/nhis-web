package com.zebone.nhis.scm.material.vo;

/**
 * 收发存汇总VO
 * @author chengjia
 *
 */
public class MtlTransSumVo {
	
	/**
	 * 物品分类主键
	 */
	private String pkPdcate;
	
	/**
	 * 物品分类编码
	 */
	private String codePdcate;
	
	/**
	 * 物品分类
	 */
	private String namePdcate;

	/**
	 * 初期金额
	 */
	private Double amtBegin;

	/**
	 * 收入金额
	 */
	private Double amtIn;

	/**
	 *支出金额 
	 */
	private Double amtOut;

	/**
	 *结存金额
 	 */
	private Double amtBalance;
	
	/**
	 *
	 * 调价金额
	 */
	private Double amtPrice;
	
	/**
	 * 零售金额
	 */
	private Double amt;
	
	/**
	 * 成本金额
	 */
	private Double amtCost;
	
	
	public String getNamePdcate() {
		return namePdcate;
	}

	public void setNamePdcate(String namePdcate) {
		this.namePdcate = namePdcate;
	}

	public Double getAmtBegin() {
		return amtBegin;
	}

	public void setAmtBegin(Double amtBegin) {
		this.amtBegin = amtBegin;
	}

	public Double getAmtIn() {
		return amtIn;
	}

	public void setAmtIn(Double amtIn) {
		this.amtIn = amtIn;
	}

	public Double getAmtOut() {
		return amtOut;
	}

	public void setAmtOut(Double amtOut) {
		this.amtOut = amtOut;
	}

	public Double getAmtBalance() {
		return amtBalance;
	}

	public void setAmtBalance(Double amtBalance) {
		this.amtBalance = amtBalance;
	}

	public String getPkPdcate() {
		return pkPdcate;
	}

	public void setPkPdcate(String pkPdcate) {
		this.pkPdcate = pkPdcate;
	}

	public Double getAmt() {
		return amt;
	}

	public void setAmt(Double amt) {
		this.amt = amt;
	}

	public Double getAmtCost() {
		return amtCost;
	}

	public void setAmtCost(Double amtCost) {
		this.amtCost = amtCost;
	}

	public String getCodePdcate() {
		return codePdcate;
	}

	public void setCodePdcate(String codePdcate) {
		this.codePdcate = codePdcate;
	}

	public Double getAmtPrice() {
		return amtPrice;
	}

	public void setAmtPrice(Double amtPrice) {
		this.amtPrice = amtPrice;
	}

	
	
}
