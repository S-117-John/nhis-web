package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdDeptBu;
import com.zebone.nhis.common.module.base.bd.res.BdDeptBus;

public class DeptBuAndBusesParam {

	/**
	 * 业务线定义
	 */
	private BdDeptBu deptbu = new BdDeptBu();
	
	/**
	 * 线下部门定义
	 */
	private List<BdDeptBus> deptbusList = new ArrayList<BdDeptBus>();

	public BdDeptBu getDeptbu() {
		return deptbu;
	}

	public void setDeptbu(BdDeptBu deptbu) {
		this.deptbu = deptbu;
	}

	public List<BdDeptBus> getDeptbusList() {
		return deptbusList;
	}

	public void setDeptbusList(List<BdDeptBus> deptbusList) {
		this.deptbusList = deptbusList;
	}
}
