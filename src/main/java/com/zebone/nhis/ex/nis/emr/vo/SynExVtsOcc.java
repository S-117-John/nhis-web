package com.zebone.nhis.ex.nis.emr.vo;

import java.util.Map;

import com.zebone.nhis.common.module.ex.nis.emr.ExVtsOcc;

public class SynExVtsOcc extends ExVtsOcc{

	private Map<String,Object> synParam;

	public Map<String, Object> getSynParam() {
		return synParam;
	}

	public void setSynParam(Map<String, Object> synParam) {
		this.synParam = synParam;
	}
	
}
