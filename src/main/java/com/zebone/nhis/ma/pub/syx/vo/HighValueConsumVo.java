package com.zebone.nhis.ma.pub.syx.vo;


public class HighValueConsumVo {
	/*
	 * 耗材类型
	 */
	private String dtItemtype;
	
	/*
	 * 高值耗材项目编码
	 */
	private String itemCode;
	
	/*
	 * 老系统项目编码
	 */
	private String oldId;
	
	/*
	 * 中间库库存
	 */
	private double quantity;
	
	/*
	 * 材料编码
	 */
	private String barcode;
	
	/*
	 * 科室编码
	 */
	private String deptCode;
	
	/**
	 * 执行科室
	 */
	private String pkDeptEx;
	
	/**
	 * 需要数量
	 */
	private double quanCg;
	
	private String name;
	
	private String pkItem;

	public String getPkItem() {
		return pkItem;
	}

	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}

	public String getDtItemtype() {
		return dtItemtype;
	}

	public void setDtItemtype(String dtItemtype) {
		this.dtItemtype = dtItemtype;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getOldId() {
		return oldId;
	}

	public void setOldId(String oldId) {
		this.oldId = oldId;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public double getQuanCg() {
		return quanCg;
	}

	public void setQuanCg(double quanCg) {
		this.quanCg = quanCg;
	}

	public String getPkDeptEx() {
		return pkDeptEx;
	}

	public void setPkDeptEx(String pkDeptEx) {
		this.pkDeptEx = pkDeptEx;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
