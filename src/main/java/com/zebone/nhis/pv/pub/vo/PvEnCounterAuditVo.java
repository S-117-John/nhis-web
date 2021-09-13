package com.zebone.nhis.pv.pub.vo;

import java.util.Date;

public class PvEnCounterAuditVo {

	/** 患者主键 */
	private String pkPi;
	/** 姓名 */
	private String namePi;

	/** 就诊主键 */
	private String pkPv;

	/** 住院号 */
	private String codeIp;

	/** 床号 */
	private String bedNo;

	/** 科室 */
	private String nameDept;
	/** 科室主键 */
	private String pkDept;

	/** 病区 */
	private String nameDeptNs;
	/** 病区主键 */
	private String pkDeptNs;

	/** 状态 */
	public String euStatus;
	public String flagCheckIn;
	public String flagCheckInStatus;
	/** 医保计划 */
	private String nameHp;
	/** 医保计划主键 */
	private String pkInsu;
	/** 审核人主键 */
	private String pkEmpChk;
	/** 审核人 */
	private String nameEmpChk;
	/** 审核时间 */
	private Date dateChk;

	private String dataAttachment;
	/** 入院日期 */
	public Date dateBegin;

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getNamePi() {
		return namePi;
	}

	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getBedNo() {
		return bedNo;
	}

	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getNameDeptNs() {
		return nameDeptNs;
	}

	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getNameHp() {
		return nameHp;
	}

	public void setNameHp(String nameHp) {
		this.nameHp = nameHp;
	}

	public String getPkInsu() {
		return pkInsu;
	}

	public void setPkInsu(String pkInsu) {
		this.pkInsu = pkInsu;
	}

	public String getPkEmpChk() {
		return pkEmpChk;
	}

	public void setPkEmpChk(String pkEmpChk) {
		this.pkEmpChk = pkEmpChk;
	}

	public String getNameEmpChk() {
		return nameEmpChk;
	}

	public void setNameEmpChk(String nameEmpChk) {
		this.nameEmpChk = nameEmpChk;
	}

	public Date getDateChk() {
		return dateChk;
	}

	public void setDateChk(Date dateChk) {
		this.dateChk = dateChk;
	}

	public String getFlagCheckIn() {
		return flagCheckIn;
	}

	public void setFlagCheckIn(String flagCheckIn) {
		this.flagCheckIn = flagCheckIn;
	}

	public String getFlagCheckInStatus() {
		return flagCheckInStatus;
	}

	public void setFlagCheckInStatus(String flagCheckInStatus) {
		this.flagCheckInStatus = flagCheckInStatus;
	}

	public String getDataAttachment() {
		return dataAttachment;
	}

	public void setDataAttachment(String dataAttachment) {
		this.dataAttachment = dataAttachment;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

}
