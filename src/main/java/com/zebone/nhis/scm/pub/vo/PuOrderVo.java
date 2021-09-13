package com.zebone.nhis.scm.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPurchase;

public class PuOrderVo extends PdPurchase{

	private List<PuOrderDtVo> dt;
	//采购计划主键
	private String pkPdPlan;
	private String nameDept;
	private String nameSpr;
	private String putype;
	
	private List<Object[]> delDtList;//删除的明细主键

	public List<Object[]> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<Object[]> delDtList) {
		this.delDtList = delDtList;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

	public String getNameSpr() {
		return nameSpr;
	}

	public void setNameSpr(String nameSpr) {
		this.nameSpr = nameSpr;
	}

	public String getPutype() {
		return putype;
	}

	public void setPutype(String putype) {
		this.putype = putype;
	}

	public String getPkPdPlan() {
		return pkPdPlan;
	}

	public void setPkPdPlan(String pkPdPlan) {
		this.pkPdPlan = pkPdPlan;
	}

	public List<PuOrderDtVo> getDt() {
		return dt;
	}

	public void setDt(List<PuOrderDtVo> dt) {
		this.dt = dt;
	}

}
