package com.zebone.nhis.common.module.compay.ins.shenzhen;

import java.util.List;
import java.util.Map;

public class SzYbDiagnosisContrastInfoAndCount {


	private int totalCount;

	//医保目录对照集合
	private List<Map<String,Object>> szYbDiagnosisContrastInfo;


	public int getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}


	public List<Map<String, Object>> getSzYbDiagnosisContrastInfo() {
		return szYbDiagnosisContrastInfo;
	}


	public void setSzYbDiagnosisContrastInfo(List<Map<String, Object>> szYbDiagnosisContrastInfo) {
		this.szYbDiagnosisContrastInfo = szYbDiagnosisContrastInfo;
	}






}
