package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrdHerb;

public class SyxOpHerbVo extends CnOrdHerb{
	
	private String name;
	private String flagAcc;
	private String flagSettle;
    private String unit;
    
    
	public String getFlagAcc() {
		return flagAcc;
	}

	public void setFlagAcc(String flagAcc) {
		this.flagAcc = flagAcc;
	}

	public String getFlagSettle() {
		return flagSettle;
	}

	public void setFlagSettle(String flagSettle) {
		this.flagSettle = flagSettle;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
