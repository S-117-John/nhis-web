package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.BdResPcArgu;

public class SaveMoreParam {
	
	private List<BdResPcArgu> arguList = new ArrayList<BdResPcArgu>();
	private List<MoreParamVo> list = new ArrayList<MoreParamVo>();

	public List<MoreParamVo> getList() {
		return list;
	}

	public void setList(List<MoreParamVo> list) {
		this.list = list;
	}

	public List<BdResPcArgu> getArguList() {
		return arguList;
	}

	public void setArguList(List<BdResPcArgu> arguList) {
		this.arguList = arguList;
	}

	
	
	
	
}
