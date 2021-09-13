package com.zebone.nhis.common.module.emr.rec.rec;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="emr_home_page_diags_icd")
public class EmrHomePageDiagsIcd extends BaseModule{

	@PK
	@Field(value="pk_pagediag_icd",id=KeyId.UUID)
	private String pkPagediagIcd;//主键
	
	@Field(value="pk_page")
	private String pkPage;//病案首页ID
	
	@Field(value="pk_pv")
	private String pkPv;//就诊主键
	
	@Field(value="seq_no")
	private String seqNo;//序号
	
	@Field(value="seq_no_sub")
	private String seqNoSub;//序号

	@Field(value="dt_diag_type")
	private String dtDiagType;//诊断类别
	
	@Field(value="pk_diag")
	private String pkDiag;//诊断
	
	@Field(value="diag_code")
	private String diagCode;//诊断编码
	
	@Field(value="diag_name")
	private String diagName;//诊断名称
	
	@Field(value="diag_desc")
	private String diagDesc;//诊断描述
	
	@Field(value="admit_cond_code")
	private String admitCondCode;//入院病情
	
	@Field(value="admit_cond_name")
	private String admitCondName;//入院病情名称
	
	@Field(value="flag_primary")
	private String flagPrimary;//是否主诊断
	
	@Field(value="diag_code_icd")
	private String diagCodeIcd;//病案诊断编码
	
	@Field(value="diag_name_icd")
	private String diagNameIcd;//病案诊断名称
	
	@Field(value="remark_icd")
	private String remarkIcd;//病案诊断备注

	@Field(value="pk_parent")
	private String pkParent;//父手术主键
	
	@Field(value="flag_sub")
	private String flagSub;//附属诊断标志
	
	@Field(value="diag_code_si")
	private String diagCodeSi;
	
	@Field(value="diag_name_si")
	private String diagNameSi;
	public String getPkParent() {
		return pkParent;
	}

	public void setPkParent(String pkParent) {
		this.pkParent = pkParent;
	}

	public String getFlagSub() {
		return flagSub;
	}

	public void setFlagSub(String flagSub) {
		this.flagSub = flagSub;
	}

	public String getPkPagediagIcd() {
		return pkPagediagIcd;
	}

	public void setPkPagediagIcd(String pkPagediagIcd) {
		this.pkPagediagIcd = pkPagediagIcd;
	}

	public String getPkPage() {
		return pkPage;
	}

	public void setPkPage(String pkPage) {
		this.pkPage = pkPage;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}

	public String getDtDiagType() {
		return dtDiagType;
	}

	public void setDtDiagType(String dtDiagType) {
		this.dtDiagType = dtDiagType;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getDiagCode() {
		return diagCode;
	}

	public void setDiagCode(String diagCode) {
		this.diagCode = diagCode;
	}

	public String getDiagName() {
		return diagName;
	}

	public void setDiagName(String diagName) {
		this.diagName = diagName;
	}

	public String getDiagDesc() {
		return diagDesc;
	}

	public void setDiagDesc(String diagDesc) {
		this.diagDesc = diagDesc;
	}

	public String getAdmitCondCode() {
		return admitCondCode;
	}

	public void setAdmitCondCode(String admitCondCode) {
		this.admitCondCode = admitCondCode;
	}

	public String getAdmitCondName() {
		return admitCondName;
	}

	public void setAdmitCondName(String admitCondName) {
		this.admitCondName = admitCondName;
	}

	public String getFlagPrimary() {
		return flagPrimary;
	}

	public void setFlagPrimary(String flagPrimary) {
		this.flagPrimary = flagPrimary;
	}

	public String getDiagCodeIcd() {
		return diagCodeIcd;
	}

	public void setDiagCodeIcd(String diagCodeIcd) {
		this.diagCodeIcd = diagCodeIcd;
	}

	public String getDiagNameIcd() {
		return diagNameIcd;
	}

	public void setDiagNameIcd(String diagNameIcd) {
		this.diagNameIcd = diagNameIcd;
	}

	public String getRemarkIcd() {
		return remarkIcd;
	}

	public void setRemarkIcd(String remarkIcd) {
		this.remarkIcd = remarkIcd;
	}

	public String getSeqNoSub() {
		return seqNoSub;
	}

	public void setSeqNoSub(String seqNoSub) {
		this.seqNoSub = seqNoSub;
	}

	public String getDiagCodeSi() {
		return diagCodeSi;
	}

	public void setDiagCodeSi(String diagCodeSi) {
		this.diagCodeSi = diagCodeSi;
	}

	public String getDiagNameSi() {
		return diagNameSi;
	}

	public void setDiagNameSi(String diagNameSi) {
		this.diagNameSi = diagNameSi;
	}
	
}
