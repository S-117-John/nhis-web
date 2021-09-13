package com.zebone.nhis.scm.material.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.scm.purchase.PdPlan;
import com.zebone.nhis.common.module.scm.purchase.PdPlanDetail;

public class MtlSavePdPlanVo {
	
	private PdPlan pdPlan;

	private List<PdPlanDetail> pdPlanDtList = new ArrayList<PdPlanDetail>();
	
	private String pkPlanList;

	public PdPlan getPdPlan() {
		return pdPlan;
	}

	public void setPdPlan(PdPlan pdPlan) {
		this.pdPlan = pdPlan;
	}

	public List<PdPlanDetail> getPdPlanDtList() {
		return pdPlanDtList;
	}

	public void setPdPlanDtList(List<PdPlanDetail> pdPlanDtList) {
		this.pdPlanDtList = pdPlanDtList;
	}

	public String getPkPlanList() {
		return pkPlanList;
	}

	public void setPkPlanList(String pkPlanList) {
		this.pkPlanList = pkPlanList;
	} 
}
