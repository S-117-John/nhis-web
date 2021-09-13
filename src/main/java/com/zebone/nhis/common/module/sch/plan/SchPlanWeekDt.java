package com.zebone.nhis.common.module.sch.plan;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: SCH_PLANWEEK_DT 
 * 排班预约-排班周计划-明细
 * @since 2019-07-04
 */
@Table(value="SCH_PLANWEEK_DT")
public class SchPlanWeekDt extends BaseModule {

	@PK
	@Field(value="pk_planweekdt",id=KeyId.UUID)
    private String pkPlanweekdt;
	
	@Field(value="PK_PLANWEEK")
    private String pkPlanweek;
	
	@Field(value="pk_dateslot")
    private String pkDateslot;
	
	@Field(value="time_begin")
    private String timeBegin;
	
	@Field(value="time_end")
    private String timeEnd;
	
	@Field(value="cnt")
    private Integer cnt;
	
	@Field(value="cnt_appt")
    private Integer cntAppt;
	
	@Field(value="DT_APPTYPE")
    private String dtApptype;

	public String getPkPlanweekdt() {
		return pkPlanweekdt;
	}

	public void setPkPlanweekdt(String pkPlanweekdt) {
		this.pkPlanweekdt = pkPlanweekdt;
	}

	public String getPkPlanweek() {
		return pkPlanweek;
	}

	public void setPkPlanweek(String pkPlanweek) {
		this.pkPlanweek = pkPlanweek;
	}

	public String getPkDateslot() {
		return pkDateslot;
	}

	public void setPkDateslot(String pkDateslot) {
		this.pkDateslot = pkDateslot;
	}

	public String getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin(String timeBegin) {
		this.timeBegin = timeBegin;
	}

	public String getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(String timeEnd) {
		this.timeEnd = timeEnd;
	}

	public Integer getCnt() {
		return cnt;
	}

	public void setCnt(Integer cnt) {
		this.cnt = cnt;
	}

	public Integer getCntAppt() {
		return cntAppt;
	}

	public void setCntAppt(Integer cntAppt) {
		this.cntAppt = cntAppt;
	}

	public String getDtApptype() {
		return dtApptype;
	}

	public void setDtApptype(String dtApptype) {
		this.dtApptype = dtApptype;
	}
	
	
	
}
