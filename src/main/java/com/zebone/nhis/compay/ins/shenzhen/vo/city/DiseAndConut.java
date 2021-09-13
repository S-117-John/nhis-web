package com.zebone.nhis.compay.ins.shenzhen.vo.city;

import java.util.List;
import java.util.Map;

public class DiseAndConut {

	private int totalCount;

	private List<Map<String,Object>> diseList;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Map<String, Object>> getDiseList() {
		return diseList;
	}

	public void setDiseList(List<Map<String, Object>> diseList) {
		this.diseList = diseList;
	}

}
