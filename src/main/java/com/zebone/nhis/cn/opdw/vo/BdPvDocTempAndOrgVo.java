package com.zebone.nhis.cn.opdw.vo;

import java.util.Date;

import com.zebone.nhis.common.module.cn.opdw.BdPvDocTemp;
import com.zebone.platform.modules.dao.build.au.Field;

public class BdPvDocTempAndOrgVo extends BdPvDocTemp{

	private String pkTemporg;

    private String pkOrgUse;

    private String pkDept;
    
	private String flagEmr;
    
    private String flagDef;
    
    private int euTmpLevel;

    private  String flagDefault;
    
    public String getFlagEmr() {
		return flagEmr;
	}

	public void setFlagEmr(String flagEmr) {
		this.flagEmr = flagEmr;
	}

	public String getFlagDef() {
		return flagDef;
	}

	public void setFlagDef(String flagDef) {
		this.flagDef = flagDef;
	}
	
	public String getPkTemporg() {
		return pkTemporg;
	}

	public void setPkTemporg(String pkTemporg) {
		this.pkTemporg = pkTemporg;
	}

	public String getPkOrgUse() {
		return pkOrgUse;
	}

	public void setPkOrgUse(String pkOrgUse) {
		this.pkOrgUse = pkOrgUse;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public int getEuTmpLevel() {
		return euTmpLevel;
	}

	public void setEuTmpLevel(int euTmpLevel) {
		this.euTmpLevel = euTmpLevel;
	}

	public String getFlagDefault() {
		return flagDefault;
	}

	public void setFlagDefault(String flagDefault) {
		this.flagDefault = flagDefault;
	}
}
