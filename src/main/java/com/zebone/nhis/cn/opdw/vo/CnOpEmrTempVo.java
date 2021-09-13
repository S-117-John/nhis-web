package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.opdw.BdOpEmrTemp;

public class CnOpEmrTempVo extends BdOpEmrTemp {
	private String cateName;
	private String DiagName;

	public String getCateName() {
		return cateName;
	}
	
	public void setCateName(String cateName) {
		this.cateName = cateName;
	}

	public String getDiagName() {
		return DiagName;
	}

	public void setDiagName(String diagName) {
		DiagName = diagName;
	}
}
