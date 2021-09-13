package com.zebone.nhis.cn.ipdw.vo;

import java.util.Date;

public class OpApplyQryParam {

	private Date dateStartBegin;
	private Date dateStartEnd;
	private String codeApply;
	private String pkDept;
	private String pkDeptNs;
	private String euPvtype;
	private String codeIp;
	private String namePi;
	private String codePi;
	private String euStatus;
	private String flagDetial;
	private String ordDeptByOp;
	private String ordDeptNsByOp;
	private String pkOrg;
	private Date datePlanBegin;
	private Date datePlanEnd;
	private String ticketno;
	private String bedNo;
	private String isAnae;
	private String dtAnae;
	private String deptOpAndAnae;
	private String pkPv;
	private String codeOp;
	private String review; //手术是否需要审核
	private String isIncludeFee;//是否查询录入的费用
	
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getDeptOpAndAnae() {
		return deptOpAndAnae;
	}
	public void setDeptOpAndAnae(String deptOpAndAnae) {
		this.deptOpAndAnae = deptOpAndAnae;
	}
	public String getDtAnae() {
		return dtAnae;
	}
	public void setDtAnae(String dtAnae) {
		this.dtAnae = dtAnae;
	}
	public String getIsAnae() {
		return isAnae;
	}
	public void setIsAnae(String isAnae) {
		this.isAnae = isAnae;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public Date getDatePlanBegin() {
		return datePlanBegin;
	}
	public void setDatePlanBegin(Date datePlanBegin) {
		this.datePlanBegin = datePlanBegin;
	}
	public Date getDatePlanEnd() {
		return datePlanEnd;
	}
	public void setDatePlanEnd(Date datePlanEnd) {
		this.datePlanEnd = datePlanEnd;
	}
	public String getTicketno() {
		return ticketno;
	}
	public void setTicketno(String ticketno) {
		this.ticketno = ticketno;
	}
	public String getPkOrg() {
		return pkOrg;
	}
	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}
	public Date getDateStartBegin() {
		return dateStartBegin;
	}
	public void setDateStartBegin(Date dateStartBegin) {
		this.dateStartBegin = dateStartBegin;
	}
	public Date getDateStartEnd() {
		return dateStartEnd;
	}
	public void setDateStartEnd(Date dateStartEnd) {
		this.dateStartEnd = dateStartEnd;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getEuPvtype() {
		return euPvtype;
	}
	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public String getNamePi() {
		return namePi;
	}
	public void setNamePi(String namePi) {
		this.namePi = namePi;
	}
	public String getCodePi() {
		return codePi;
	}
	public void setCodePi(String codePi) {
		this.codePi = codePi;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getFlagDetial() {
		return flagDetial;
	}
	public void setFlagDetial(String flagDetial) {
		this.flagDetial = flagDetial;
	}
	public String getOrdDeptByOp() {
		return ordDeptByOp;
	}
	public void setOrdDeptByOp(String ordDeptByOp) {
		this.ordDeptByOp = ordDeptByOp;
	}
	public String getOrdDeptNsByOp() {
		return ordDeptNsByOp;
	}
	public void setOrdDeptNsByOp(String ordDeptNsByOp) {
		this.ordDeptNsByOp = ordDeptNsByOp;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
	public String getIsIncludeFee() {
		return isIncludeFee;
	}
	public void setIsIncludeFee(String isIncludeFee) {
		this.isIncludeFee = isIncludeFee;
	}
}
