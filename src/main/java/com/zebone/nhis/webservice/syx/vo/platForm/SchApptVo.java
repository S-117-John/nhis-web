package com.zebone.nhis.webservice.syx.vo.platForm;

import java.util.Date;

public class SchApptVo {
	private String pkSchappt;
	
	private String pkPi;
	
	private String flagCancel;//取消标志
	
	private String flagPay;//支付标志
	
	private String dtApptype;//预约渠道编号
	
	private String apptypeName;//预约渠道名称
	
	private String pkOrg;
	
	private String pkSchsrv;
	
	private String pkDept;//预约科室
	
	private Date dateAppt;//就诊日期
	
	private Date dateReg;//登记日期
	
	private String euSrvtype;

	private String pkSchres;
	
	private String pkDateslot;
	
	private String pkEmp;
	
	private Long ticketNo;

	private String pkSch;
	
	private String pkPv;
	
	private String flagCancelPv;
	
	private String pkInvoice;
	
	private String pvEuStatus;

	private String pkApptpv;
	
	private String orderidExt;

	/**
	 * 医生专家标识
	 */
	private String flagSpec;

	
	public String getOrderidExt() {
		return orderidExt;
	}

	public void setOrderidExt(String orderidExt) {
		this.orderidExt = orderidExt;
	}

	public String getFlagSpec() {
		return flagSpec;
	}

	public void setFlagSpec(String flagSpec) {
		this.flagSpec = flagSpec;
	}

	public String getPkApptpv() {
		return pkApptpv;
	}

	public void setPkApptpv(String pkApptpv) {
		this.pkApptpv = pkApptpv;
	}

	public String getPvEuStatus() {
		return pvEuStatus;
	}

	public void setPvEuStatus(String pvEuStatus) {
		this.pvEuStatus = pvEuStatus;
	}

	public String getPkInvoice() {
		return pkInvoice;
	}

	public void setPkInvoice(String pkInvoice) {
		this.pkInvoice = pkInvoice;
	}

	public String getFlagCancelPv() {
		return flagCancelPv;
	}

	public void setFlagCancelPv(String flagCancelPv) {
		this.flagCancelPv = flagCancelPv;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	public Long getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(Long ticketNo) {
		this.ticketNo = ticketNo;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getPkSchres() {
		return pkSchres;
	}

	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}

	public String getEuSrvtype() {
		return euSrvtype;
	}

	public void setEuSrvtype(String euSrvtype) {
		this.euSrvtype = euSrvtype;
	}

	public Date getDateReg() {
		return dateReg;
	}

	public void setDateReg(Date dateReg) {
		this.dateReg = dateReg;
	}

	public Date getDateAppt() {
		return dateAppt;
	}

	public void setDateAppt(Date dateAppt) {
		this.dateAppt = dateAppt;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkSchsrv() {
		return pkSchsrv;
	}

	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getDtApptype() {
		return dtApptype;
	}

	public void setDtApptype(String dtApptype) {
		this.dtApptype = dtApptype;
	}

	public String getApptypeName() {
		return apptypeName;
	}

	public void setApptypeName(String apptypeName) {
		this.apptypeName = apptypeName;
	}

	public String getPkSchappt() {
		return pkSchappt;
	}

	public void setPkSchappt(String pkSchappt) {
		this.pkSchappt = pkSchappt;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getFlagCancel() {
		return flagCancel;
	}

	public void setFlagCancel(String flagCancel) {
		this.flagCancel = flagCancel;
	}

	public String getFlagPay() {
		return flagPay;
	}

	public void setFlagPay(String flagPay) {
		this.flagPay = flagPay;
	}
	
	
}
