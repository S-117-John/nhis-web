package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdQcPlatform;

public class BdQcPlatformVo extends BdQcPlatform {

	private static final long serialVersionUID = 1L;

	private String pkDept;
	
	private String codeDept;
	
	private String nameDept;

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getCodeDept() {
		return codeDept;
	}

	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}

	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}
	
	
}
