package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.opdw.PvDoc;

public class CnOpPvDocVo extends PvDoc{
	private String tempName;

	public String getTempName() {
		return tempName;
	}

	public void setTempName(String tempName) {
		this.tempName = tempName;
	}
	private String pkDept;

	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

}
