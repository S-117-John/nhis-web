package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

public class SureOpOrdParam {

	private String pkDeptNs;
	private List<String> pkpvs;
	private String isSure;
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public List<String> getPkpvs() {
		return pkpvs;
	}
	public void setPkpvs(List<String> pkpvs) {
		this.pkpvs = pkpvs;
	}
	public String getIsSure() {
		return isSure;
	}
	public void setIsSure(String isSure) {
		this.isSure = isSure;
	}
	
	
}
