package com.zebone.nhis.webservice.vo.depositevo;

import javax.xml.bind.annotation.XmlElement;

public class ResDepositeVo {
	
	private String euDptype;
	
	private String euDirect;
	
	private String euPvtype;
	
	private String dtPaymode;
	
	private String namePaymode;
	
	private String dtDank;
	
	private String nameBank;
	
	private String bankNo;
	
	private String payInfo;
	
	private String datePay;
	
	private String pkDept;
	
	private String nameDept;
	
	private String pkEmpPay;
	
	private String nameEmpPay;
	
	private String pkAcc;
	
	private String flagAcc;
	
	private String flagSettle;
	
	private String pkSettle;
	
	private String amount;
	
	private String reptNo;
	
	private String tradeNo;
	
	private String serialNo;
	
	private String sysname;
	
	private String pkDepoBack;
	
	
	
	@XmlElement(name = "euDptype")
	public String getEuDptype() {
		return euDptype;
	}

	public void setEuDptype(String euDptype) {
		this.euDptype = euDptype;
	}
	
	@XmlElement(name = "euDirect")
	public String getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}
	
	@XmlElement(name = "euPvtype")
	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	@XmlElement(name = "dtPaymode")
	public String getDtPaymode() {
		return dtPaymode;
	}

	public void setDtPaymode(String dtPaymode) {
		this.dtPaymode = dtPaymode;
	}
    
	@XmlElement(name = "namePaymode")
	public String getNamePaymode() {
		return namePaymode;
	}

	public void setNamePaymode(String namePaymode) {
		this.namePaymode = namePaymode;
	}
	
	@XmlElement(name = "dtDank")
	public String getDtDank() {
		return dtDank;
	}

	public void setDtDank(String dtDank) {
		this.dtDank = dtDank;
	}

	@XmlElement(name = "nameBank")
	public String getNameBank() {
		return nameBank;
	}

	public void setNameBank(String nameBank) {
		this.nameBank = nameBank;
	}
	
	@XmlElement(name = "bankNo")
	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	
	@XmlElement(name = "payInfo")
	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}
    
	@XmlElement(name = "datePay")
	public String getDatePay() {
		return datePay;
	}

	public void setDatePay(String datePay) {
		this.datePay = datePay;
	}
	
	@XmlElement(name = "pkDept")
	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
    
	@XmlElement(name = "nameDept")
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
    
	@XmlElement(name = "pkEmpPay")
	public String getPkEmpPay() {
		return pkEmpPay;
	}

	public void setPkEmpPay(String pkEmpPay) {
		this.pkEmpPay = pkEmpPay;
	}
    
	@XmlElement(name = "nameEmpPay")
	public String getNameEmpPay() {
		return nameEmpPay;
	}

	public void setNameEmpPay(String nameEmpPay) {
		this.nameEmpPay = nameEmpPay;
	}
	
	@XmlElement(name = "pkAcc")
	public String getPkAcc() {
		return pkAcc;
	}

	public void setPkAcc(String pkAcc) {
		this.pkAcc = pkAcc;
	}

	@XmlElement(name = "flagAcc")
	public String getFlagAcc() {
		return flagAcc;
	}

	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}
	
	@XmlElement(name = "flagSettle")
	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	@XmlElement(name = "pkSettle")
	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
    
	@XmlElement(name = "amount")
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
    
	@XmlElement(name = "reptNo")
	public String getReptNo() {
		return reptNo;
	}

	public void setReptNo(String reptNo) {
		this.reptNo = reptNo;
	}
    
	@XmlElement(name = "tradeNo")
	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	@XmlElement(name = "serialNo")
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
    
	@XmlElement(name = "sysname")
	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	@XmlElement(name = "pkDepoBack")
	public String getPkDepoBack() {
		return pkDepoBack;
	}

	public void setPkDepoBack(String pkDepoBack) {
		this.pkDepoBack = pkDepoBack;
	}


	
	
}
