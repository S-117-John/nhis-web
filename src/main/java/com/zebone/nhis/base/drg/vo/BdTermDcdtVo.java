package com.zebone.nhis.base.drg.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.drg.BdTermDcdt;

public class BdTermDcdtVo extends BdTermDcdt{
	private List<Map<String,Object>> termDcdtList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public List<Map<String, Object>> getTermDcdtList() {
		return termDcdtList;
	}
	public void setTermDcdtList(List<Map<String, Object>> termDcdtList) {
		this.termDcdtList = termDcdtList;
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
