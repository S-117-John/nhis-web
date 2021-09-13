package com.zebone.nhis.base.ou.vo;

import com.zebone.nhis.common.module.base.ou.BdOuEmpIntern;
import com.zebone.nhis.common.module.base.ou.BdOuEmpjob;
import com.zebone.nhis.common.module.base.ou.BdOuEmployee;

public class EmpAndInternParam {
	/**
	 * 人员信息
	 */
	private BdOuEmployee emp = new BdOuEmployee();
	
	/**
	 * 实习人员信息
	 */
	private BdOuEmpIntern internEmp = new BdOuEmpIntern();
	
	/**
	 * 关系
	 */
	private BdOuEmpjob empJobVo = new BdOuEmpjob();

	public BdOuEmployee getEmp() {
		return emp;
	}

	public void setEmp(BdOuEmployee emp) {
		this.emp = emp;
	}

	

	public BdOuEmpIntern getInternEmp() {
		return internEmp;
	}

	public void setInternEmp(BdOuEmpIntern internEmp) {
		this.internEmp = internEmp;
	}

	public BdOuEmpjob getEmpJobVo() {
		return empJobVo;
	}

	public void setEmpJobVo(BdOuEmpjob empJobVo) {
		this.empJobVo = empJobVo;
	}

	

	

	

}
