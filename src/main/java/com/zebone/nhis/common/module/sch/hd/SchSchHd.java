package com.zebone.nhis.common.module.sch.hd;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: SCH_SCH_HD  - 排班预约-透析排班
 *
 * @since 2018-11-27 10:12:41
 */
@Table(value="SCH_SCH_HD")
public class SchSchHd extends BaseModule{
	
	/** PK_FAMILY - 主键 */
	@PK
	@Field(value="PK_SCHHD",id=KeyId.UUID)
	private String pkSchhd;
	
	/** 透析床位 **/
	@Field(value="PK_HDBED")
	private String pkHdbed;
	
	/** 透析日期 **/
	@Field(value="DATE_HD")
	private Date dateHd;
	
	/** 班次 **/
	@Field(value="PK_DATESLOT")
	private String pkDateslot;
	
	/** 患者 **/
	@Field(value="PK_PI")
	private String pkPi;
	
	/** 排班日期 **/
	@Field(value="DATE_OPT")
	private Date dateOpt;
	
	/** 排班科室 **/
	@Field(value="PK_DEPT")
	private String pkDept;
	
	/** 操作员 **/
	@Field(value="PK_EMP_OPT")
	private String pkEmpOpt;
	
	/** 操作员姓名 **/
	@Field(value="NAME_EMP_OPT")
	private String nameEmpOpt;
	
	/** 取消标志 **/
	@Field(value="FLAG_CANC")
	private String flagCanc;
	
	/** 取消日期 **/
	@Field(value="DATE_CANC")
	private Date dateCanc;
	
	/** 取消人 **/
	@Field(value="PK_EMP_CANC")
	private String pkEmpCanc;
	
	/** 取消人姓名 **/
	@Field(value="NAME_EMP_CANC")
	private String nameEmpCanc;
	
	/** 就诊确认标志 **/
	@Field(value="FLAG_CONFIRM")
	private String flagConfirm;
	
	/** 就诊号 **/
	@Field(value="PK_PV")
	private String pkPv;
	
	/** 备注 **/
	@Field(value="NOTE")
	private String note;

	public String getPkSchhd() {
		return pkSchhd;
	}

	public void setPkSchhd(String pkSchhd) {
		this.pkSchhd = pkSchhd;
	}

	public String getPkHdbed() {
		return pkHdbed;
	}

	public void setPkHdbed(String pkHdbed) {
		this.pkHdbed = pkHdbed;
	}

	public Date getDateHd() {
		return dateHd;
	}

	public void setDateHd(Date dateHd) {
		this.dateHd = dateHd;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public Date getDateOpt() {
		return dateOpt;
	}

	public void setDateOpt(Date dateOpt) {
		this.dateOpt = dateOpt;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkEmpOpt() {
		return pkEmpOpt;
	}

	public void setPkEmpOpt(String pkEmpOpt) {
		this.pkEmpOpt = pkEmpOpt;
	}

	public String getNameEmpOpt() {
		return nameEmpOpt;
	}

	public void setNameEmpOpt(String nameEmpOpt) {
		this.nameEmpOpt = nameEmpOpt;
	}

	public String getFlagCanc() {
		return flagCanc;
	}

	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}

	public Date getDateCanc() {
		return dateCanc;
	}

	public void setDateCanc(Date dateCanc) {
		this.dateCanc = dateCanc;
	}

	public String getPkEmpCanc() {
		return pkEmpCanc;
	}

	public void setPkEmpCanc(String pkEmpCanc) {
		this.pkEmpCanc = pkEmpCanc;
	}

	public String getNameEmpCanc() {
		return nameEmpCanc;
	}

	public void setNameEmpCanc(String nameEmpCanc) {
		this.nameEmpCanc = nameEmpCanc;
	}

	public String getFlagConfirm() {
		return flagConfirm;
	}

	public void setFlagConfirm(String flagConfirm) {
		this.flagConfirm = flagConfirm;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	
}
