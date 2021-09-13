package com.zebone.nhis.base.pub.vo;

import java.util.List;
import java.util.Map;

public class DumpPubSearchVo {
	private List<Map<String,Object>> dataList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public List<Map<String, Object>> getDataList() {
		return dataList;
	}
	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	
}
