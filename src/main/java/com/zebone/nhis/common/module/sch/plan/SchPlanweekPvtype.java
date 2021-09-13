package com.zebone.nhis.common.module.sch.plan;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: SCH_PLANWEEK_PVTYPE - sch_planweek_pvtype 
 *
 * @since 2016-10-10 02:53:49
 */
@Table(value="SCH_PLANWEEK_PVTYPE")
public class SchPlanweekPvtype extends BaseModule  {

    /** PK_PLANPVTYPE - 主要对基于医嘱的医技科室预约有效 */
	@PK
	@Field(value="PK_PLANPVTYPE",id=KeyId.UUID)
    private String pkPlanpvtype;

	@Field(value="PK_PLANWEEK")
    private String pkPlanweek;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检，5 家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="CNT_APPT")
    private Integer cntAppt;

    /** TICKETRULES - 定义就诊类型下的号票规则，受数量_可预约的控制 */
	@Field(value="TICKETRULES")
    private String ticketrules;


    public String getPkPlanpvtype(){
        return this.pkPlanpvtype;
    }
    public void setPkPlanpvtype(String pkPlanpvtype){
        this.pkPlanpvtype = pkPlanpvtype;
    }

    public String getPkPlanweek(){
        return this.pkPlanweek;
    }
    public void setPkPlanweek(String pkPlanweek){
        this.pkPlanweek = pkPlanweek;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public Integer getCntAppt(){
        return this.cntAppt;
    }
    public void setCntAppt(Integer cntAppt){
        this.cntAppt = cntAppt;
    }

    public String getTicketrules(){
        return this.ticketrules;
    }
    public void setTicketrules(String ticketrules){
        this.ticketrules = ticketrules;
    }
}