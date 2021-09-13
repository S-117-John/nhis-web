package com.zebone.nhis.scm.material.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;

@SuppressWarnings("serial")
public class MtlAllAppVo extends PdPlan{
	private String plantype;
	private String nameOrgAcc;
	private String nameStoreAcc;
	private String nameDeptAcc;
	private List<MtlAllAppDtVo> dtlist;
	private List<Object[]> delDtList;//删除的明细主键
	
	public List<Object[]> getDelDtList() {
		return delDtList;
	}
	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}
	public List<MtlAllAppDtVo> getDtlist() {
		return dtlist;
	}
	public void setDtlist(List<MtlAllAppDtVo> dtlist) {
		this.dtlist = dtlist;
	}
	public String getPlantype() {
		return plantype;
	}
	public void setPlantype(String plantype) {
		this.plantype = plantype;
	}
	public String getNameOrgAcc() {
		return nameOrgAcc;
	}
	public void setNameOrgAcc(String nameOrgAcc) {
		this.nameOrgAcc = nameOrgAcc;
	}
	public String getNameStoreAcc() {
		return nameStoreAcc;
	}
	public void setNameStoreAcc(String nameStoreAcc) {
		this.nameStoreAcc = nameStoreAcc;
	}
	public String getNameDeptAcc() {
		return nameDeptAcc;
	}
	public void setNameDeptAcc(String nameDeptAcc) {
		this.nameDeptAcc = nameDeptAcc;
	}
	
	
	

}
