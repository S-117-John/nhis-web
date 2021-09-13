package com.zebone.nhis.sch.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.sch.plan.SchPlanWeek;
import com.zebone.nhis.common.module.sch.plan.SchPlanWeekDt;
import com.zebone.nhis.common.module.sch.plan.SchPlanweekPvtype;

public class SchPlanWeekExt extends SchPlanWeek {

	/**bd_code_dateslot表的日期分组序号*/
	private int sortno;
	
	private String nameDeptUnit;

	/** 周计划明细*/
	private List<SchPlanWeekDt> listPlanWeekDt;
	
	/** 被删除的主键*/
	private String pkPlanweekDel;
	
	public String getNameDeptUnit() {
		return nameDeptUnit;
	}

	public void setNameDeptUnit(String nameDeptUnit) {
		this.nameDeptUnit = nameDeptUnit;
	}
	/**
	 * 排班计划-医技预约流程
	 */
	private List<SchPlanweekPvtype> pvtypeList = new ArrayList<SchPlanweekPvtype>();
	
	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public List<SchPlanweekPvtype> getPvtypeList() {
		return pvtypeList;
	}

	public void setPvtypeList(List<SchPlanweekPvtype> pvtypeList) {
		this.pvtypeList = pvtypeList;
	}

	public List<SchPlanWeekDt> getListPlanWeekDt() {
		return listPlanWeekDt;
	}

	public void setListPlanWeekDt(List<SchPlanWeekDt> listPlanWeekDt) {
		this.listPlanWeekDt = listPlanWeekDt;
	}

	public String getPkPlanweekDel() {
		return pkPlanweekDel;
	}

	public void setPkPlanweekDel(String pkPlanweekDel) {
		this.pkPlanweekDel = pkPlanweekDel;
	}



	
}
