package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

public class EmpVo extends BaseModule {
	/**
	 * 人员主键
	 */
	@PK
	@Field(value="pk_emp",id=KeyId.UUID)
	private String pkEmp;
	
	/**
	 * 人员编码
	 */
	@Field(value="code_emp")
	private String codeEmp;
	
	/**
	 * 人员名称
	 */
	@Field(value="name_emp")
	private String nameEmp;
	
	private String dtEmpsrvtype;

	public String getDtEmpsrvtype() {
		return dtEmpsrvtype;
	}

	public void setDtEmpsrvtype(String dtEmpsrvtype) {
		this.dtEmpsrvtype = dtEmpsrvtype;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getCodeEmp() {
		return codeEmp;
	}

	public void setCodeEmp(String codeEmp) {
		this.codeEmp = codeEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

}
