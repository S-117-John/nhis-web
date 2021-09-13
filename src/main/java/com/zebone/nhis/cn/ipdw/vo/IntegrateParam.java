package com.zebone.nhis.cn.ipdw.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.cn.ipdw.CnOrder;

public class IntegrateParam {
	
	private List<String>  stringList = new ArrayList<String>();
	private Map<String,Object>  unListParamMap=new HashMap<String,Object> ();
	
	//医嘱列表
	private List<CnOrder>  ordList = new ArrayList<CnOrder>();
	
	public List<CnOrder> getOrdList() {
		return ordList;
	}
	public void setOrdList(List<CnOrder> ordList) {
		this.ordList = ordList;
	}
	public Map<String, Object> getUnListParamMap() {
		return unListParamMap;
	}
	public void setUnListParamMap(Map<String, Object> unListParamMap) {
		this.unListParamMap = unListParamMap;
	}

}
