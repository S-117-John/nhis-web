package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlDepositPi;

public class BlDepositPiParam extends BlDepositPi{
	
	private String pkPv;
	private String euOptype;
	private List<String> pkCnord;
	private String flagYp;
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getEuOptype() {
		return euOptype;
	}
	public void setEuOptype(String euOptype) {
		this.euOptype = euOptype;
	}
	public List<String> getPkCnord() {
		return pkCnord;
	}
	public void setPkCnord(List<String> pkCnord) {
		this.pkCnord = pkCnord;
	}
	public String getFlagYp() {
		return flagYp;
	}
	public void setFlagYp(String flagYp) {
		this.flagYp = flagYp;
	}
	
	
	
}
