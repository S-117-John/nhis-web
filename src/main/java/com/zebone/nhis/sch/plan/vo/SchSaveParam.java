package com.zebone.nhis.sch.plan.vo;

import java.util.List;

import com.zebone.nhis.common.module.sch.plan.SchEmp;
import com.zebone.nhis.common.module.sch.plan.SchPvtype;
import com.zebone.nhis.common.module.sch.plan.SchSch;

public class SchSaveParam {
	
	private SchSch schSch;
	
	private List<SchEmp> schEmps;
	
	private List<SchPvtype> schPvtypes;
	
	private String notAutoModifyOfTotalCAndBookCFlag;

	/** 周计划明细*/
	private List<SchPlanWeekDtVo> listPlanWeekDt;
	
	//排班计划医生主键
	private String pkEmp;

	public List<SchEmp> getSchEmps() {
		return schEmps;
	}

	public void setSchEmps(List<SchEmp> schEmps) {
		this.schEmps = schEmps;
	}

	public List<SchPvtype> getSchPvtypes() {
		return schPvtypes;
	}

	public void setSchPvtypes(List<SchPvtype> schPvtypes) {
		this.schPvtypes = schPvtypes;
	}

	public List<SchPlanWeekDtVo> getListPlanWeekDt() {
		return listPlanWeekDt;
	}

	public void setListPlanWeekDt(List<SchPlanWeekDtVo> listPlanWeekDt) {
		this.listPlanWeekDt = listPlanWeekDt;
	}	

	public String getNotAutoModifyOfTotalCAndBookCFlag() {
		return notAutoModifyOfTotalCAndBookCFlag;
	}

	public void setNotAutoModifyOfTotalCAndBookCFlag(String notAutoModifyOfTotalCAndBookCFlag) {
		this.notAutoModifyOfTotalCAndBookCFlag = notAutoModifyOfTotalCAndBookCFlag;
	}
	
	public SchSch getSchSch() {
		return schSch;
	}

	public void setSchSch(SchSch schSch) {
		this.schSch = schSch;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

}
