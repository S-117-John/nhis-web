package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

public class PdPlanDtVo extends PdPlanDetail{
	private String codePd;
    private String namePd;
    private String spec;
    private String factoryName;
    private String unitName;
    private String pkOrgPlan;
    private String pkDeptPlan;
    private String pkStorePlan;
    private String spcode;
    private String supplyName;
    private String unitPd;
    private Integer packSizePd;//零售包装量
	private String flagUse;//在用属性

	public String getFlagUse() {
		return flagUse;
	}

	public void setFlagUse(String flagUse) {
		this.flagUse = flagUse;
	}

	public Integer getPackSizePd() {
		return packSizePd;
	}
	public void setPackSizePd(Integer packSizePd) {
		this.packSizePd = packSizePd;
	}
	public String getUnitPd() {
		return unitPd;
	}
	public void setUnitPd(String unitPd) {
		this.unitPd = unitPd;
	}
	public String getSupplyName() {
		return supplyName;
	}
	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getCodePd() {
		return codePd;
	}
	public void setCodePd(String codePd) {
		this.codePd = codePd;
	}
	public String getNamePd() {
		return namePd;
	}
	public void setNamePd(String namePd) {
		this.namePd = namePd;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
    
}
