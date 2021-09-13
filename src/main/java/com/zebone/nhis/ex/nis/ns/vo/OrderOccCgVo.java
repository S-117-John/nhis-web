package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;

public class OrderOccCgVo extends OrderOccVo {
	
    private String pkDeptDe;
   
    private String pkCgip ;
    
    private String pkOrdexdt;//发药明细主键
    
    private String pkPres;

    private String flagBase;

    private String flagMedout;

    private String flagSelf;

    private String flagEmer;
    
    private String pkPd;
    
    private String batchNo;
    
    private Date dateExpire; 
    
    private int packSize;
    
    private double priceCost;
    
    private double price;
    
    private double quan;
    

	public double getQuan() {
		return quan;
	}

	public void setQuan(double quan) {
		this.quan = quan;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getPkPres() {
		return pkPres;
	}

	public void setPkPres(String pkPres) {
		this.pkPres = pkPres;
	}

	public String getFlagBase() {
		return flagBase;
	}

	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}

	public String getFlagMedout() {
		return flagMedout;
	}

	public void setFlagMedout(String flagMedout) {
		this.flagMedout = flagMedout;
	}

	public String getFlagSelf() {
		return flagSelf;
	}

	public void setFlagSelf(String flagSelf) {
		this.flagSelf = flagSelf;
	}

	public String getFlagEmer() {
		return flagEmer;
	}

	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public Date getDateExpire() {
		return dateExpire;
	}

	public void setDateExpire(Date dateExpire) {
		this.dateExpire = dateExpire;
	}

	public int getPackSize() {
		return packSize;
	}

	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}

	public double getPriceCost() {
		return priceCost;
	}

	public void setPriceCost(double priceCost) {
		this.priceCost = priceCost;
	}

	public String getPkDeptDe() {
		return pkDeptDe;
	}

	public void setPkDeptDe(String pkDeptDe) {
		this.pkDeptDe = pkDeptDe;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

	public String getPkOrdexdt() {
		return pkOrdexdt;
	}

	public void setPkOrdexdt(String pkOrdexdt) {
		this.pkOrdexdt = pkOrdexdt;
	}
    
    
}
