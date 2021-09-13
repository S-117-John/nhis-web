package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.List;
import java.util.Map;

public class BdItemAndCount {

	private int totalCount;

	//收费实体集合
	private List<Map<String,Object>> bdItemInfoList;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Map<String, Object>> getBdItemInfoList() {
		return bdItemInfoList;
	}

	public void setBdItemInfoList(List<Map<String, Object>> bdItemInfoList) {
		this.bdItemInfoList = bdItemInfoList;
	}







}
