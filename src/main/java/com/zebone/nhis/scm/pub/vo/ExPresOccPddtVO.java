package com.zebone.nhis.scm.pub.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExPresOccPddt;

public class ExPresOccPddtVO extends  ExPresOccPddt {
	
	private String pkCnord;
	
	private String pkPv;
	
	public String getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(String pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	
}