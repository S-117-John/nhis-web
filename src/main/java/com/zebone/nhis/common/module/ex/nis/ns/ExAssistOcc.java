package com.zebone.nhis.common.module.ex.nis.ns;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_ASSIST_OCC - ex_assist_occ 
 *
 * @since 2016-11-25 09:19:50
 */
@Table(value="EX_ASSIST_OCC")
public class ExAssistOcc extends BaseModule  {

	@PK
	@Field(value="PK_ASSOCC",id=KeyId.UUID)
    private String pkAssocc;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

    /** DT_OCCTYPE - 参见注释：医技执行-医嘱类型 */
	@Field(value="DT_OCCTYPE")
    private String dtOcctype;

    /** EU_PVTYPE - 1 门诊，2 急诊，3 住院，4 体检，5 家床 */
	@Field(value="EU_PVTYPE")
    private String euPvtype;

    /** CODE_OCC - 可用于执行单条形码 */
	@Field(value="CODE_OCC")
    private String codeOcc;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_EMP_ORD")
    private String pkEmpOrd;

	@Field(value="NAME_EMP_ORD")
    private String nameEmpOrd;

	@Field(value="DATE_ORD")
    private Date dateOrd;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="DATE_APPT")
    private Date dateAppt;

	@Field(value="QUAN_OCC")
    private Double quanOcc;

	@Field(value="TIMES_OCC")
    private Integer timesOcc;

	@Field(value="TIMES_TOTAL")
    private Integer timesTotal;

	@Field(value="PK_ORG_OCC")
    private String pkOrgOcc;

	@Field(value="PK_DEPT_OCC")
    private String pkDeptOcc;

	@Field(value="FLAG_OCC")
    private String flagOcc;

	@Field(value="DATE_OCC")
    private Date dateOcc;

	@Field(value="PK_EMP_OCC")
    private String pkEmpOcc;

	@Field(value="NAME_EMP_OCC")
    private String nameEmpOcc;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="PK_EXOCC")
    private String pkExocc;

	@Field(value="INFANT_NO")
    private String infantNo;

    /** EU_STATUS - 0 申请，1执行，9 取消 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_PRT")
    private String flagPrt;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	@Field(value="FLAG_REFUND")
	private String flagRefund;
	
	@Field(value="PK_SETTLE")
	private String pkSettle;
	/**执行诊区**/
    @Field(value="PK_DEPT_AREA")
    private String pkDeptArea;
    /**执行人考勤科室**/
    @Field(value="PK_DEPT_JOB")
    private String pkDeptJob;

    public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getFlagRefund() {
		return flagRefund;
	}
	public void setFlagRefund(String flagRefund) {
		this.flagRefund = flagRefund;
	}
	public String getPkAssocc(){
        return this.pkAssocc;
    }
    public void setPkAssocc(String pkAssocc){
        this.pkAssocc = pkAssocc;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getDtOcctype(){
        return this.dtOcctype;
    }
    public void setDtOcctype(String dtOcctype){
        this.dtOcctype = dtOcctype;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
    }

    public String getCodeOcc(){
        return this.codeOcc;
    }
    public void setCodeOcc(String codeOcc){
        this.codeOcc = codeOcc;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkEmpOrd(){
        return this.pkEmpOrd;
    }
    public void setPkEmpOrd(String pkEmpOrd){
        this.pkEmpOrd = pkEmpOrd;
    }

    public String getNameEmpOrd(){
        return this.nameEmpOrd;
    }
    public void setNameEmpOrd(String nameEmpOrd){
        this.nameEmpOrd = nameEmpOrd;
    }

    public Date getDateOrd(){
        return this.dateOrd;
    }
    public void setDateOrd(Date dateOrd){
        this.dateOrd = dateOrd;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public Date getDateAppt(){
        return this.dateAppt;
    }
    public void setDateAppt(Date dateAppt){
        this.dateAppt = dateAppt;
    }

    public Double getQuanOcc(){
        return this.quanOcc;
    }
    public void setQuanOcc(Double quanOcc){
        this.quanOcc = quanOcc;
    }

    public Integer getTimesOcc(){
        return this.timesOcc;
    }
    public void setTimesOcc(Integer timesOcc){
        this.timesOcc = timesOcc;
    }

    public Integer getTimesTotal(){
        return this.timesTotal;
    }
    public void setTimesTotal(Integer timesTotal){
        this.timesTotal = timesTotal;
    }

    public String getPkOrgOcc(){
        return this.pkOrgOcc;
    }
    public void setPkOrgOcc(String pkOrgOcc){
        this.pkOrgOcc = pkOrgOcc;
    }

    public String getPkDeptOcc(){
        return this.pkDeptOcc;
    }
    public void setPkDeptOcc(String pkDeptOcc){
        this.pkDeptOcc = pkDeptOcc;
    }

    public String getFlagOcc(){
        return this.flagOcc;
    }
    public void setFlagOcc(String flagOcc){
        this.flagOcc = flagOcc;
    }

    public Date getDateOcc(){
        return this.dateOcc;
    }
    public void setDateOcc(Date dateOcc){
        this.dateOcc = dateOcc;
    }

    public String getPkEmpOcc(){
        return this.pkEmpOcc;
    }
    public void setPkEmpOcc(String pkEmpOcc){
        this.pkEmpOcc = pkEmpOcc;
    }

    public String getNameEmpOcc(){
        return this.nameEmpOcc;
    }
    public void setNameEmpOcc(String nameEmpOcc){
        this.nameEmpOcc = nameEmpOcc;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public String getPkExocc(){
        return this.pkExocc;
    }
    public void setPkExocc(String pkExocc){
        this.pkExocc = pkExocc;
    }

    public String getInfantNo(){
        return this.infantNo;
    }
    public void setInfantNo(String infantNo){
        this.infantNo = infantNo;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagPrt(){
        return this.flagPrt;
    }
    public void setFlagPrt(String flagPrt){
        this.flagPrt = flagPrt;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getPkDeptArea() {
        return pkDeptArea;
    }

    public void setPkDeptArea(String pkDeptArea) {
        this.pkDeptArea = pkDeptArea;
    }

    public String getPkDeptJob() {
        return pkDeptJob;
    }

    public void setPkDeptJob(String pkDeptJob) {
        this.pkDeptJob = pkDeptJob;
    }
}