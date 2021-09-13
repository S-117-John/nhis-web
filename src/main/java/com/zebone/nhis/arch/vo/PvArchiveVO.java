package com.zebone.nhis.arch.vo;

import com.zebone.nhis.common.module.arch.PvArchive;

public class PvArchiveVO extends PvArchive {
	
	private String codePv;

	private String flagForce;

	private String pkComt;

	public String getCodePv() {
		return codePv;
	}

	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}

	public String getFlagForce() {
		return flagForce;
	}

	public void setFlagForce(String flagForce) {
		this.flagForce = flagForce;
	}

	public String getPkComt() {
		return pkComt;
	}

	public void setPkComt(String pkComt) {
		this.pkComt = pkComt;
	}
}
