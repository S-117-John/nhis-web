package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.cn.cp.SyxCpTemp;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempDiag;
import com.zebone.nhis.common.module.cn.cp.SyxCpTempPhase;

public class SyxCpTempVo {

	//路径模板基本信息
	private SyxCpTemp cpTemp;
	
	//路径模板审批信息
	
	private List<BdFlowSpVo> flowSp;
	
	//路径模板适用诊断
	
	private List<SyxCpTempDiag> cpTempDiag;
	
	//路径模板适用科室
	
	private List<SyxCpTempDeptVo> cpTempDept;
	
	//路径模板表单
	
	private List<SyxCpTempFormPhaseVo> cpTempForm;
	
	//路径模板阶段查询用
	
	private List<SyxCpTempPhase> cpTempPhase;
	
	//路径模板阶段保存用
	private SyxCpTempPhaseVo cpTempPhaseVo;
	
	//刷新路径定义页签用
	private SyxCpTempPhaseOrdVo cpTempPhaseOrdVo;
	
	
	private String isUpdate;
	
	private String isNewVersion;
	
	private String tabPage;
	
	private String pkCptemp;
		
	
	public String getPkCptemp() {
		return pkCptemp;
	}

	public void setPkCptemp(String pkCptemp) {
		this.pkCptemp = pkCptemp;
	}

	public String getTabPage() {
		return tabPage;
	}

	public void setTabPage(String tabPage) {
		this.tabPage = tabPage;
	}

	public SyxCpTempPhaseVo getCpTempPhaseVo() {
		return cpTempPhaseVo;
	}

	public void setCpTempPhaseVo(SyxCpTempPhaseVo cpTempPhaseVo) {
		this.cpTempPhaseVo = cpTempPhaseVo;
	}

	public String getIsUpdate() {
		return isUpdate;
	}

	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getIsNewVersion() {
		return isNewVersion;
	}

	public void setIsNewVersion(String isNewVersion) {
		this.isNewVersion = isNewVersion;
	}

	public SyxCpTemp getCpTemp() {
		return cpTemp;
	}

	public void setCpTemp(SyxCpTemp cpTemp) {
		this.cpTemp = cpTemp;
	}

	public List<BdFlowSpVo> getFlowSp() {
		return flowSp;
	}

	public void setFlowSp(List<BdFlowSpVo> flowSp) {
		this.flowSp = flowSp;
	}

	public List<SyxCpTempDiag> getCpTempDiag() {
		return cpTempDiag;
	}

	public void setCpTempDiag(List<SyxCpTempDiag> cpTempDiag) {
		this.cpTempDiag = cpTempDiag;
	}

	public List<SyxCpTempDeptVo> getCpTempDept() {
		return cpTempDept;
	}

	public void setCpTempDept(List<SyxCpTempDeptVo> cpTempDept) {
		this.cpTempDept = cpTempDept;
	}

	public List<SyxCpTempFormPhaseVo> getCpTempForm() {
		return cpTempForm;
	}

	public void setCpTempForm(List<SyxCpTempFormPhaseVo> cpTempForm) {
		this.cpTempForm = cpTempForm;
	}

	public List<SyxCpTempPhase> getCpTempPhase() {
		return cpTempPhase;
	}

	public void setCpTempPhase(List<SyxCpTempPhase> cpTempPhase) {
		this.cpTempPhase = cpTempPhase;
	}

	public SyxCpTempPhaseOrdVo getCpTempPhaseOrdVo() {
		return cpTempPhaseOrdVo;
	}

	public void setCpTempPhaseOrdVo(SyxCpTempPhaseOrdVo cpTempPhaseOrdVo) {
		this.cpTempPhaseOrdVo = cpTempPhaseOrdVo;
	}
	
	
}
