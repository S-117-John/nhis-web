package com.zebone.nhis.pro.zsba.ex.vo;

import java.util.Date;

public class MedAppBaVo {
	private String pkPv;
	private String pkCnord;
	private String codeApply;
	private String bedNo;
	private String namePi;
	private String codeIp;
	private String apptype;
	private String nameOrd;
	private Date dateEnter;
	private Date dateStart;
	private Date datePlan;
	private Date dateOcc;
	private String nameDept;
	private String nameDeptEx;
	private String euStatusOrd;
	private String euStatusApp;
	private String codeApptype;
	private String pkOrdop;
	private String pkOp;
	private String descOp;
	private String ordsn;
	private String ordsnParent;
	private String formApp;
	private String noteOrd;
	private String flagCanc;
	private String euStatus;
	private String euAlways;
	private String pkExocc;
	private String pkEmpCanc;
	private String nameEmpCanc;
	private String flagDurg;
	private String flagBase;
	private String flagBl;
	private String flagAutoCg;//自动计费
	private String pkOrd;//医嘱码表主键
	private String pkDeptExec;//执行科室
	private String flagPrint2;//pacs系统：接收标志 0-未接收，1-pacs接收，2-接收后取消提交，3-pacs删除接收
	private String dateExam;//检查预约系统：预约时间
	private String sampNo;//lis系统：条形码
	private String nameDeptNs;//开立病区名称
	private String dtDepttype;//开立科室 - 科室类型
	private String codeRpt;//报告号
	
	public String getEuStatusOrd() {
		return euStatusOrd;
	}
	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}
	public String getDtDepttype() {
		return dtDepttype;
	}
	public void setDtDepttype(String dtDepttype) {
		this.dtDepttype = dtDepttype;
	}
	public String getCodeRpt() {
		return codeRpt;
	}
	public void setCodeRpt(String codeRpt) {
		this.codeRpt = codeRpt;
	}
	public String getDtDeptType() {
		return dtDepttype;
	}
	public void setDtDeptType(String dtDepttype) {
		this.dtDepttype = dtDepttype;
	}
	public String getNameDeptNs() {
		return nameDeptNs;
	}
	public void setNameDeptNs(String nameDeptNs) {
		this.nameDeptNs = nameDeptNs;
	}
	public String getDateExam() {
		return dateExam;
	}
	public void setDateExam(String dateExam) {
		this.dateExam = dateExam;
	}
	public String getSampNo() {
		return sampNo;
	}
	public void setSampNo(String sampNo) {
		this.sampNo = sampNo;
	}
	public String getFlagPrint2() {
		return flagPrint2;
	}
	public void setFlagPrint2(String flagPrint2) {
		this.flagPrint2 = flagPrint2;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getApptype() {
		return apptype;
	}
	public void setApptype(String apptype) {
		this.apptype = apptype;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public Date getDateEnter() {
		return dateEnter;
	}
	public void setDateEnter(Date dateEnter) {
		this.dateEnter = dateEnter;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Date getDatePlan() {
		return datePlan;
	}
	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}
	public Date getDateOcc() {
		return dateOcc;
	}
	public void setDateOcc(Date dateOcc) {
		this.dateOcc = dateOcc;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getNameDeptEx() {
		return nameDeptEx;
	}
	public void setNameDeptEx(String nameDeptEx) {
		this.nameDeptEx = nameDeptEx;
	}
	public String getEuStatusApp() {
		return euStatusApp;
	}
	public void setEuStatusApp(String euStatusApp) {
		this.euStatusApp = euStatusApp;
	}
	public String getCodeApptype() {
		return codeApptype;
	}
	public void setCodeApptype(String codeApptype) {
		this.codeApptype = codeApptype;
	}
	public String getPkOrdop() {
		return pkOrdop;
	}
	public void setPkOrdop(String pkOrdop) {
		this.pkOrdop = pkOrdop;
	}
	public String getPkOp() {
		return pkOp;
	}
	public void setPkOp(String pkOp) {
		this.pkOp = pkOp;
	}
	public String getDescOp() {
		return descOp;
	}
	public void setDescOp(String descOp) {
		this.descOp = descOp;
	}
	public String getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(String ordsn) {
		this.ordsn = ordsn;
	}
	public String getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(String ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public String getFormApp() {
		return formApp;
	}
	public void setFormApp(String formApp) {
		this.formApp = formApp;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getFlagCanc() {
		return flagCanc;
	}
	public void setFlagCanc(String flagCanc) {
		this.flagCanc = flagCanc;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getEuAlways() {
		return euAlways;
	}
	public void setEuAlways(String euAlways) {
		this.euAlways = euAlways;
	}
	public String getPkExocc() {
		return pkExocc;
	}
	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
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
	public String getFlagDurg() {
		return flagDurg;
	}
	public void setFlagDurg(String flagDurg) {
		this.flagDurg = flagDurg;
	}
	public String getFlagBase() {
		return flagBase;
	}
	public void setFlagBase(String flagBase) {
		this.flagBase = flagBase;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
	public String getFlagAutoCg() {
		return flagAutoCg;
	}
	public void setFlagAutoCg(String flagAutoCg) {
		this.flagAutoCg = flagAutoCg;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
}
