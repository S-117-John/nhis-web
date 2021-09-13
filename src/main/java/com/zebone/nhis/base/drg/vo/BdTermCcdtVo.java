package com.zebone.nhis.base.drg.vo;

import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.base.bd.drg.BdTermCcdt;
import com.zebone.platform.modules.dao.build.au.Table;




public class BdTermCcdtVo  {
	
	private List<Map<String,Object>> termCcdtList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	
	public List<Map<String, Object>> getTermCcdtList() {
		return termCcdtList;
	}
	public void setTermCcdtList(List<Map<String, Object>> termCcdtList) {
		this.termCcdtList = termCcdtList;
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