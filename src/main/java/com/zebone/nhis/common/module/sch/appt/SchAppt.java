package com.zebone.nhis.common.module.sch.appt;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: SCH_APPT - sch_appt 
 *
 * @since 2016-10-18 08:44:57
 */
@Table(value="SCH_APPT")
public class SchAppt extends BaseModule  {

	@PK
	@Field(value="PK_SCHAPPT",id=KeyId.UUID)
    private String pkSchappt;

    /** EU_SCHCLASS - 0 门诊出诊；1 医技排班；2 床位预约；3 手术排班 */
	@Field(value="EU_SCHCLASS")
    private String euSchclass;

	@Field(value="PK_SCH")
    private String pkSch;

	@Field(value="CODE")
    private String code;

	@Field(value="DATE_APPT")
    private Date dateAppt;

	@Field(value="PK_DATESLOT")
    private String pkDateslot;

	@Field(value="PK_SCHRES")
    private String pkSchres;

	@Field(value="PK_SCHSRV")
    private String pkSchsrv;

	@Field(value="TICKET_NO")
    private String ticketNo;

	@Field(value="BEGIN_TIME")
    private Date beginTime;

	@Field(value="END_TIME")
    private Date endTime;

	@Field(value="PK_PI")
    private String pkPi;

    /** DT_APPTYPE - 记录本次预约的预约渠道。 */
	@Field(value="DT_APPTYPE")
    private String dtApptype;

	@Field(value="PK_ORG_EX")
    private String pkOrgEx;

	@Field(value="PK_DEPT_EX")
    private String pkDeptEx;

	@Field(value="DATE_REG")
    private Date dateReg;

	@Field(value="PK_DEPT_REG")
    private String pkDeptReg;

	@Field(value="PK_EMP_REG")
    private String pkEmpReg;

	@Field(value="NAME_EMP_REG")
    private String nameEmpReg;

    /** EU_STATUS - 0 预约，1 到达，2 取消 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** FLAG_PAY - 表示本预约是否已经完成支付，根据系统配置表示是否需要这个标志。 */
	@Field(value="FLAG_PAY")
    private String flagPay;

    /** FLAG_NOTICE - 表示本次预约已经通知患者 */
	@Field(value="FLAG_NOTICE")
    private String flagNotice;

	@Field(value="FLAG_CANCEL")
    private String flagCancel;

	@Field(value="DATE_CANCEL")
    private Date dateCancel;

	@Field(value="PK_EMP_CANCEL")
    private String pkEmpCancel;

	@Field(value="NAME_EMP_CANCEL")
    private String nameEmpCancel;

	@Field(value="NOTE")
    private String note;

    @Field(value="NOTE_APPT")
    private String noteAppt;

    /**
     * 外部预约系统订单号
     */
	@Field(value="ORDERID_EXT")
    private String orderidExt;

    /**
     * 号源未释放标志
     */
	@Field(value="FLAG_NORELEASE")
    private String flagNorelease;

	@Field(value="FLAG_NOTICE_CANC")
    private String flagNoticeCanc;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	
	/** 资源名称 */
	private String resourceName;
	
	/** 午别 */
	private String dateslotName;

    public String getNoteAppt() {
        return noteAppt;
    }

    public void setNoteAppt(String noteAppt) {
        this.noteAppt = noteAppt;
    }

    public String getOrderidExt() {
        return orderidExt;
    }

    public void setOrderidExt(String orderidExt) {
        this.orderidExt = orderidExt;
    }

    public String getFlagNorelease() {
        return flagNorelease;
    }

    public void setFlagNorelease(String flagNorelease) {
        this.flagNorelease = flagNorelease;
    }

    public String getPkSchappt(){
        return this.pkSchappt;
    }
    public void setPkSchappt(String pkSchappt){
        this.pkSchappt = pkSchappt;
    }

    public String getEuSchclass(){
        return this.euSchclass;
    }
    public void setEuSchclass(String euSchclass){
        this.euSchclass = euSchclass;
    }

    public String getPkSch(){
        return this.pkSch;
    }
    public void setPkSch(String pkSch){
        this.pkSch = pkSch;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }

    public Date getDateAppt(){
        return this.dateAppt;
    }
    public void setDateAppt(Date dateAppt){
        this.dateAppt = dateAppt;
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

    public String getTicketNo(){
        return this.ticketNo;
    }
    public void setTicketNo(String ticketNo){
        this.ticketNo = ticketNo;
    }

    public Date getBeginTime(){
        return this.beginTime;
    }
    public void setBeginTime(Date beginTime){
        this.beginTime = beginTime;
    }

    public Date getEndTime(){
        return this.endTime;
    }
    public void setEndTime(Date endTime){
        this.endTime = endTime;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getDtApptype(){
        return this.dtApptype;
    }
    public void setDtApptype(String dtApptype){
        this.dtApptype = dtApptype;
    }

    public String getPkOrgEx(){
        return this.pkOrgEx;
    }
    public void setPkOrgEx(String pkOrgEx){
        this.pkOrgEx = pkOrgEx;
    }

    public String getPkDeptEx(){
        return this.pkDeptEx;
    }
    public void setPkDeptEx(String pkDeptEx){
        this.pkDeptEx = pkDeptEx;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getPkDeptReg(){
        return this.pkDeptReg;
    }
    public void setPkDeptReg(String pkDeptReg){
        this.pkDeptReg = pkDeptReg;
    }

    public String getPkEmpReg(){
        return this.pkEmpReg;
    }
    public void setPkEmpReg(String pkEmpReg){
        this.pkEmpReg = pkEmpReg;
    }

    public String getNameEmpReg(){
        return this.nameEmpReg;
    }
    public void setNameEmpReg(String nameEmpReg){
        this.nameEmpReg = nameEmpReg;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagPay(){
        return this.flagPay;
    }
    public void setFlagPay(String flagPay){
        this.flagPay = flagPay;
    }

    public String getFlagNotice(){
        return this.flagNotice;
    }
    public void setFlagNotice(String flagNotice){
        this.flagNotice = flagNotice;
    }

    public String getFlagCancel(){
        return this.flagCancel;
    }
    public void setFlagCancel(String flagCancel){
        this.flagCancel = flagCancel;
    }

    public Date getDateCancel(){
        return this.dateCancel;
    }
    public void setDateCancel(Date dateCancel){
        this.dateCancel = dateCancel;
    }

    public String getPkEmpCancel(){
        return this.pkEmpCancel;
    }
    public void setPkEmpCancel(String pkEmpCancel){
        this.pkEmpCancel = pkEmpCancel;
    }

    public String getNameEmpCancel(){
        return this.nameEmpCancel;
    }
    public void setNameEmpCancel(String nameEmpCancel){
        this.nameEmpCancel = nameEmpCancel;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getFlagNoticeCanc(){
        return this.flagNoticeCanc;
    }
    public void setFlagNoticeCanc(String flagNoticeCanc){
        this.flagNoticeCanc = flagNoticeCanc;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getDateslotName() {
		return dateslotName;
	}
	public void setDateslotName(String dateslotName) {
		this.dateslotName = dateslotName;
	}
}