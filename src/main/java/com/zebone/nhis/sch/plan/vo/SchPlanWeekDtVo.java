package com.zebone.nhis.sch.plan.vo;

import com.zebone.nhis.common.module.sch.plan.SchPlanWeekDt;

public class SchPlanWeekDtVo extends SchPlanWeekDt {
	private String pkDeptunit;
	
	private String flagUsed;
	
	private String oldDtapptype;
	
	private String addOrUpd;//修改或新增标识

	private String pkSch;

	public String getPkSch() {
		return pkSch;
	}

	public void setPkSch(String pkSch) {
		this.pkSch = pkSch;
	}

	public String getPkDeptunit() {
		return pkDeptunit;
	}

	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}

	public String getFlagUsed() {
		return flagUsed;
	}

	public void setFlagUsed(String flagUsed) {
		this.flagUsed = flagUsed;
	}

	public String getOldDtapptype() {
		return oldDtapptype;
	}

	public void setOldDtapptype(String oldDtapptype) {
		this.oldDtapptype = oldDtapptype;
	}

	public String getAddOrUpd() {
		return addOrUpd;
	}

	public void setAddOrUpd(String addOrUpd) {
		this.addOrUpd = addOrUpd;
	}

}
