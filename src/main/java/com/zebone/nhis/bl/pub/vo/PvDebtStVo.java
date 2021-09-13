package com.zebone.nhis.bl.pub.vo;

import java.util.List;
import java.util.Map;

public class PvDebtStVo {
	private Map<String,Object> patiVo;
	
	private List<Map<String,Object>> cateVo;
	
	private List<Map<String,Object>> perpVo;

	public Map<String, Object> getPatiVo() {
		return patiVo;
	}

	public void setPatiVo(Map<String, Object> patiVo) {
		this.patiVo = patiVo;
	}

	public List<Map<String, Object>> getCateVo() {
		return cateVo;
	}

	public void setCateVo(List<Map<String, Object>> cateVo) {
		this.cateVo = cateVo;
	}

	public List<Map<String, Object>> getPerpVo() {
		return perpVo;
	}

	public void setPerpVo(List<Map<String, Object>> perpVo) {
		this.perpVo = perpVo;
	}
	
	
}
