package com.zebone.nhis.bl.pub.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public  class NssPatientVo {
	
    //查询时间
	@DateTimeFormat(pattern="yyyyMMddHHmmss")
	private Date dateSt;
	
	//结算主键
	private String pkSettle;
	
	//所属机构
	private String pkOrg;
	
	//患者主键
	private String pkPi;
	
	//就诊主键
	private String pkPv;
	

	public Date getDateSt() {
		return dateSt;
	}

	public void setDateSt(Date dateSt) {
		this.dateSt = dateSt;
	}

	public String getPkSettle() {
		return pkSettle;
	}

	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}


}
