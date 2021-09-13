package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.wf.BdPivasruleDept;

//静配规则使用科室扩展类
public class BdPivasruleDeptVO extends BdPivasruleDept{
	
	//使用科室列表
	private List<BdPivasruleDept> list;
	
	//静配规则
	private String pkPivasrule;

	public List<BdPivasruleDept> getList() {
		return list;
	}

	public void setList(List<BdPivasruleDept> list) {
		this.list = list;
	}

	public String getPkPivasrule() {
		return pkPivasrule;
	}

	public void setPkPivasrule(String pkPivasrule) {
		this.pkPivasrule = pkPivasrule;
	}
	
	
}
