package com.zebone.nhis.scm.st.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;

public class AllocationAppVo extends PdPlan{
	private String plantype;
	private String nameOrg;
	private String nameStore;
	private List<AllocationAppDtVo> dtlist;
	private List<Object[]> delDtList;//删除的明细主键
	
	public List<Object[]> getDelDtList() {
		return delDtList;
	}
	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}
	public List<AllocationAppDtVo> getDtlist() {
		return dtlist;
	}
	public void setDtlist(List<AllocationAppDtVo> dtlist) {
		this.dtlist = dtlist;
	}
	public String getPlantype() {
		return plantype;
	}
	public void setPlantype(String plantype) {
		this.plantype = plantype;
	}
	public String getNameOrg() {
		return nameOrg;
	}
	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
	public String getNameStore() {
		return nameStore;
	}
	public void setNameStore(String nameStore) {
		this.nameStore = nameStore;
	}
	
	

}
