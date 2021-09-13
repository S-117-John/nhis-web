package com.zebone.nhis.common.module.sch.plan;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_PLAN_WEEK  - sch_plan_week 
 *
 * @since 2016-09-18 10:38:57
 */
@Table(value="SCH_PLAN_WEEK")
public class SchPlanWeek extends BaseModule  {

	@PK
	@Field(value="PK_PLANWEEK",id=KeyId.UUID)
    private String pkPlanweek;

	@Field(value="PK_SCHPLAN")
    private String pkSchplan;

	@Field(value="WEEK_NO")
    private Long weekNo;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="CNT_TOTAL")
    private Long cntTotal;

	@Field(value="CNT_APPT")
    private Long cntAppt;

	@Field(value="CNT_ADD")
    private Long cntAdd;
	
	@Field(value="PK_DEPTUNIT")
	private String pkDeptunit;
	
	
	private int cntStd;

    public int getCntStd() {
		return cntStd;
	}
	public void setCntStd(int cntStd) {
		this.cntStd = cntStd;
	}
	public String getPkPlanweek(){
        return this.pkPlanweek;
    }
    public void setPkPlanweek(String pkPlanweek){
        this.pkPlanweek = pkPlanweek;
    }

    public String getPkSchplan(){
        return this.pkSchplan;
    }
    public void setPkSchplan(String pkSchplan){
        this.pkSchplan = pkSchplan;
    }

    public Long getWeekNo(){
        return this.weekNo;
    }
    public void setWeekNo(Long weekNo){
        this.weekNo = weekNo;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
    }

    public Long getCntTotal(){
        return this.cntTotal;
    }
    public void setCntTotal(Long cntTotal){
        this.cntTotal = cntTotal;
    }

    public Long getCntAppt(){
        return this.cntAppt;
    }
    public void setCntAppt(Long cntAppt){
        this.cntAppt = cntAppt;
    }

    public Long getCntAdd(){
        return this.cntAdd;
    }
    public void setCntAdd(Long cntAdd){
        this.cntAdd = cntAdd;
    }
	public String getPkDeptunit() {
		return pkDeptunit;
	}
	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}
	

    
}