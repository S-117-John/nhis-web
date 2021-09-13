package com.zebone.nhis.ma.self.vo;

import java.util.Date;

public class SelfPatInfo {
private String pkPv;
private String pkPi;
private String namePi;
private String codePv;
private String pkDept;
private String pkDeptNs;
private Date dateReg;
private Double AmtDepo;
private String pkPiacc;
private Double amtAcc;
private Double creditAcc;
private String euStatus;


public String getPkPv() {
	return pkPv;
}
public void setPkPv(String pkPv) {
	this.pkPv = pkPv;
}
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
public String getCodePv() {
	return codePv;
}
public void setCodePv(String codePv) {
	this.codePv = codePv;
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
public Date getDateReg() {
	return dateReg;
}
public void setDateReg(Date dateReg) {
	this.dateReg = dateReg;
}
public String getPkPiacc() {
	return pkPiacc;
}
public Double getAmtDepo() {
	return AmtDepo;
}
public void setAmtDepo(Double amtDepo) {
	AmtDepo = amtDepo;
}
public void setPkPiacc(String pkPiacc) {
	this.pkPiacc = pkPiacc;
}
public Double getAmtAcc() {
	return amtAcc;
}
public void setAmtAcc(Double amtAcc) {
	this.amtAcc = amtAcc;
}
public Double getCreditAcc() {
	return creditAcc;
}
public void setCreditAcc(Double creditAcc) {
	this.creditAcc = creditAcc;
}
public String getEuStatus() {
	return euStatus;
}
public void setEuStatus(String euStatus) {
	this.euStatus = euStatus;
}
}
