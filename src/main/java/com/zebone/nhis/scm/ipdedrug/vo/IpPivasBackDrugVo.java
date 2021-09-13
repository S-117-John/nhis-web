package com.zebone.nhis.scm.ipdedrug.vo;

import java.util.Date;

public class IpPivasBackDrugVo {
	private String pkPdapdt;
	
	private String pkCnord;
	
	private double amount;
	
	private double price;
	
	private double quanPack;
	
	private double quanMin;
	
	private int packSize;
	
	private String pkExocc;
	
	private Date ts;
	
	private String pkPdap;
	
	private String codeOrdtype;

	private String noteBack;

	public String getNoteBack() {
		return noteBack;
	}

	public void setNoteBack(String noteBack) {
		this.noteBack = noteBack;
	}

	public String getPkPdapdt() {
		return pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public double getQuanPack() {
		return quanPack;
	}

	public void setQuanPack(double quanPack) {
		this.quanPack = quanPack;
	}

	public double getQuanMin() {
		return quanMin;
	}

	public void setQuanMin(double quanMin) {
		this.quanMin = quanMin;
	}

	public int getPackSize() {
		return packSize;
	}

	public void setPackSize(int packSize) {
		this.packSize = packSize;
	}

	public String getPkExocc() {
		return pkExocc;
	}

	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getPkPdap() {
		return pkPdap;
	}

	public void setPkPdap(String pkPdap) {
		this.pkPdap = pkPdap;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
}
