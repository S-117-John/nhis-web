package com.zebone.nhis.common.module.sch.plan;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: SCH_SCH  - sch_sch 
 *
 * @since 2016-09-22 08:46:26
 */
@Table(value="SCH_SCH")
public class SchSch extends BaseModule  {

	@PK
	@Field(value="PK_SCH",id=KeyId.UUID)
    private String pkSch;

	@Field(value="PK_PLANWEEK")
    private String pkPlanweek;
	
	@Field(value="PK_SCHPLAN")
    private String pkSchplan;

    /** EU_SCHCLASS - 0 门诊出诊；1 医技排班；2 床位预约；3 手术排班 */
	@Field(value="EU_SCHCLASS")
    private String euSchclass;

	@Field(value="DATE_WORK")
    private Date dateWork;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="PK_SCHRES")
    private String pkSchres;

    /** PK_SCHSRV - 由排班资源中引入默认值 */
	@Field(value="PK_SCHSRV")
    private String pkSchsrv;

	@Field(value="CNT_TOTAL")
    private Integer cntTotal;

	@Field(value="CNT_APPT")
    private Integer cntAppt;

	@Field(value="CNT_ADD")
    private Integer cntAdd;

	@Field(value="CNT_USED")
    private Integer cntUsed;

	@Field(value="CNT_OVER")
    private Integer cntOver;

    /** TICKET_NO - 对无号表排班有效。 */
	@Field(value="TICKET_NO")
    private String ticketNo;

	@Field(value="MINUTE_PER")
    private Integer minutePer;

    /** PK_DEPT - 本资源归属的部门 */
	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="FLAG_STOP")
    private String flagStop;
	
	@Field(value="MODITY_TIME")
    private Date modityTime;

	/** eu_status -状态:	0 排班，1提交，8审核*/
	@Field(value="EU_STATUS")
	private String euStatus;

	@Field(value="PK_EMP_CHK")
	private String pkEmpChk;
	
	@Field(value="name_emp_chk")
	private String nameEmpChk;
	
	@Field(value="DATE_CHK")
    private Date dateChk;
	
	@Field(value="FLAG_MODI")
	private String flagModi;
	
	@Field(value="pk_orgarea")
	private String pkOrgarea;
	
	@Field(value="pk_emp_sch")
	private String pkEmpSch;
	
	@Field(value="name_emp_sch")
	private String nameEmpSch;
	
	@Field(value="date_sch")
	private Date dateSch;
	
	@Field(value="PK_DEPTUNIT")
	private String pkDeptunit;
	
	@Field(value="NOTE")
	private String note;
	
	@Field(value="EU_APPTTYPE")
	private String euAppttype;

	@Field(value="EU_RELEASETYPE")
	private String euReleasetype;

    public String getEuReleasetype() {
        return euReleasetype;
    }

    public void setEuReleasetype(String euReleasetype) {
        this.euReleasetype = euReleasetype;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getPkPlanweek(){
        return this.pkPlanweek;
    }
    public void setPkPlanweek(String pkPlanweek){
        this.pkPlanweek = pkPlanweek;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
    }

    public Date getDateWork(){
        return this.dateWork;
    }
    public void setDateWork(Date dateWork){
        this.dateWork = dateWork;
    }

    public String getPkDateslot(){
        return this.pkDateslot;
    }
    public void setPkDateslot(String pkDateslot){
        this.pkDateslot = pkDateslot;
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

    public Integer getCntTotal(){
        return this.cntTotal;
    }
    public void setCntTotal(Integer cntTotal){
        this.cntTotal = cntTotal;
    }

    public Integer getCntAppt(){
        return this.cntAppt == null ? 0 : this.cntAppt;
    }
    public void setCntAppt(Integer cntAppt){
        this.cntAppt = cntAppt;
    }

    public Integer getCntAdd(){
        return this.cntAdd;
    }
    public void setCntAdd(Integer cntAdd){
        this.cntAdd = cntAdd;
    }

    public Integer getCntUsed(){
        return this.cntUsed == null ? 0 : this.cntUsed;
    }
    public void setCntUsed(Integer cntUsed){
        this.cntUsed = cntUsed;
    }

    public Integer getCntOver(){
        return this.cntOver;
    }
    public void setCntOver(Integer cntOver){
        this.cntOver = cntOver;
    }

    public String getTicketNo(){
        return this.ticketNo;
    }
    public void setTicketNo(String ticketNo){
        this.ticketNo = ticketNo;
    }

    public Integer getMinutePer(){
        return this.minutePer;
    }
    public void setMinutePer(Integer minutePer){
        this.minutePer = minutePer;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getPkSchplan() {
		return pkSchplan;
	}
	public void setPkSchplan(String pkSchplan) {
		this.pkSchplan = pkSchplan;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
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
	public String getFlagModi() {
		return flagModi;
	}
	public void setFlagModi(String flagModi) {
		this.flagModi = flagModi;
	}
	public String getPkOrgarea() {
		return pkOrgarea;
	}
	public void setPkOrgarea(String pkOrgarea) {
		this.pkOrgarea = pkOrgarea;
	}
	public String getPkEmpSch() {
		return pkEmpSch;
	}
	public void setPkEmpSch(String pkEmpSch) {
		this.pkEmpSch = pkEmpSch;
	}
	public String getNameEmpSch() {
		return nameEmpSch;
	}
	public void setNameEmpSch(String nameEmpSch) {
		this.nameEmpSch = nameEmpSch;
	}
	public Date getDateSch() {
		return dateSch;
	}
	public void setDateSch(Date dateSch) {
		this.dateSch = dateSch;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getPkDeptunit() {
		return pkDeptunit;
	}
	public void setPkDeptunit(String pkDeptunit) {
		this.pkDeptunit = pkDeptunit;
	}
	public String getEuAppttype() {
		return euAppttype;
	}
	public void setEuAppttype(String euAppttype) {
		this.euAppttype = euAppttype;
	}
    
    
    
}