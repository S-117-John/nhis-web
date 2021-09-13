package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.wf.BdFlow;
import com.zebone.nhis.common.module.base.bd.wf.BdFlowStep;

public class BdFlowVO{

	private List<BdFlowStep> flowStepList;
	
	private BdFlow flow;

	public List<BdFlowStep> getFlowStepList() {
		return flowStepList;
	}

	public void setFlowStepList(List<BdFlowStep> flowStepList) {
		this.flowStepList = flowStepList;
	}

	public BdFlow getFlow() {
		return flow;
	}

	public void setFlow(BdFlow flow) {
		this.flow = flow;
	}
	
}
