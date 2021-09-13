package com.zebone.nhis.scm.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;

public class PdPlanVo extends PdPlan{
	
	private String pltype;//计划类型名称
	private String nameOrg;//所属机构名称
	private String nameDept;//计划部门名称
	private String nameStore;//计划仓库名称
	
    private List<PdPlanDtVo> dtlist;
    
	private List<Object[]> delDtList;//删除的明细主键

	public List<Object[]> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}

	public String getPltype() {
		return pltype;
	}

	public void setPltype(String pltype) {
		this.pltype = pltype;
	}

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameStore() {
		return nameStore;
	}

	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}

	public List<PdPlanDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<PdPlanDtVo> dtlist) {
		this.dtlist = dtlist;
	}
    
    
}
