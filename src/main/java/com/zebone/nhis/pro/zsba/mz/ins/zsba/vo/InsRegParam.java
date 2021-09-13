package com.zebone.nhis.pro.zsba.mz.ins.zsba.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.platform.modules.dao.build.au.Field;

public class InsRegParam {

	@JSONField(name="psn_no")
	@Field(value="psn_no")
	private String psnNo ;
	
	@JSONField(name="insutype")
	@Field(value="insutype")
	private String insutype ;
	
	@JSONField(name="begntime")
	@Field(value="begntime")
	private String begntime ;
	
	@JSONField(name="mdtrt_cert_type")
	@Field(value="mdtrt_cert_type")
	private String mdtrtCertType ;
	
	//@JSONField(name="mdtrt_cert_no")
	@Field(value="mdtrt_cert_no")
	private String mdtrtCertNo ;
	
	@JSONField(name="ipt_otp_no")
	@Field(value="ipt_otp_no")
	private String iptOtpNo ;
	
	@JSONField(name="atddr_no")
	@Field(value="atddr_no")
	private String atddrNo ;
	
	@JSONField(name="dr_name")
	@Field(value="dr_name")
	private String drName ;
	
	@JSONField(name="dept_code")
	@Field(value="dept_code")
	private String deptCode ;
	
	@JSONField(name="dept_name")
	@Field(value="dept_name")
	private String deptName ;
	
	@Field(value="caty")
	@JSONField(name="caty")
	private String caty ;
	
	private String pkPv;

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPsnNo() {
		return psnNo;
	}

	public void setPsnNo(String psnNo) {
		this.psnNo = psnNo;
	}

	public String getInsutype() {
		return insutype;
	}

	public void setInsutype(String insutype) {
		this.insutype = insutype;
	}

	public String getBegntime() {
		return begntime;
	}

	public void setBegntime(String begntime) {
		this.begntime = begntime;
	}

	public String getMdtrtCertType() {
		return mdtrtCertType;
	}

	public void setMdtrtCertType(String mdtrtCertType) {
		this.mdtrtCertType = mdtrtCertType;
	}

	public String getMdtrtCertNo() {
		return mdtrtCertNo;
	}

	public void setMdtrtCertNo(String mdtrtCertNo) {
		this.mdtrtCertNo = mdtrtCertNo;
	}

	public String getIptOtpNo() {
		return iptOtpNo;
	}

	public void setIptOtpNo(String iptOtpNo) {
		this.iptOtpNo = iptOtpNo;
	}

	public String getAtddrNo() {
		return atddrNo;
	}

	public void setAtddrNo(String atddrNo) {
		this.atddrNo = atddrNo;
	}

	public String getDrName() {
		return drName;
	}

	public void setDrName(String drName) {
		this.drName = drName;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getCaty() {
		return caty;
	}

	public void setCaty(String caty) {
		this.caty = caty;
	}
	
	
}
