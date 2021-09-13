package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_TASK_DT 
 *
 * @since 2019-05-13 
 */
@Table(value="BD_CP_TASK_DT")
public class BdCpTaskDt extends BaseModule  {

	@PK
	@Field(value="PK_CPTASKDT",id=KeyId.UUID)
    private String pkCptaskdt;

	@Field(value="PK_CPTASK")
    private String pkCptask;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="SPEC")
    private String spec;

	@Field(value="FLAG_DEF")
    private String flagDef;

	public String getPkCptaskdt() {
		return pkCptaskdt;
	}

	public void setPkCptaskdt(String pkCptaskdt) {
		this.pkCptaskdt = pkCptaskdt;
	}

	public String getPkCptask() {
		return pkCptask;
	}

	public void setPkCptask(String pkCptask) {
		this.pkCptask = pkCptask;
	}

	public String getPkOrd() {
		return pkOrd;
	}

	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}

	public String getCodeOrd() {
		return codeOrd;
	}

	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}

	public String getNameOrd() {
		return nameOrd;
	}

	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getFlagDef() {
		return flagDef;
	}

	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}

	
}