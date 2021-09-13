package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import java.util.Map;





public class CnChiefResidentVo  {
	
	private List<Map<String,Object>> cnChiefResidentVoList;
	private int totalCount;
	/** 每页行数 */
	private int pageSize;
	/** 页码 */
	private int pageIndex;
	
	
	public List<Map<String, Object>> getCnChiefResidentVoList() {
		return cnChiefResidentVoList;
	}
	public void setCnChiefResidentVoList(
			List<Map<String, Object>> cnChiefResidentVoList) {
		this.cnChiefResidentVoList = cnChiefResidentVoList;
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