package com.zebone.nhis.scm.material.vo;

import java.sql.Date;

import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

public class MtlPdPlanDtVo  extends PdPlanDetail{
	private String name;
	private String code;
	private String spec;
	private String factoryName;
	private String unitName;
	private Double quanStk;
	private String spcode;
	private String status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
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
	public Double getQuanStk() {
		return quanStk;
	}
	public void setQuanStk(Double quanStk) {
		this.quanStk = quanStk;
	}
	public String getSpcode() {
		return spcode;
	}
	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	

}
