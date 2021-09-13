package com.zebone.nhis.sch.pub.vo;

import com.zebone.nhis.common.module.sch.plan.SchPlan;

public class SchPlanVo extends SchPlan {
	private String euRestype;// 资源类型 0：部门 1：人员

	/** 来自资源的pk_dept_belong*/
	private String pkDeptBelong;
	
	private String pkEmp;
	
	public String getEuRestype() {
		return euRestype;
	}

	public void setEuRestype(String euRestype) {
		this.euRestype = euRestype;
	}

	public String getPkDeptBelong() {
		return pkDeptBelong;
	}

	public void setPkDeptBelong(String pkDeptBelong) {
		this.pkDeptBelong = pkDeptBelong;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}
	
}
