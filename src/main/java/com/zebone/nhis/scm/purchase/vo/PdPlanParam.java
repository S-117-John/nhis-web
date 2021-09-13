package com.zebone.nhis.scm.purchase.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;



/**
 * 采购计划及其明细
 * 
 */
public class PdPlanParam{
	
	/** 采购计划 */
	private PdPlan pdPlan;
	
	/** 采购计划明细 */
	List<PdPlanDetail> planDetailList;

	public PdPlan getPdPlan() {
		return pdPlan;
	}

	public void setPdPlan(PdPlan pdPlan) {
		this.pdPlan = pdPlan;
	}

	public List<PdPlanDetail> getPlanDetailList() {
		return planDetailList;
	}

	public void setPlanDetailList(List<PdPlanDetail> planDetailList) {
		this.planDetailList = planDetailList;
	}
 
	
}