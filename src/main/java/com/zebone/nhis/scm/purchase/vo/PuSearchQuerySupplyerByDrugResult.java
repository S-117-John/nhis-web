package com.zebone.nhis.scm.purchase.vo;

/*
 * 采购查询-根据药品查询关联的采购供应商
 */
public class PuSearchQuerySupplyerByDrugResult {
	
	private String code;//编码
	
	private String name;//名称
	
	private String priceCost;//成本单价 number
		
	private String quanPack;//数量 number
	
	private String pkUnitPack;//单位
	
	private String amountCost;//金额 number
	
	private String dateSt;//入库日期  date

	private String dateValidReg;//药品效期

	private String  receiptNo; //发票号

	private String price; //零售价

	private String batchNo;//批号

	public String getDateValidReg() {
		return dateValidReg;
	}

	public void setDateValidReg(String dateValidReg) {
		this.dateValidReg = dateValidReg;
	}

	public String getReceiptNo() {
		return receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(String priceCost) {
		this.priceCost = priceCost;
	}

	public String getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(String quanPack) {
		this.quanPack = quanPack;
	}

	public String getPkUnitPack() {
		return pkUnitPack;
	}

	public void setPkUnitPack(String pkUnitPack) {
		this.pkUnitPack = pkUnitPack;
	}

	public String getAmountCost() {
		return amountCost;
	}

	public void setAmountCost(String amountCost) {
		this.amountCost = amountCost;
	}

	public String getDateSt() {
		return dateSt;
	}

	public void setDateSt(String dateSt) {
		this.dateSt = dateSt;
	}

	
}
