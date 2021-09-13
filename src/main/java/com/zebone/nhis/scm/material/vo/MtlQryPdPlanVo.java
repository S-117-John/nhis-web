package com.zebone.nhis.scm.material.vo;

import java.util.List;

public class MtlQryPdPlanVo {
	
	private MtlPdPlanInfo pdPlan;
	
	private List<MtlPdPlanDtInfo> pdPlanDtList;

	public MtlPdPlanInfo getPdPlan() {
		return pdPlan;
	}

	public void setPdPlan(MtlPdPlanInfo pdPlan) {
		this.pdPlan = pdPlan;
	}

	public List<MtlPdPlanDtInfo> getPdPlanDtList() {
		return pdPlanDtList;
	}

	public void setPdPlanDtList(List<MtlPdPlanDtInfo> pdPlatDtList) {
		this.pdPlanDtList = pdPlatDtList;
	}
}
