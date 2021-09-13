package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivBed;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivHvitem;
import com.zebone.nhis.common.module.base.bd.price.InsGzgyDivSpitem;

public class StrategySetVo {
	
	//床位费报销策略
	private List<InsGzgyDivBed> bedList = new ArrayList<InsGzgyDivBed>();
	
	//高值耗材策略
	private List<InsGzgyDivHvitem> hvitemList = new ArrayList<InsGzgyDivHvitem>();
	
	//特殊项目策略
	private List<InsGzgyDivSpitem> spitemList = new ArrayList<InsGzgyDivSpitem>();

	public List<InsGzgyDivBed> getBedList() {
		return bedList;
	}

	public void setBedList(List<InsGzgyDivBed> bedList) {
		this.bedList = bedList;
	}

	public List<InsGzgyDivHvitem> getHvitemList() {
		return hvitemList;
	}

	public void setHvitemList(List<InsGzgyDivHvitem> hvitemList) {
		this.hvitemList = hvitemList;
	}

	public List<InsGzgyDivSpitem> getSpitemList() {
		return spitemList;
	}

	public void setSpitemList(List<InsGzgyDivSpitem> spitemList) {
		this.spitemList = spitemList;
	}

	
}
