package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DiagParam {
	private List<Map<String,Object>> pvDiagList = new ArrayList<Map<String,Object>>();
	private List<Map<String,Object>> cnDiagList = new ArrayList<Map<String,Object>>();
	public List<Map<String, Object>> getPvDiagList() {
		return pvDiagList;
	}
	public void setPvDiagList(List<Map<String, Object>> pvDiagList) {
		this.pvDiagList = pvDiagList;
	}
	public List<Map<String, Object>> getCnDiagList() {
		return cnDiagList;
	}
	public void setCnDiagList(List<Map<String, Object>> cnDiagList) {
		this.cnDiagList = cnDiagList;
	}
}
