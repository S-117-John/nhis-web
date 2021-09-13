package com.zebone.nhis.pv.reg.vo;

public class TransParamVo {
	
	/**
	 * 出诊医生
	 */
	private String pkEmp;

	/**
	 * 患者就诊
	 */
	private String pkPv;
	
	/**
	 * 转入科室
	 */
	private String pkDept;
	
	/**
	 * 转入资源
	 */
	private String pkSchres;
	
	/**
	 * 转入服务
	 */
	private String pkSchsrv;
	
	/**
	 * 关联排班
	 */
	private String pkSch;
	
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getPkSchres() {
		return pkSchres;
	}
	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}
	public String getPkSchsrv() {
		return pkSchsrv;
	}
	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}
	public String getPkSch() {
		return pkSch;
	}
	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}
	
	
}
