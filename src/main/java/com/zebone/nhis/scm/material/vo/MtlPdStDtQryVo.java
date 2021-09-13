package com.zebone.nhis.scm.material.vo;

import java.util.Date;


public class MtlPdStDtQryVo{
	
	private String receiptNo;
	private String pkSupplyer;
	private Date dateChkRpt;
	private String nameEmpChkRpt;
	private String supplyerName;
	
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getPkSupplyer() {
		return pkSupplyer;
	}
	public void setPkSupplyer(String pkSupplyer) {
		this.pkSupplyer = pkSupplyer;
	}
	public Date getDateChkRpt() {
		return dateChkRpt;
	}
	public void setDateChkRpt(Date dateChkRpt) {
		this.dateChkRpt = dateChkRpt;
	}
	
	public String getSupplyerName() {
		return supplyerName;
	}
	public void setSupplyerName(String supplyerName) {
		this.supplyerName = supplyerName;
	}
	public String getNameEmpChkRpt() {
		return nameEmpChkRpt;
	}
	public void setNameEmpChkRpt(String nameEmpChkRpt) {
		this.nameEmpChkRpt = nameEmpChkRpt;
	}
	
	
}
