package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdQuyBillDtlsVo {
	
	//日期
	private Date dateDtl;    
    
	//单据号
	private String codeSt;     
    
	//摘要
	private String dtSttype; 
	
	//摘要名称
	private String sttypeName;
	
	//1收入，-1支出(出入库)
	private String euDirect;
	
	//调价单号
	private String codeRep;
	
	//调价类型
	private String dtReptype;
	
	//1收入，-1支出(调价)
	private String tjEuDirect;
	
	//初期数量
	private Long cqQuan;
	
	//初期零售金额
	private Double cqAmount;
	
	//初期成本金额
	private Double cqAmountCost;
	
	//收支数量
	private Long stQuan;
	
	//收支零售金额
	private Double stAmount;
	
	//收支成本金额
	private Double stAmountCost;
	
	//调价金额
	private Double tjAmt;
	
	//调价类型名称
	private String reptypeName;
	
	//调价数量
	private Long tjQuan;
	
	public Long getTjQuan() {
		return tjQuan;
	}

	public void setTjQuan(Long tjQuan) {
		this.tjQuan = tjQuan;
	}

	public String getReptypeName() {
		return reptypeName;
	}

	public void setReptypeName(String reptypeName) {
		this.reptypeName = reptypeName;
	}

	public Date getDateDtl() {
		return dateDtl;
	}

	public void setDateDtl(Date dateDtl) {
		this.dateDtl = dateDtl;
	}

	public String getCodeSt() {
		return codeSt;
	}

	public void setCodeSt(String codeSt) {
		this.codeSt = codeSt;
	}

	public String getDtSttype() {
		return dtSttype;
	}

	public void setDtSttype(String dtSttype) {
		this.dtSttype = dtSttype;
	}

	public String getSttypeName() {
		return sttypeName;
	}

	public void setSttypeName(String sttypeName) {
		this.sttypeName = sttypeName;
	}

	public String getEuDirect() {
		return euDirect;
	}

	public void setEuDirect(String euDirect) {
		this.euDirect = euDirect;
	}

	public String getCodeRep() {
		return codeRep;
	}

	public void setCodeRep(String codeRep) {
		this.codeRep = codeRep;
	}

	public String getDtReptype() {
		return dtReptype;
	}

	public void setDtReptype(String dtReptype) {
		this.dtReptype = dtReptype;
	}

	public String getTjEuDirect() {
		return tjEuDirect;
	}

	public void setTjEuDirect(String tjEuDirect) {
		this.tjEuDirect = tjEuDirect;
	}

	public Long getCqQuan() {
		return cqQuan;
	}

	public void setCqQuan(Long cqQuan) {
		this.cqQuan = cqQuan;
	}

	public Double getCqAmount() {
		return cqAmount;
	}

	public void setCqAmount(Double cqAmount) {
		this.cqAmount = cqAmount;
	}

	public Double getCqAmountCost() {
		return cqAmountCost;
	}

	public void setCqAmountCost(Double cqAmountCost) {
		this.cqAmountCost = cqAmountCost;
	}

	public Long getStQuan() {
		return stQuan;
	}

	public void setStQuan(Long stQuan) {
		this.stQuan = stQuan;
	}

	public Double getStAmount() {
		return stAmount;
	}

	public void setStAmount(Double stAmount) {
		this.stAmount = stAmount;
	}

	public Double getStAmountCost() {
		return stAmountCost;
	}

	public void setStAmountCost(Double stAmountCost) {
		this.stAmountCost = stAmountCost;
	}

	public Double getTjAmt() {
		return tjAmt;
	}

	public void setTjAmt(Double tjAmt) {
		this.tjAmt = tjAmt;
	}
	
}
