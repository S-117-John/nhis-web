package com.zebone.nhis.sch.pub.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.sch.plan.SchPlanEmp;

public class SchPlanAndEmpsWeeksParam {

	/**
	 * 排班计划
	 */
	private SchPlanVo schPlan = new SchPlanVo();
	
	private String pkDeptBelong;
	/**
	 * 排班计划-人员
	 */
	private List<SchPlanEmp> empList = new ArrayList<SchPlanEmp>();
	/**
	 * 排班计划-周定义
	 */
	private List<SchPlanWeekExt> weekList = new ArrayList<SchPlanWeekExt>();
	
	
	public String getPkDeptBelong() {
		return pkDeptBelong;
	}
	public void setPkDeptBelong(String pkDeptBelong) {
		this.pkDeptBelong = pkDeptBelong;
	}
	public SchPlanVo getSchPlan() {
		return schPlan;
	}
	public void setSchPlan(SchPlanVo schPlan) {
		this.schPlan = schPlan;
	}
	public List<SchPlanEmp> getEmpList() {
		return empList;
	}
	public void setEmpList(List<SchPlanEmp> empList) {
		this.empList = empList;
	}
	public List<SchPlanWeekExt> getWeekList() {
		return weekList;
	}
	public void setWeekList(List<SchPlanWeekExt> weekList) {
		this.weekList = weekList;
	}
}
