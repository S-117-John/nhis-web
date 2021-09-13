package com.zebone.nhis.pv.reg.vo;

public class DeptAndResource {
	// 队列主键
	private String pkQcque;
	// 科室主键
	private String pkDept;
	// 科室名称
	private String nameDept;
	// 资源主键
	private String pkSchres;
	// 资源名称
	private String nameRes;
	// 服务主键
	private String pkSchsrv;
	// 服务名称
	private String nameSrv;
	// 服务类型 0 普通；1 专家；2 特诊；9 急诊
	private String euSrvtype;
	// 排队人数
	private int cnt;
	// 排班主键
	private String pkSch;
	private String pkEmp;
	private String resDept;
	private String euRestype;
	public String getPkQcque() {
		return pkQcque;
	}
	public void setPkQcque(String pkQcque) {
		this.pkQcque = pkQcque;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	public String getPkSchres() {
		return pkSchres;
	}
	public void setPkSchres(String pkSchres) {
		this.pkSchres = pkSchres;
	}
	public String getNameRes() {
		return nameRes;
	}
	public void setNameRes(String nameRes) {
		this.nameRes = nameRes;
	}
	public String getPkSchsrv() {
		return pkSchsrv;
	}
	public void setPkSchsrv(String pkSchsrv) {
		this.pkSchsrv = pkSchsrv;
	}
	public String getNameSrv() {
		return nameSrv;
	}
	public void setNameSrv(String nameSrv) {
		this.nameSrv = nameSrv;
	}
	public String getEuSrvtype() {
		return euSrvtype;
	}
	public void setEuSrvtype(String euSrvtype) {
		this.euSrvtype = euSrvtype;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getPkSch() {
		return pkSch;
	}
	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}
	public String getPkEmp() {
		return pkEmp;
	}
	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	public String getResDept() {
		return resDept;
	}
	public void setResDept(String resDept) {
		this.resDept = resDept;
	}
	public String getEuRestype() {
		return euRestype;
	}
	public void setEuRestype(String euRestype) {
		this.euRestype = euRestype;
	}
	
}
