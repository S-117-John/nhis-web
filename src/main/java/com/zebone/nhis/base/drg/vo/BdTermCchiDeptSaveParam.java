package com.zebone.nhis.base.drg.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.drg.BdTermCchiDept;

public class BdTermCchiDeptSaveParam {
	public BdTermCchiDept bdTermCchiDept;
	
	public List<BdTermCchiDept> addTermCchiDeptList;
	
	public List<BdTermCchiDept> delTermCchiDeptList;

	public BdTermCchiDept getBdTermCchiDept() {
		return bdTermCchiDept;
	}

	public void setBdTermCchiDept(BdTermCchiDept bdTermCchiDept) {
		this.bdTermCchiDept = bdTermCchiDept;
	}

	public List<BdTermCchiDept> getAddTermCchiDeptList() {
		return addTermCchiDeptList;
	}

	public void setAddTermCchiDeptList(List<BdTermCchiDept> addTermCchiDeptList) {
		this.addTermCchiDeptList = addTermCchiDeptList;
	}

	public List<BdTermCchiDept> getDelTermCchiDeptList() {
		return delTermCchiDeptList;
	}

	public void setDelTermCchiDeptList(List<BdTermCchiDept> delTermCchiDeptList) {
		this.delTermCchiDeptList = delTermCchiDeptList;
	}
	
	
}
