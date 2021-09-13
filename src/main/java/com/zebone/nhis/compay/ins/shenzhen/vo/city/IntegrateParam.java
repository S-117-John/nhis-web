package com.zebone.nhis.compay.ins.shenzhen.vo.city;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.compay.ins.shenzhen.InsSzybCitycg;

public class IntegrateParam {
	
	private Map<String,Object>  unListParamMap=new HashMap<String,Object> ();
	private List<InsSzybCitycg>  insSzybCitycgList = new ArrayList<InsSzybCitycg>();
	
	
	public Map<String, Object> getUnListParamMap() {
		return unListParamMap;
	}
	public void setUnListParamMap(Map<String, Object> unListParamMap) {
		this.unListParamMap = unListParamMap;
	}
	public List<InsSzybCitycg> getInsSzybCitycgList() {
		return insSzybCitycgList;
	}
	public void setInsSzybCitycgList(List<InsSzybCitycg> insSzybCitycgList) {
		this.insSzybCitycgList = insSzybCitycgList;
	}
	
	
}
