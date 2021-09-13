package com.zebone.nhis.ex.pub.vo;

import java.util.Date;

public class PdPriceVo {
	private String batchNo;
	private Integer packSize;
	private Double price;
	private Double priceCost;
	private Date dateExpire;
	private String pkPd;

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Integer getPackSize() {
		return packSize;
	}

	public void setPackSize(Integer packSize) {
		this.packSize = packSize;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(Double priceCost) {
		this.priceCost = priceCost;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
    
}
