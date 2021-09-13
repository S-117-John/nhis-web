package com.zebone.nhis.ex.oi.vo;

import com.zebone.nhis.common.module.ex.oi.BdDeptIv;

public class BdDeptIvVO extends BdDeptIv {
	public String nameDept;
	private String flagAlertView;
	private String flagProView;

	public String getFlagAlertView() {
		return flagAlertView;
	}

	public void setFlagAlertView(String flagAlertView) {
		this.flagAlertView = flagAlertView;
	}

	public String getFlagProView() {
		return flagProView;
	}

	public void setFlagProView(String flagProView) {
		this.flagProView = flagProView;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

}
