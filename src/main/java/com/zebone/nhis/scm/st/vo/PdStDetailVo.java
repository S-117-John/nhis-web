package com.zebone.nhis.scm.st.vo;

/***
 * 物品，包括出入库、上月结账
 * 
 * @author wangpeng
 * @date 2016年12月6日
 *
 */
public class PdStDetailVo {
	
	/** 物品主键 */
	private String pkPd;
	
	/** 包装单位 */
	private String pkUnitPack;
	
	/** 包装量 */
	private Integer packSize;
	
	/** 结存数量_基本 */
	private Double quanMinCc;
	
	/** 成本单价_当前包装 */
	private Double priceCostCc;
	
	/** 零售单价_当前包装 */
	private Double priceCc;
	
	/** 成本金额 */
	private Double amountCostCc;
	
	/** 零售金额 */
	private Double amountCc;
	
	/** 数量_基本（入库） */
	private Double quanMinR;
	
	/** 购入单价（入库） */
	private Double priceCostR;
	
	/** 零售单价（入库） */
	private Double priceR;
	
	/** 购入金额（入库） */
	private Double amountCostR;
	
	/** 零售金额（入库） */
	private Double amountR;
	
	/** 数量_基本（出库） */
	private Double quanMinC;
	
	/** 购入单价（出库） */
	private Double priceCostC;
	
	/** 零售单价（出库） */
	private Double priceC;
	
	/** 购入金额（出库） */
	private Double amountCostC;
	
	/** 零售金额（出库） */
	private Double amountC;

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public Double getQuanMinCc() {
		return quanMinCc;
	}

	public void setQuanMinCc(Double quanMinCc) {
		this.quanMinCc = quanMinCc;
	}

	public Double getPriceCostCc() {
		return priceCostCc;
	}

	public void setPriceCostCc(Double priceCostCc) {
		this.priceCostCc = priceCostCc;
	}

	public Double getPriceCc() {
		return priceCc;
	}

	public void setPriceCc(Double priceCc) {
		this.priceCc = priceCc;
	}

	public Double getAmountCostCc() {
		return amountCostCc;
	}

	public void setAmountCostCc(Double amountCostCc) {
		this.amountCostCc = amountCostCc;
	}

	public Double getAmountCc() {
		return amountCc;
	}

	public void setAmountCc(Double amountCc) {
		this.amountCc = amountCc;
	}

	public Double getQuanMinR() {
		return quanMinR;
	}

	public void setQuanMinR(Double quanMinR) {
		this.quanMinR = quanMinR;
	}

	public Double getPriceCostR() {
		return priceCostR;
	}

	public void setPriceCostR(Double priceCostR) {
		this.priceCostR = priceCostR;
	}

	public Double getPriceR() {
		return priceR;
	}

	public void setPriceR(Double priceR) {
		this.priceR = priceR;
	}

	public Double getAmountCostR() {
		return amountCostR;
	}

	public void setAmountCostR(Double amountCostR) {
		this.amountCostR = amountCostR;
	}

	public Double getAmountR() {
		return amountR;
	}

	public void setAmountR(Double amountR) {
		this.amountR = amountR;
	}

	public Double getQuanMinC() {
		return quanMinC;
	}

	public void setQuanMinC(Double quanMinC) {
		this.quanMinC = quanMinC;
	}

	public Double getPriceCostC() {
		return priceCostC;
	}

	public void setPriceCostC(Double priceCostC) {
		this.priceCostC = priceCostC;
	}

	public Double getPriceC() {
		return priceC;
	}

	public void setPriceC(Double priceC) {
		this.priceC = priceC;
	}

	public Double getAmountCostC() {
		return amountCostC;
	}

	public void setAmountCostC(Double amountCostC) {
		this.amountCostC = amountCostC;
	}

	public Double getAmountC() {
		return amountC;
	}

	public void setAmountC(Double amountC) {
		this.amountC = amountC;
	}

}
