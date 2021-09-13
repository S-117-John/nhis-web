package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

public class CnPresDt {
	
	private Double sortNo;
	
	private String pkPd;
	
	private String name;
	
	private Double quan;
	
	private Double price;
	
	private String pkUnit;
	
	private String nameUnit;
	
	private String noteUse;
	
	private String flagAcc;
	
	private Date Ts;
	
	private String tstr;
	
	private String flagSettle;

	
	

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getTstr() {
		return tstr;
	}

	public void setTstr(String tstr) {
		this.tstr = tstr;
	}

	public Double getSortNo() {
		return sortNo;
	}

	public void setSortNo(Double sortNo) {
		this.sortNo = sortNo;
	}

	public String getPkPd() {
		return pkPd;
	}

	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getQuan() {
		return quan;
	}

	public void setQuan(Double quan) {
		this.quan = quan;
	}

	public String getPkUnit() {
		return pkUnit;
	}

	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}

	public String getNameUnit() {
		return nameUnit;
	}

	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}

	public String getNoteUse() {
		return noteUse;
	}

	public void setNoteUse(String noteUse) {
		this.noteUse = noteUse;
	}

	public String getFlagAcc() {
		return flagAcc;
	}

	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}

	public Date getTs() {
		return Ts;
	}

	public void setTs(Date ts) {
		Ts = ts;
	}

	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}



}
