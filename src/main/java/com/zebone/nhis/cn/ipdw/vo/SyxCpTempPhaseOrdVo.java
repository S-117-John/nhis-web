package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.cp.SyxCpTempCpord;

public class SyxCpTempPhaseOrdVo extends SyxCpTempCpord{

	private List<SyxCpTempCpord> doctorWork;
	private List<SyxCpTempCpord> phaseOrd;
	private List<SyxCpTempCpord> nsWork;
	private String rowStatus;
	
	public List<SyxCpTempCpord> getDoctorWork() {
		return doctorWork;
	}
	public void setDoctorWork(List<SyxCpTempCpord> doctorWork) {
		this.doctorWork = doctorWork;
	}
	public List<SyxCpTempCpord> getPhaseOrd() {
		return phaseOrd;
	}
	public void setPhaseOrd(List<SyxCpTempCpord> phaseOrd) {
		this.phaseOrd = phaseOrd;
	}
	public List<SyxCpTempCpord> getNsWork() {
		return nsWork;
	}
	public void setNsWork(List<SyxCpTempCpord> nsWork) {
		this.nsWork = nsWork;
	}
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	
}
