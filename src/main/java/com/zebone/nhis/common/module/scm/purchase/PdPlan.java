package com.zebone.nhis.common.module.scm.purchase;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: PD_PLAN - pd_plan 
 *
 * @since 2016-10-31 11:23:57
 */
@Table(value="PD_PLAN")
public class PdPlan extends BaseModule  {

	@PK
	@Field(value="PK_PDPLAN",id=KeyId.UUID)
    private String pkPdplan;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_STORE")
    private String pkStore;

    /** DT_PLANTYPE - 0X 入库计划：00 采购计划。1X 出库计划：00 部门清领。2X 调拨计划：30 调拨请领 */
	@Field(value="DT_PLANTYPE")
    private String dtPlantype;

	@Field(value="CODE_PLAN")
    private String codePlan;

	@Field(value="NAME_PLAN")
    private String namePlan;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="DT_PLANRECUR")
    private String dtPlanrecur;
	
    /** EU_STATUS - 0 制单，1 提交，2  执行 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_ORG_ACC")
    private String pkOrgAcc;

	@Field(value="PK_DEPT_ACC")
    private String pkDeptAcc;

	@Field(value="PK_STORE_ACC")
    private String pkStoreAcc;

    /** FLAG_ACC - 已经生成采购单或请领单 */
	@Field(value="FLAG_ACC")
    private String flagAcc;

	@Field(value="PK_EMP_MAK")
    private String pkEmpMak;

	@Field(value="NAME_EMP_MAK")
    private String nameEmpMak;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="DT_PDPUWAY")
	private String dtPdpuway;
	
	@Field(value="FLAG_EMER")
	private String flagEmer;
	
	@Field(value="EU_DISTTYPE")
	private String euDisttype;
	
	@Field(value="PLACE_DIST")
	private String placeDist;
	
	@Field(value="CODE_RTN")
	private String codeRtn;
	
	@Field(value="FLAG_PLAN")
	private String flagPlan;

    @Field(value="FLAG_CHK")
    private String flagChk;

    @Field(value="PK_EMP_CHK")
    private String pkEmpChk;

    @Field(value="name_emp_chk")
    private String nameEmpChk;

    @Field(value="DATE_CHK")
    private Date dateChk;

    public String getPkPdplan(){
        return this.pkPdplan;
    }
    public void setPkPdplan(String pkPdplan){
        this.pkPdplan = pkPdplan;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkStore(){
        return this.pkStore;
    }
    public void setPkStore(String pkStore){
        this.pkStore = pkStore;
    }

    public String getDtPlantype(){
        return this.dtPlantype;
    }
    public void setDtPlantype(String dtPlantype){
        this.dtPlantype = dtPlantype;
    }

    public String getCodePlan(){
        return this.codePlan;
    }
    public void setCodePlan(String codePlan){
        this.codePlan = codePlan;
    }

    public String getNamePlan(){
        return this.namePlan;
    }
    public void setNamePlan(String namePlan){
        this.namePlan = namePlan;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkOrgAcc(){
        return this.pkOrgAcc;
    }
    public void setPkOrgAcc(String pkOrgAcc){
        this.pkOrgAcc = pkOrgAcc;
    }

    public String getPkDeptAcc(){
        return this.pkDeptAcc;
    }
    public void setPkDeptAcc(String pkDeptAcc){
        this.pkDeptAcc = pkDeptAcc;
    }

    public String getPkStoreAcc(){
        return this.pkStoreAcc;
    }
    public void setPkStoreAcc(String pkStoreAcc){
        this.pkStoreAcc = pkStoreAcc;
    }

    public String getFlagAcc(){
        return this.flagAcc;
    }
    public void setFlagAcc(String flagAcc){
        this.flagAcc = flagAcc;
    }

    public String getPkEmpMak(){
        return this.pkEmpMak;
    }
    public void setPkEmpMak(String pkEmpMak){
        this.pkEmpMak = pkEmpMak;
    }

    public String getNameEmpMak(){
        return this.nameEmpMak;
    }
    public void setNameEmpMak(String nameEmpMak){
        this.nameEmpMak = nameEmpMak;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getDtPlanrecur() {
		return dtPlanrecur;
	}
	public void setDtPlanrecur(String dtPlanrecur) {
		this.dtPlanrecur = dtPlanrecur;
	}
	public String getDtPdpuway() {
		return dtPdpuway;
	}
	public void setDtPdpuway(String dtPdpuway) {
		this.dtPdpuway = dtPdpuway;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public String getEuDisttype() {
		return euDisttype;
	}
	public void setEuDisttype(String euDisttype) {
		this.euDisttype = euDisttype;
	}
	public String getPlaceDist() {
		return placeDist;
	}
	public void setPlaceDist(String placeDist) {
		this.placeDist = placeDist;
	}
	public String getCodeRtn() {
		return codeRtn;
	}
	public void setCodeRtn(String codeRtn) {
		this.codeRtn = codeRtn;
	}
	public String getFlagPlan() {
		return flagPlan;
	}
	public void setFlagPlan(String flagPlan) {
		this.flagPlan = flagPlan;
	}

    public String getFlagChk() {
        return flagChk;
    }

    public void setFlagChk(String flagChk) {
        this.flagChk = flagChk;
    }

    public String getPkEmpChk() {
        return pkEmpChk;
    }

    public void setPkEmpChk(String pkEmpChk) {
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk() {
        return nameEmpChk;
    }

    public void setNameEmpChk(String nameEmpChk) {
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk() {
        return dateChk;
    }

    public void setDateChk(Date dateChk) {
        this.dateChk = dateChk;
    }
}