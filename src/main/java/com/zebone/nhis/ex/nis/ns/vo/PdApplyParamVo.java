package com.zebone.nhis.ex.nis.ns.vo;

import java.util.List;

public class PdApplyParamVo {
	
	private String pkDeptNs;
	private String endDate;
	private List<PdApplyVo> ordsPivas;
	private List<String> pkPvs;
	private String flagOnce;
	//医嘱主键集合--核对时自动生成请领使用
	private List<String> pkCnords;
	
	public List<String> getPkCnords() {
		return pkCnords;
	}
	public void setPkCnords(List<String> pkCnords) {
		this.pkCnords = pkCnords;
	}
	public String getFlagOnce() {
		return flagOnce;
	}
	public void setFlagOnce(String flagOnce) {
		this.flagOnce = flagOnce;
	}
	public List<String> getPkPvs() {
		return pkPvs;
	}
	public void setPkPvs(List<String> pkPvs) {
		this.pkPvs = pkPvs;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<PdApplyVo> getOrdsPivas() {
		return ordsPivas;
	}
	public void setOrdsPivas(List<PdApplyVo> ordsPivas) {
		this.ordsPivas = ordsPivas;
	}
}
