package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.wf.BdAdtConfig;

public class AdtConfigVo {
	
	private List<BdAdtConfig> dtList ;
	
	private String delstr ;

	public List<BdAdtConfig> getDtList() {
		return dtList;
	}

	public void setDtList(List<BdAdtConfig> dtList) {
		this.dtList = dtList;
	}

	public String getDelstr() {
		return delstr;
	}

	public void setDelstr(String delstr) {
		this.delstr = delstr;
	}
	

}
