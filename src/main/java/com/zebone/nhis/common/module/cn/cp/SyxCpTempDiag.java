package com.zebone.nhis.common.module.cn.cp;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="CP_TEMP_DIAG")
public class SyxCpTempDiag extends BaseModule{

	@PK
	@Field(value="PK_CPDIAG",id=KeyId.UUID)
    private String pkCpdiag;

	@Field(value="PK_CPTEMP")
    private String pkCptemp;

	@Field(value="CODE_ICD")
    private String codeIcd;
	
	@Field(value="NAME_ICD")
	private String nameIcd;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	public String getPkCpdiag() {
		return pkCpdiag;
	}

	public void setPkCpdiag(String pkCpdiag) {
		this.pkCpdiag = pkCpdiag;
	}

	public String getPkCptemp() {
		return pkCptemp;
	}

	public void setPkCptemp(String pkCptemp) {
		this.pkCptemp = pkCptemp;
	}

	public String getCodeIcd() {
		return codeIcd;
	}

	public void setCodeIcd(String codeIcd) {
		this.codeIcd = codeIcd;
	}

	public String getNameIcd() {
		return nameIcd;
	}

	public void setNameIcd(String nameIcd) {
		this.nameIcd = nameIcd;
	}

	public Date getModityTime() {
		return modityTime;
	}

	public void setModityTime(Date modityTime) {
		this.modityTime = modityTime;
	}
	
}
