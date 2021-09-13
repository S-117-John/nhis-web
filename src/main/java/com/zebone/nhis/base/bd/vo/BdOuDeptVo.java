package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.ou.BdOuDept;

public class BdOuDeptVo extends BdOuDept {
	/**
	 * 自定义科室
	 */
	private String pkDefdept;
	
	private String flagChecked;
	
	
	public String getFlagChecked() {
		return flagChecked;
	}

	public void setFlagChecked(String flagChecked) {
		this.flagChecked = flagChecked;
	}

	public String getPkDefdept() {
		return pkDefdept;
	}

	public void setPkDefdept(String pkDefdept) {
		this.pkDefdept = pkDefdept;
	}
	
	
}
