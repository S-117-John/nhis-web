package com.zebone.nhis.pro.zsrm.bl.vo;

import com.zebone.nhis.pv.pub.vo.PvEncounterListVo;

import java.util.List;
import java.util.Map;

public class PageMedListVo {
    private List<Map<String, Object>> MedList;
	
	private int totalCount;

	public List<Map<String, Object>> getMedList() {
		return MedList;
	}

	public void setMedList(List<Map<String, Object>> medList) {
		MedList = medList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
}
