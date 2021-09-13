package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.List;
import java.util.Map;

public class SzYbDrugContrastInfoAndCount {

	private int totalCount;

	//医保目录对照集合
	private List<Map<String,Object>> szYbDrugContrastInfo;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Map<String, Object>> getSzYbDrugContrastInfo() {
		return szYbDrugContrastInfo;
	}

	public void setSzYbDrugContrastInfo(List<Map<String, Object>> szYbDrugContrastInfo) {
		this.szYbDrugContrastInfo = szYbDrugContrastInfo;
	}




}
