package com.zebone.nhis.pv.pub.vo;

import java.util.List;

public class PageAuditVo {

	private List<PvEnCounterAuditVo> patiList;

	private int totalCount;

	public List<PvEnCounterAuditVo> getPatiList() {
		return patiList;
	}

	public void setPatiList(List<PvEnCounterAuditVo> patiList) {
		this.patiList = patiList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	
}
