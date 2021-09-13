package com.zebone.nhis.scm.material.vo;

import java.util.Date;

import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

@SuppressWarnings("serial")
public class MtlPdPlanDtInfo extends PdPlanDetail {
	
	private String code;
    private String namePd;
    private String spcode;
    private String spec;
    private String nameFactory;
    private String nameUnit;
    private String nameSupplyer;
    private String unitPd;
    private String pkOrgPlan;
    private String pkDeptPlan;
    private String pkStorePlan;
    private String euStockmode;
    private Date dateValidReg;
    private Date dateValidRun;
    private Date dateValidLicense;
    private String name;
    private String flagStop;

	public String getFlagStop() {
		return flagStop;
	}
	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getEuStockmode() {
		return euStockmode;
	}
	public void setEuStockmode(String euStockmode) {
		this.euStockmode = euStockmode;
	}
	public Date getDateValidReg() {
		return dateValidReg;
	}
	public void setDateValidReg(Date dateValidReg) {
		this.dateValidReg = dateValidReg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getNamePd() {
		return namePd;
	}
	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcodePd(String spcode) {
		this.spcode = spcode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getNameFactory() {
		return nameFactory;
	}
	public void setNameFactory(String nameFactory) {
		this.nameFactory = nameFactory;
	}
	public String getNameUnit() {
		return nameUnit;
	}
	public void setNameUnit(String nameUnit) {
		this.nameUnit = nameUnit;
	}
	public String getNameSupplyer() {
		return nameSupplyer;
	}
	public void setNameSupplyer(String nameSupply) {
		this.nameSupplyer = nameSupply;
	}
	public String getUnitPd() {
		return unitPd;
	}
	public void setUnitPd(String unitPd) {
		this.unitPd = unitPd;
	}
	public String getPkOrgPlan() {
		return pkOrgPlan;
	}
	public void setPkOrgPlan(String pkOrgPlan) {
		this.pkOrgPlan = pkOrgPlan;
	}
	public String getPkDeptPlan() {
		return pkDeptPlan;
	}
	public void setPkDeptPlan(String pkDeptPlan) {
		this.pkDeptPlan = pkDeptPlan;
	}
	public String getPkStorePlan() {
		return pkStorePlan;
	}
	public void setPkStorePlan(String pkStorePlan) {
		this.pkStorePlan = pkStorePlan;
	}
	public Date getDateValidRun() {
		return dateValidRun;
	}
	public void setDateValidRun(Date dateValidRun) {
		this.dateValidRun = dateValidRun;
	}
	public Date getDateValidLicense() {
		return dateValidLicense;
	}
	public void setDateValidLicense(Date dateValidLicense) {
		this.dateValidLicense = dateValidLicense;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
