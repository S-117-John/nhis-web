package com.zebone.nhis.base.drg.vo;

import java.util.List;
import java.util.Map;

public class BdCchiItemVo {
	private List<Map<String,Object>> cchiItemList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public List<Map<String, Object>> getCchiItemList() {
		return cchiItemList;
	}
	public void setCchiItemList(List<Map<String, Object>> cchiItemList) {
		this.cchiItemList = cchiItemList;
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
