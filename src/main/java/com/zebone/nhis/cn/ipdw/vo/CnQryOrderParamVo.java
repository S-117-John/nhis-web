package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;
import java.util.Map;

public class CnQryOrderParamVo{
	
	private List<String> pkPdList;
	
	private Map<String, Object> paramMap;
	
	public List<String> getPkPdList() {
		return pkPdList;
	}

	public void setPkPdList(List<String> pkPdList) {
		this.pkPdList = pkPdList;
	}

	public Map<String, Object> getParamMap() {
		return paramMap;
	}

	public void setParamMap(Map<String, Object> paramMap) {
		this.paramMap = paramMap;
	}

}
