package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdResPcArgu;

public class SaveParam {
	private String pkDept;
	private List<BdResPcArgu> arguList = new ArrayList<BdResPcArgu>();
	
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public List<BdResPcArgu> getArguList() {
		return arguList;
	}
	public void setArguList(List<BdResPcArgu> arguList) {
		this.arguList = arguList;
	}
	
	

}
