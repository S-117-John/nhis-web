package com.zebone.nhis.labor.nis.vo;

import java.util.List;

import com.zebone.nhis.common.module.labor.nis.PvLaborInstru;
import com.zebone.nhis.common.module.labor.nis.PvLaborInstruDt;

public class PvLaborInstruVo extends PvLaborInstru {
	private List<PvLaborInstruDt> dtlist;
	private String flagInit;//初始化

	public String getFlagInit() {
		return flagInit;
	}

	public void setFlagInit(String flagInit) {
		this.flagInit = flagInit;
	}

	public List<PvLaborInstruDt> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<PvLaborInstruDt> dtlist) {
		this.dtlist = dtlist;
	}

}
