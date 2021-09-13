package com.zebone.nhis.scm.material.vo;

import java.util.Date;

public class BdPdBillDtlsVo {
	
	//日期
	private Date dateDtl;
	
	//单据号
	private String codeSt;    
	
	//摘要名称
	private String sttypeName;
	
	//收入数量
	private Long srQuan;
	
	//收入金额
	private Double srAmt;
	
	//支出数量
	private Long zcQuan;
	
	
	//支出金额
	private Double zcAmt;
	
	//结存数量
	private Long jcQuan;
	
	//结存金额
	private Double jcAmt;

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

	public String getSttypeName() {
		return sttypeName;
	}

	public void setSttypeName(String sttypeName) {
		this.sttypeName = sttypeName;
	}

	public Long getSrQuan() {
		return srQuan;
	}

	public void setSrQuan(Long srQuan) {
		this.srQuan = srQuan;
	}

	public Double getSrAmt() {
		return srAmt;
	}

	public void setSrAmt(Double srAmt) {
		this.srAmt = srAmt;
	}

	public Long getZcQuan() {
		return zcQuan;
	}

	public void setZcQuan(Long zcQuan) {
		this.zcQuan = zcQuan;
	}

	public Double getZcAmt() {
		return zcAmt;
	}

	public void setZcAmt(Double zcAmt) {
		this.zcAmt = zcAmt;
	}

	public Long getJcQuan() {
		return jcQuan;
	}

	public void setJcQuan(Long jcQuan) {
		this.jcQuan = jcQuan;
	}

	public Double getJcAmt() {
		return jcAmt;
	}

	public void setJcAmt(Double jcAmt) {
		this.jcAmt = jcAmt;
	}
	
	
	
}
