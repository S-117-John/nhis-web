package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.nhis.common.module.compay.ins.syx.InsDeptMap;
import com.zebone.platform.modules.dao.build.au.Field;

public class InsDeptMapDataVo extends InsDeptMap{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Field(value = "CODE_DEPT")
	private String codeDept;
	@Field(value = "PY_CODE")
	private String pyCode;
	@Field(value = "NAME_DEPT")
	private String nameDept;
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getPyCode() {
		return pyCode;
	}
	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}
	public String getNameDept() {
		return nameDept;
	}
	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
	}

}
