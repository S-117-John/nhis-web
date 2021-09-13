package com.zebone.nhis.scm.material.vo;

import java.sql.Date;

import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

public class MtlAllAppDtVo  extends PdPlanDetail{
	private String pdname;
	private String pdcode;
	private String spec;
	private String factory;
	private String unit;
	private String nameOrg;
	private String nameStore;
	private Double quanStk;
	private String spcode;
	private String pdnameas;
	private String unitPd;
	
	
	//消耗计算
	 private Date dateEnd;
	    
	 private String accounts;
	 
	 private Integer cnt;
	 
	 private Integer cntPlan;
	public Date getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public Integer getCnt() {
		return cnt;
	}
	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}
	public Integer getCntPlan() {
		return cntPlan;
	}
	public void setCntPlan(Integer cntPlan) {
		this.cntPlan = cntPlan;
	}
	public String getUnitPd() {
		return unitPd;
	}
	public void setUnitPd(String unitPd) {
		this.unitPd = unitPd;
	}
	public String getPdnameas() {
		return pdnameas;
	}
	public void setPdnameas(String pdnameas) {
		this.pdnameas = pdnameas;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getPdname() {
		return pdname;
	}
	public void setPdname(String pdname) {
		this.pdname = pdname;
	}
	public String getPdcode() {
		return pdcode;
	}
	public void setPdcode(String pdcode) {
		this.pdcode = pdcode;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public Double getQuanStk() {
		return quanStk;
	}
	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	
	

}
