package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CP_TEMP_DEPT")
public class SyxCpTempDept extends BaseModule{

	@PK
	@Field(value="PK_CPDEPT",id=KeyId.UUID)
    private String pkCpdept;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="PK_ORG_USE")
    private String pkOrgUse;

	@Field(value="PK_DEPT_USE")
    private String pkDeptUse;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	public String getPkCpdept() {
		return pkCpdept;
	}

	public void setPkCpdept(String pkCpdept) {
		this.pkCpdept = pkCpdept;
	}

	public String getPkCptemp() {
		return pkCptemp;
	}

	public void setPkCptemp(String pkCptemp) {
		this.pkCptemp = pkCptemp;
	}

	public String getPkOrgUse() {
		return pkOrgUse;
	}

	public void setPkOrgUse(String pkOrgUse) {
		this.pkOrgUse = pkOrgUse;
	}

	public String getPkDeptUse() {
		return pkDeptUse;
	}

	public void setPkDeptUse(String pkDeptUse) {
		this.pkDeptUse = pkDeptUse;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
}
