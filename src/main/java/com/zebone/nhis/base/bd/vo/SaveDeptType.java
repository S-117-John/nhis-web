package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdResPcArgu;

public class SaveDeptType {
	public String code;
	private List<BdResPcArgu> arguList = new ArrayList<BdResPcArgu>();

	public List<BdResPcArgu> getArguList() {
		return arguList;
	}

	public void setArguList(List<BdResPcArgu> arguList) {
		this.arguList = arguList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
