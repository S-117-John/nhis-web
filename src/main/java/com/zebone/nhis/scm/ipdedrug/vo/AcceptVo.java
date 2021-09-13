package com.zebone.nhis.scm.ipdedrug.vo;

import java.util.List;

public class AcceptVo {
	private List<DispatchVo> list;
	private EmpVo emp;
	public List<DispatchVo> getList() {
		return list;
	}
	public void setList(List<DispatchVo> list) {
		this.list = list;
	}
	public EmpVo getEmp() {
		return emp;
	}
	public void setEmp(EmpVo emp) {
		this.emp = emp;
	}
}
