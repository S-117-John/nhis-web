package com.zebone.nhis.cn.pub.vo;

public class OpPdStorePackVo {

	private String pkPd;
	private String pkUnit;
	private double packSize;
	private double quanDisp;
	private String pkDeptExec;
	
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public double getQuanDisp() {
		return quanDisp;
	}
	public void setQuanDisp(double quanDisp) {
		this.quanDisp = quanDisp;
	}
	public String getPkPd() {
		return pkPd;
	}
	public void setPkPd(String pkPd) {
		this.pkPd = pkPd;
	}
	public String getPkUnit() {
		return pkUnit;
	}
	public void setPkUnit(String pkUnit) {
		this.pkUnit = pkUnit;
	}
	public double getPackSize() {
		return packSize;
	}
	public void setPackSize(double packSize) {
		this.packSize = packSize;
	}
	
	
}
