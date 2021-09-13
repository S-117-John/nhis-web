package com.zebone.nhis.common.module.sch.plan;


import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: SCH_PLAN  - sch_plan 
 *
 * @since 2016-09-18 10:37:49
 */
@Table(value="SCH_PLAN")
public class SchPlan extends BaseModule  {

	@PK
	@Field(value="PK_SCHPLAN",id=KeyId.UUID)
    private String pkSchplan;

    /** EU_SCHCLASS - 0 门诊出诊；1 医技排班；2 床位预约；3 手术排班 */
	@Field(value="EU_SCHCLASS")
    private String euSchclass;

	@Field(value="PK_SCHRES")
    private String pkSchres;

    /** PK_SCHSRV - 由排班资源中引入默认值 */
	@Field(value="PK_SCHSRV")
    private String pkSchsrv;
		
	@Field(value="PK_DEPTUNIT")
    private String pkDeptunit;

	@Field(value="CODE")
    private String code;

	@Field(value="NAME")
    private String name;
	
	@Field(value="PK_ORGAREA")
    private String pkOrgArea;
	
	@Field(value="PK_DEPT_PLAN")
    private String pkDeptPlan;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="PK_WORKCALENDAR")
    private String pkWorkcalendar;

    /** EU_RECURTYPE - 0 单周；1 双周 ；2 月；3 季度；9 年 */
	@Field(value="EU_RECURTYPE")
    private String euRecurtype;

    /** DT_DATESLOTTYPE - 取默认日期分组 */
	@Field(value="DT_DATESLOTTYPE")
    private String dtDateslottype;

    /** PK_TICKETRULES - 由排班资源中引入默认值 */
	@Field(value="PK_TICKETRULES")
    private String pkTicketrules;

    /** MINUTE_PER - 由排班资源中引入默认值 */
	@Field(value="MINUTE_PER")
    private Long minutePer;

    /** PK_DEPT - 计划所属部门 */
	@Field(value="PK_DEPT")
    private String pkDept;

    /** FLAG_TICKET - 是否产生sch_ticket记录 */
	@Field(value="FLAG_TICKET")
    private String flagTicket;

	@Field(value="FLAG_STOP")
    private String flagStop;	
	
    @Field(value="CNT_SPEC")
    private String cntSpec;

	@Field(value="NOTE")
    private String note;
	
	@Field(value="EU_APPTTYPE")
	private String euAppttype;

	@Field(value="CYCLE_PLAN")
	private Long cyclePlan;

	@Field(value="DATE_END")
	private Date dateEnd;
	
//	private String cntStd;
//	
//    public String getCntStd() {
//		return cntStd;
//	}
//	public void setCntStd(String cntStd) {
//		this.cntStd = cntStd;
//	}


    public Long getCyclePlan() {
        return cyclePlan;
    }

    public void setCyclePlan(Long cyclePlan) {
        this.cyclePlan = cyclePlan;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getPkSchplan(){
        return this.pkSchplan;
    }
    public void setPkSchplan(String pkSchplan){
        this.pkSchplan = pkSchplan;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
    }

    public String getPkSchres(){
        return this.pkSchres;
    }
    public void setPkSchres(String pkSchres){
        this.pkSchres = pkSchres;
    }

    public String getPkSchsrv(){
        return this.pkSchsrv;
    }
    public void setPkSchsrv(String pkSchsrv){
        this.pkSchsrv = pkSchsrv;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getSpcode(){
        return this.spcode;
    }
    public void setSpcode(String spcode){
        this.spcode = spcode;
    }

    public String getdCode(){
        return this.dCode;
    }
    public void setdCode(String dCode){
        this.dCode = dCode;
    }

    public String getPkWorkcalendar(){
        return this.pkWorkcalendar;
    }
    public void setPkWorkcalendar(String pkWorkcalendar){
        this.pkWorkcalendar = pkWorkcalendar;
    }

    public String getEuRecurtype(){
        return this.euRecurtype;
    }
    public void setEuRecurtype(String euRecurtype){
        this.euRecurtype = euRecurtype;
    }

    public String getDtDateslottype(){
        return this.dtDateslottype;
    }
    public void setDtDateslottype(String dtDateslottype){
        this.dtDateslottype = dtDateslottype;
    }

    public String getPkTicketrules(){
        return this.pkTicketrules;
    }
    public void setPkTicketrules(String pkTicketrules){
        this.pkTicketrules = pkTicketrules;
    }

    public Long getMinutePer(){
        return this.minutePer;
    }
    public void setMinutePer(Long minutePer){
        this.minutePer = minutePer;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getFlagTicket(){
        return this.flagTicket;
    }
    public void setFlagTicket(String flagTicket){
        this.flagTicket = flagTicket;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }
	public String getPkDeptunit() {
		return pkDeptunit;
	}
	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}
	public String getPkOrgArea() {
		return pkOrgArea;
	}
	public void setPkOrgArea(String pkOrgArea) {
		this.pkOrgArea = pkOrgArea;
	}
	public String getPkDeptPlan() {
		return pkDeptPlan;
	}
	public void setPkDeptPlan(String pkDeptPlan) {
		this.pkDeptPlan = pkDeptPlan;
	}
	public String getCntSpec() {
		return cntSpec;
	}
	public void setCntSpec(String cntSpec) {
		this.cntSpec = cntSpec;
	}
	public String getEuAppttype() {
		return euAppttype;
	}
	public void setEuAppttype(String euAppttype) {
		this.euAppttype = euAppttype;
	}
	
}