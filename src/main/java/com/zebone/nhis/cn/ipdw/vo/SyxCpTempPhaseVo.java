package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.cp.SyxCpTempPhase;

public class SyxCpTempPhaseVo extends SyxCpTempPhase{

	private String phaseStatus;
	private List<SyxCpTempPhaseOrdVo> phaseOrd;

	public List<SyxCpTempPhaseOrdVo> getPhaseOrd() {
		return phaseOrd;
	}

	public void setPhaseOrd(List<SyxCpTempPhaseOrdVo> phaseOrd) {
		this.phaseOrd = phaseOrd;
	}

	public String getPhaseStatus() {
		return phaseStatus;
	}

	public void setPhaseStatus(String phaseStatus) {
		this.phaseStatus = phaseStatus;
	}
	
}
