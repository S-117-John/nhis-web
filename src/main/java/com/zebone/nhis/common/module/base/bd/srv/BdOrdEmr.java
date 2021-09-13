package com.zebone.nhis.common.module.base.bd.srv;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_ORD_EMR  - bd_ord_emr 
 *
 * @since 2016-09-08 03:20:45
 */
@Table(value="BD_ORD_EMR")
public class BdOrdEmr extends BaseModule{
	
	@Field(value="PK_ORDEMR",id=KeyId.UUID)
    private String pkOrdemr;
	
	@Field(value="PK_ORD")
    private String pkOrd;
	
	@Field(value="CODE_EMRTEMP")
    private String codeEmrtemp;
	
	@Field(value="NAME_EMRTEMP")
    private String nameEmrtemp;
	
	@Field(value="FLAG_ACTIVE")
    private String flagActive;
	
	@Field(value="EU_PVTYPE")
	private String euPvtype;
	
	@Field(value="PK_EMRTEMP")
	private String pkEmrtemp;

	public String getPkEmrtemp() {
		return pkEmrtemp;
	}

	public void setPkEmrtemp(String pkEmrtemp) {
		this.pkEmrtemp = pkEmrtemp;
	}

	public String getEuPvtype() {
		return euPvtype;
	}

	public void setEuPvtype(String euPvtype) {
		this.euPvtype = euPvtype;
	}

	public String getPkOrdemr() {
		return pkOrdemr;
	}

	public void setPkOrdemr(String pkOrdemr) {
		this.pkOrdemr = pkOrdemr;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getCodeEmrtemp() {
		return codeEmrtemp;
	}

	public void setCodeEmrtemp(String codeEmrtemp) {
		this.codeEmrtemp = codeEmrtemp;
	}

	public String getNameEmrtemp() {
		return nameEmrtemp;
	}

	public void setNameEmrtemp(String nameEmrtemp) {
		this.nameEmrtemp = nameEmrtemp;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}
	
	
}
