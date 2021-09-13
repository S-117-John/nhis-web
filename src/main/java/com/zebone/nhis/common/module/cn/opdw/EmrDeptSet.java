package com.zebone.nhis.common.module.cn.opdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;

/**
 * @author liH
 * @version 1.0
 * @date 2021/3/23 10:30
 * @description
 * @currentMinute zebone_CZ
 */
@Table(value="EMR_DEPT_SET")
public class EmrDeptSet extends BaseModule {
	@PK
	@Field(value="PK_DEPT_SET",id= KeyId.UUID)
	private String pkDeptSet;

	@Field(value="PK_DEPT")
	private String pkDept;

	@Field(value="CODE")
	private String code;

	@Field(value="NAME")
	private String name;

	@Field(value="PY_CODE")
	private String pyCode;

	@Field(value="D_CODE")
	private String dCode;

	@Field(value="SET_PK")
	private String setPk;

	@Field(value="SET_CODE")
	private String setCode;

	@Field(value="SET_NAME")
	private String setName;

	@Field(value="REMARK")
	private String remark;

	public String getPkDeptSet() {
		return pkDeptSet;
	}

	public void setPkDeptSet(String pkDeptSet) {
		this.pkDeptSet = pkDeptSet;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getSetPk() {
		return setPk;
	}

	public void setSetPk(String setPk) {
		this.setPk = setPk;
	}

	public String getSetCode() {
		return setCode;
	}

	public void setSetCode(String setCode) {
		this.setCode = setCode;
	}

	public String getSetName() {
		return setName;
	}

	public void setSetName(String setName) {
		this.setName = setName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
