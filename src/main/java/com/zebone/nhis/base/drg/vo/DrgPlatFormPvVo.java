package com.zebone.nhis.base.drg.vo;

import java.util.List;
import java.util.Map;

/**
 * drg平台用-患者信息列表带分页
 * @author dell
 *
 */
public class DrgPlatFormPvVo {
	private List<Map<String,Object>> pvEncounterList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	public List<Map<String, Object>> getPvEncounterList() {
		return pvEncounterList;
	}
	public void setPvEncounterList(List<Map<String, Object>> pvEncounterList) {
		this.pvEncounterList = pvEncounterList;
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
