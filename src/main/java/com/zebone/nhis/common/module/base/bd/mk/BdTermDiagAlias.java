package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

@Table(value="BD_TERM_DIAG_ALIAS")
public class BdTermDiagAlias extends BaseModule{

	@PK
	@Field(value="PK_DIAGALIAS",id=KeyId.UUID)
	private String pkDiagalias;
	
	@Field(value="PK_DIAG")
    private String pkDiag;
	
	@Field(value="ALIAS")
	private String alias;
	
	@Field(value="SPCODE")
	private String spcode;
	
	@Field(value="D_CODE")
	private String dCode;
	
	@Field(value="DEL_FLAG")
	private String delFlag;

	public String getPkDiagalias() {
		return pkDiagalias;
	}

	public void setPkDiagAlias(String pkDiagalias) {
		this.pkDiagalias = pkDiagalias;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
	
}
