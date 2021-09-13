package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;

public class BdOrdSetDtVO extends BdOrdSetDt {
	private String ordCode;
	private String ordName;
	private String pkUnitMin;
	private String pkUnitWt;
	private String pkUnitVol;
	private double weight;
	private double vol;
	private String spec;//规格
	private String ordtype;//医嘱类型
	private String price;
	private String unit;//用量单位
	private String unitDos;//剂量单位
	private String DeptExec;//执行科室
	private String EuUsecate;//药品使用范围

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getOrdCode() {
		return ordCode;
	}

	public void setOrdCode(String ordCode) {
		this.ordCode = ordCode;
	}

	public String getOrdName() {
		return ordName;
	}

	public void setOrdName(String ordName) {
		this.ordName = ordName;
	}

	public String getPkUnitMin() {
		return pkUnitMin;
	}

	public void setPkUnitMin(String pkUnitMin) {
		this.pkUnitMin = pkUnitMin;
	}

	public String getPkUnitWt() {
		return pkUnitWt;
	}

	public void setPkUnitWt(String pkUnitWt) {
		this.pkUnitWt = pkUnitWt;
	}

	public String getPkUnitVol() {
		return pkUnitVol;
	}

	public void setPkUnitVol(String pkUnitVol) {
		this.pkUnitVol = pkUnitVol;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public double getVol() {
		return vol;
	}

	public void setVol(double vol) {
		this.vol = vol;
	}

	public String getOrdtype() {
		return ordtype;
	}

	public void setOrdtype(String ordtype) {
		this.ordtype = ordtype;
	}

	public String getUnitDos() {
		return unitDos;
	}

	public void setUnitDos(String unitDos) {
		this.unitDos = unitDos;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getDeptExec() {
		return DeptExec;
	}

	public void setDeptExec(String deptExec) {
		DeptExec = deptExec;
	}

	public String getEuUsecate() {
		return EuUsecate;
	}

	public void setEuUsecate(String euUsecate) {
		EuUsecate = euUsecate;
	}
}
