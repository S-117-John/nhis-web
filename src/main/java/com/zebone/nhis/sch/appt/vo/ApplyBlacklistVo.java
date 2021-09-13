package com.zebone.nhis.sch.appt.vo;


import java.util.List;

public class ApplyBlacklistVo {
	
	private List<ApplyBlacklist> patiList;
	
	private int totalCount;
	
	public List<ApplyBlacklist> getPatiList() {
		return patiList;
	}

	public void setPatiList(List<ApplyBlacklist> patiList) {
		this.patiList = patiList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
