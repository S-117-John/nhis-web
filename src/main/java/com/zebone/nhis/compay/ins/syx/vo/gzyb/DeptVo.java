package com.zebone.nhis.compay.ins.syx.vo.gzyb;

import com.zebone.platform.modules.dao.build.au.Field;

public class DeptVo {

	/**
	 * 部门主键
	 */
	@Field(value="PK_DEPT")
	private String pkDept;
	/**
	 * 部门编码
	 */
	@Field(value="CODE_DEPT")
	private String codeDept;
	
	/**
	 * 部门名称
	 */
	@Field(value="NAME_DEPT")
	private String nameDept;
	/**
	 * 旧ID
	 */
	@Field(value="OLD_ID")
	private String oldId;
	/**
	 * 旧编码
	 */
	@Field(value="OLD_CODE")
	private String oldCode;
	
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
	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public String getOldCode() {
		return oldCode;
	}
	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}
	
}
