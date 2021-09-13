package com.zebone.nhis.webservice.syx.vo.platForm;

import java.math.BigDecimal;
import java.util.Date;

public class InvInfoVo {

	private int page;

	private BigDecimal amount;
	
	private BigDecimal amountPi;

	public BigDecimal getAmountPi() {
		return amountPi;
	}
	public void setAmountPi(BigDecimal amountPi) {
		this.amountPi = amountPi;
	}
	private String pkItemCate;
	
	private String splitWay;
	
	private BillInfo inv;

	private String code;
	private String name;

	private String pkDept;

	private String nameDept;

	private String pkInvcateitem;
	
	private Date dateSplitBegin;
	
	private Date dateSplitEnd;
	
	private String note;
	
	private String nameMachine;
	
	public String getNameMachine() {
		return nameMachine;
	}
	public void setNameMachine(String nameMachine) {
		this.nameMachine = nameMachine;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Date getDateSplitBegin() {
		return dateSplitBegin;
	}
	public void setDateSplitBegin(Date dateSplitBegin) {
		this.dateSplitBegin = dateSplitBegin;
	}
	public Date getDateSplitEnd() {
		return dateSplitEnd;
	}
	public void setDateSplitEnd(Date dateSplitEnd) {
		this.dateSplitEnd = dateSplitEnd;
	}
	public BillInfo getInv() {
		return inv;
	}
	public void setInv(BillInfo inv) {
		this.inv = inv;
	}
	

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
//	public double getRate() {
//		return rate;
//	}
//	public void setRate(double rate) {
//		this.rate = rate;
//	}
	public String getSplitWay() {
		return splitWay;
	}
	public void setSplitWay(String splitWay) {
		this.splitWay = splitWay;
	}

	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getPkItemCate() {
		return pkItemCate;
	}
	public void setPkItemCate(String pkItemCate) {
		this.pkItemCate = pkItemCate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkInvcateitem() {
		return pkInvcateitem;
	}
	public void setPkInvcateitem(String pkInvcateitem) {
		this.pkInvcateitem = pkInvcateitem;
	}


}
