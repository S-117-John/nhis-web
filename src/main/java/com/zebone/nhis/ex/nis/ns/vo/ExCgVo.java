package com.zebone.nhis.ex.nis.ns.vo;

import java.util.Date;

public class ExCgVo {
	public String pkExocc;
	public double quan;
	public String pkCgip;
	public String euStatus;
	public String pkCnord;
	public Date datePlan;
    public String pkPdapdt;
	
	public String getPkPdapdt() {
		return pkPdapdt;
	}

	public void setPkPdapdt(String pkPdapdt) {
		this.pkPdapdt = pkPdapdt;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkCnord() {
		return pkCnord;
	}

	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}

	public Date getDatePlan() {
		return datePlan;
	}

	public void setDatePlan(Date datePlan) {
		this.datePlan = datePlan;
	}

	public String getPkExocc() {
		return pkExocc;
	}

	public void setPkExocc(String pkExocc) {
		this.pkExocc = pkExocc;
	}

	public double getQuan() {
		return quan;
	}

	public void setQuan(double quan) {
		this.quan = quan;
	}

	public String getPkCgip() {
		return pkCgip;
	}

	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}

}
