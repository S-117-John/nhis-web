package com.zebone.nhis.pv.pub.vo;

import java.util.List;

public class PagePvListVo {
private List<PvEncounterListVo> patiList;
	
	private int totalCount;
	
	public List<PvEncounterListVo> getPatiList() {
		return patiList;
	}

	public void setPatiList(List<PvEncounterListVo> patiList) {
		this.patiList = patiList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
