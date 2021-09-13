package com.zebone.nhis.bl.pub.vo;

import java.util.List;

public class PageSettleRecord {
	
	private List<SettleRecord> settleRecordList;
	
	private int totalCount;

	public List<SettleRecord> getSettleRecordList() {
		return settleRecordList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setSettleRecordList(List<SettleRecord> settleRecordList) {
		this.settleRecordList = settleRecordList;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
