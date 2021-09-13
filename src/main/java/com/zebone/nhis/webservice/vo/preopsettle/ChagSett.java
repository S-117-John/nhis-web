package com.zebone.nhis.webservice.vo.preopsettle;

import java.math.BigDecimal;
import java.util.List;

public class ChagSett {
	BigDecimal aggregateAmount = null;
	String amtInsuThird ="";
	BlDeposits BlDeposits = null;
	BlOpDts blOpDts = null;
	String codeEmpSt="";
	BigDecimal medicarePayments;
	String note;
	BigDecimal patientsPay;
	String pkDeptSt; 
    String pkOrgSt;
    String pkPi;
    String pkPv;
    
	public BigDecimal getMedicarePayments() {
		return medicarePayments;
	}
	public void setMedicarePayments(BigDecimal medicarePayments) {
		this.medicarePayments = medicarePayments;
	}
	public BigDecimal getAggregateAmount() {
		return aggregateAmount;
	}
	public void setAggregateAmount(BigDecimal aggregateAmount) {
		this.aggregateAmount = aggregateAmount;
	}
	public String getAmtInsuThird() {
		return amtInsuThird;
	}
	public void setAmtInsuThird(String amtInsuThird) {
		this.amtInsuThird = amtInsuThird;
	}
	public BlDeposits getBlDeposits() {
		return BlDeposits;
	}
	public void setBlDeposits(BlDeposits blDeposits) {
		BlDeposits = blDeposits;
	}
	public String getCodeEmpSt() {
		return codeEmpSt;
	}
	public void setCodeEmpSt(String codeEmpSt) {
		this.codeEmpSt = codeEmpSt;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public BlOpDts getBlOpDts() {
		return blOpDts;
	}
	public void setBlOpDts(BlOpDts blOpDts) {
		this.blOpDts = blOpDts;
	}
	public BigDecimal getPatientsPay() {
		return patientsPay;
	}
	public void setPatientsPay(BigDecimal patientsPay) {
		this.patientsPay = patientsPay;
	}
	public String getPkDeptSt() {
		return pkDeptSt;
	}
	public void setPkDeptSt(String pkDeptSt) {
		this.pkDeptSt = pkDeptSt;
	}
	public String getPkOrgSt() {
		return pkOrgSt;
	}
	public void setPkOrgSt(String pkOrgSt) {
		this.pkOrgSt = pkOrgSt;
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
