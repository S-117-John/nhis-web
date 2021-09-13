package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;


/**
 * Table: CN_ORDER 
 *
 * @since 2016-10-13 03:18:37
 */
@Table(value="CN_ORDER")
public class CnOrder extends BaseModule  {

	@PK
	@Field(value="PK_CNORD",id=KeyId.UUID)
    private String pkCnord;

	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="DATE_EFFE")
    private Date dateEffe;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="SORT_IV")
	private Integer sortIv;
	
	@Field(value="ORDSN")
    private Integer ordsn;
	
	//做唯一校验用
	@Field(value="ORDSN_CHK")
    private Integer ordsnChk;

	@Field(value="ORDSN_PARENT")
    private Integer ordsnParent;
	
	@Field(value="groupno")
    private Integer groupno;

	@Field(value="PK_ORD")
    private String pkOrd;

	@Field(value="CODE_ORD")
    private String codeOrd;

	@Field(value="PK_PRES")
    private String pkPres;

	@Field(value="NAME_ORD")
    private String nameOrd;

	@Field(value="DESC_ORD")
    private String descOrd;

	@Field(value="CODE_APPLY")
    private String codeApply;

	@Field(value="CODE_FREQ")
    private String codeFreq;

	@Field(value="SPEC")
    private String spec;

	@Field(value="DOSAGE")
    private Double dosage;

	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="PK_UNIT")
    private String pkUnit;
	
	@Field(value = "QUAN_DISP")
	private Double quanDisp;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="QUAN_CG")
    private Double quanCg;

	@Field(value="PK_UNIT_CG")
    private String pkUnitCg;

	@Field(value="PACK_SIZE")
    private Double packSize;

	@Field(value="PRICE_CG")
    private Double priceCg;

	@Field(value="NOTE_SUPPLY")
    private String noteSupply;

	@Field(value="DAYS")
    private Long days;

	@Field(value="DRIP_SPEED")
    private Integer dripSpeed;

	@Field(value="ORDS")
    private Long ords;

	@Field(value="FLAG_FIRST")
    private String flagFirst;

	@Field(value="LAST_NUM")
    private Long lastNum;

	@Field(value="PK_ORG_EXEC")
    private String pkOrgExec;

	@Field(value="PK_DEPT_EXEC")
    private String pkDeptExec;

	@Field(value="EU_STATUS_ORD")
    private String euStatusOrd;

	@Field(value="DATE_ENTER")
    private Date dateEnter;

	@Field(value="DATE_START")
    private Date dateStart;

	@Field(value="FLAG_DURG")
    private String flagDurg;

	@Field(value="FLAG_SELF")
    private String flagSelf;

	@Field(value="FLAG_NOTE")
    private String flagNote;

	@Field(value="FLAG_BASE")
    private String flagBase;

	@Field(value="FLAG_BL")
    private String flagBl;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="PK_WG")
    private String pkWg;

    @Field(value="PK_WG_ORG")
    private String pkWgOrg;

	@Field(value="PK_EMP_INPUT")
    private String pkEmpInput;

	@Field(value="NAME_EMP_INPUT")
    private String nameEmpInput;

	@Field(value="PK_EMP_ORD")
    private String pkEmpOrd;

	@Field(value="NAME_EMP_ORD")
    private String nameEmpOrd;

	@Field(value="DATE_SIGN")
    private Date dateSign;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="DATE_LAST_EX")
    private Date dateLastEx;

	@Field(value="DATE_PLAN_EX")
    private Date datePlanEx;

	@Field(value="DATE_STOP")
    private Date dateStop;

	@Field(value="PK_EMP_STOP")
    private String pkEmpStop;

	@Field(value="NAME_EMP_STOP")
    private String nameEmpStop;

	@Field(value="FLAG_STOP")
    private String flagStop;

	@Field(value="DATE_STOP_CHK")
    private Date dateStopChk;

	@Field(value="PK_EMP_STOP_CHK")
    private String pkEmpStopChk;

	@Field(value="NAME_EMP_STOP_CHK")
    private String nameEmpStopChk;

	@Field(value="FLAG_STOP_CHK")
    private String flagStopChk;

	@Field(value="DATE_ERASE")
    private Date dateErase;

	@Field(value="PK_EMP_ERASE")
    private String pkEmpErase;

	@Field(value="NAME_EMP_ERASE")
    private String nameEmpErase;

	@Field(value="FLAG_ERASE")
    private String flagErase;

	@Field(value="DATE_ERASE_CHK")
    private Date dateEraseChk;

	@Field(value="PK_EMP_ERASE_CHK")
    private String pkEmpEraseChk;

	@Field(value="NAME_ERASE_CHK")
    private String nameEraseChk;

	@Field(value="FLAG_ERASE_CHK")
    private String flagEraseChk;

	@Field(value="FLAG_CP")
    private String flagCp;

	@Field(value="FLAG_DOCTOR")
    private String flagDoctor;

	@Field(value="INFANT_NO")
    private Integer infantNo;

	@Field(value="PK_EVENT")
    private String pkEvent;

	@Field(value="FLAG_PRINT")
    private String flagPrint;

	@Field(value="FLAG_MEDOUT")
    private String flagMedout;

	@Field(value="EU_EXCTYPE")
    private String euExctype;

	@Field(value="PK_ORD_EXC")
    private String pkOrdExc;

	@Field(value="FLAG_EMER")
    private String flagEmer;

	@Field(value="FLAG_THERA")
    private String flagThera;

	@Field(value="FLAG_PREV")
    private String flagPrev;

	@Field(value="FLAG_FIT")
    private String flagFit;

	@Field(value="QUAN_BED")
    private Double quanBed;

	@Field(value="NOTE_ORD")
    private String noteOrd;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value = "DATE_TAKE")
	private Date dateTake;

	@Field(value="FLAG_SIGN")
    private String flagSign;
	
	@Field(value="EU_INTERN")
	private String euIntern;
	
	@Field(value="EU_ST")
	private String euSt;
	
	@Field(value="DT_USAGENOTE")
	private String dtUsagenote;
	
	@Field(value="FIRST_NUM")
	private Integer firstNum;
	
	@Field(value="FLAG_PIVAS")
	private String flagPivas;
	
	@Field(value="DT_HERBUSAGE")
	private String dtHerbusage;
	
	@Field(value="DESC_FIT")
	private String descFit;
	
	@Field(value="FLAG_ITC")
    private String flagItc;
	
	@Field(value="PK_EMP_EX")
    private String pkEmpEx;//执行护士主键

	@Field(value="NAME_EMP_EX")
    private String nameEmpEx;//执行护士名称
	
	@Field(value="PK_CNORD_RL")
    private String pkCnordRl;//关联医嘱
	
	@Field(value="CODE_SUPPLY_ADD")
	private String codeSupplyAdd;

	@Field(value = "EU_ORDTYPE")
    private String euOrdtype;

    /**处方医保类型*/
    @Field(value = "DT_HPPROP")
    private String dtHpprop;

    /**执行确认**/
    @Field(value = "FLAG_OCC")
    private String flagOcc;

    /**考勤科室**/
    @Field(value = "PK_DEPT_JOB")
    private String pkDeptJob;

    /**执行诊区**/
    @Field(value = "PK_DEPT_AREA")
    private String pkDeptArea;

    @Field(value = "FLAG_DISP")
    private String flagDisp;//不发药标志

    @Field(value = "QUAN_BACK")
    private String quanBack;//退药数量
    
    /**开立诊区*/
    @Field(value = "PK_DEPT_AREAAPP")
    private String pkDeptAreaapp;
    /**抢救**/
    @Field(value = "FLAG_RESCUE")
    private String flagRescue;

    /**计划执行次数**/
    @Field(value = "PLAN_OCC_NUM")
    private Integer planOccNum;

    /**伤病判定 0 因伤，1 因病**/
    @Field(value = "EU_INJURY")
    private String euInjury;

    /**不符合条件原因**/
    @Field(value = "EU_EASON_DIS")
    private String euEasonDis;

    @Field(value = "AMOUNT_DISC")
    private Double amountDisc;

    @Field(value = "FLAG_DISCST")
    private String flagDiscst;

    @Field(value = "FLAG_PLAN")
    private String flagPlan;

    public String getEuInjury() {
        return euInjury;
    }

    public void setEuInjury(String euInjury) {
        this.euInjury = euInjury;
    }

    /**作废原因**/
    @Field(value = "DT_REASON_ERASE")
    private String dtReasonErase;

    public String getBarcode() {
        return barcode;
    }
    /**特殊单位标记*/
    @Field(value="FLAG_SP_UNIT")
    private String flagSpUnit;
    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    private String barcode;/**高值耗材**/

    public String getPkWgOrg() {
        return pkWgOrg;
    }

    public void setPkWgOrg(String pkWgOrg) {
        this.pkWgOrg = pkWgOrg;
    }

    

    public Integer getOrdsnChk() {
		return ordsnChk;
	}
	public void setOrdsnChk(Integer ordsnChk) {
		this.ordsnChk = ordsnChk;
	}
	public String getPkCnordRl() {
		return pkCnordRl;
	}
	public void setPkCnordRl(String pkCnordRl) {
		this.pkCnordRl = pkCnordRl;
	}
	public String getFlagItc() {
		return flagItc;
	}
	public void setFlagItc(String flagItc) {
		this.flagItc = flagItc;
	}
	public String getDescFit() {
		return descFit;
	}
	public void setDescFit(String descFit) {
		this.descFit = descFit;
	}
	public String getEuIntern() {
		return euIntern;
	}
	public void setEuIntern(String euIntern) {
		this.euIntern = euIntern;
	}
	public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getEuPvtype(){
        return this.euPvtype;
    }
    public void setEuPvtype(String euPvtype){
        this.euPvtype = euPvtype;
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

    public Date getDateEffe(){
        return this.dateEffe;
    }
    public void setDateEffe(Date dateEffe){
        this.dateEffe = dateEffe;
    }

    public String getCodeOrdtype(){
        return this.codeOrdtype;
    }
    public void setCodeOrdtype(String codeOrdtype){
        this.codeOrdtype = codeOrdtype;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }
    public Integer getSortIv() {
		return sortIv;
	}
	public void setSortIv(Integer sortIv) {
		this.sortIv = sortIv;
	}
	public Integer getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(Integer ordsn){
        this.ordsn = ordsn;
    }

    public Integer getOrdsnParent(){
        return this.ordsnParent;
    }
    public void setOrdsnParent(Integer ordsnParent){
        this.ordsnParent = ordsnParent;
    }

    public String getPkOrd(){
        return this.pkOrd;
    }
    public void setPkOrd(String pkOrd){
        this.pkOrd = pkOrd;
    }

    public String getCodeOrd(){
        return this.codeOrd;
    }
    public void setCodeOrd(String codeOrd){
        this.codeOrd = codeOrd;
    }

    public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
    }
    
	public Double getQuanDisp() {
		return quanDisp;
	}
	public void setQuanDisp(Double quanDisp) {
		this.quanDisp = quanDisp;
	}

    public String getNameOrd(){
        return this.nameOrd;
    }
    public void setNameOrd(String nameOrd){
        this.nameOrd = nameOrd;
    }

    public String getDescOrd(){
        return this.descOrd;
    }
    public void setDescOrd(String descOrd){
        this.descOrd = descOrd;
    }

    public String getCodeApply(){
        return this.codeApply;
    }
    public void setCodeApply(String codeApply){
        this.codeApply = codeApply;
    }

    public String getCodeFreq(){
        return this.codeFreq;
    }
    public void setCodeFreq(String codeFreq){
        this.codeFreq = codeFreq;
    }

    public String getSpec(){
        return this.spec;
    }
    public void setSpec(String spec){
        this.spec = spec;
    }

    public Double getDosage(){
        return this.dosage;
    }
    public void setDosage(Double dosage){
        this.dosage = dosage;
    }

    public String getPkUnitDos(){
        return this.pkUnitDos;
    }
    public void setPkUnitDos(String pkUnitDos){
        this.pkUnitDos = pkUnitDos;
    }

    public Double getQuan(){
        return this.quan;
    }
    public void setQuan(Double quan){
        this.quan = quan;
    }

    public String getPkUnit(){
        return this.pkUnit;
    }
    public void setPkUnit(String pkUnit){
        this.pkUnit = pkUnit;
    }

    public String getCodeSupply(){
        return this.codeSupply;
    }
    public void setCodeSupply(String codeSupply){
        this.codeSupply = codeSupply;
    }

    public Double getQuanCg(){
        return this.quanCg;
    }
    public void setQuanCg(Double quanCg){
        this.quanCg = quanCg;
    }

    public String getPkUnitCg(){
        return this.pkUnitCg;
    }
    public void setPkUnitCg(String pkUnitCg){
        this.pkUnitCg = pkUnitCg;
    }

    public Double getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Double packSize){
        this.packSize = packSize;
    }

    public Double getPriceCg(){
        return this.priceCg;
    }
    public void setPriceCg(Double priceCg){
        this.priceCg = priceCg;
    }

    public String getNoteSupply(){
        return this.noteSupply;
    }
    public void setNoteSupply(String noteSupply){
        this.noteSupply = noteSupply;
    }

    public Long getDays(){
        return this.days;
    }
    public void setDays(Long days){
        this.days = days;
    }

    public Integer getDripSpeed(){
        return this.dripSpeed;
    }
    public void setDripSpeed(Integer dripSpeed){
        this.dripSpeed = dripSpeed;
    }

    public Long getOrds(){
        return this.ords;
    }
    public void setOrds(Long ords){
        this.ords = ords;
    }

    public String getFlagFirst(){
        return this.flagFirst;
    }
    public void setFlagFirst(String flagFirst){
        this.flagFirst = flagFirst;
    }

    public Long getLastNum(){
        return this.lastNum;
    }
    public void setLastNum(Long lastNum){
        this.lastNum = lastNum;
    }

    public String getPkOrgExec(){
        return this.pkOrgExec;
    }
    public void setPkOrgExec(String pkOrgExec){
        this.pkOrgExec = pkOrgExec;
    }

    public String getPkDeptExec(){
        return this.pkDeptExec;
    }
    public void setPkDeptExec(String pkDeptExec){
        this.pkDeptExec = pkDeptExec;
    }

    public String getEuStatusOrd(){
        return this.euStatusOrd;
    }
    public void setEuStatusOrd(String euStatusOrd){
        this.euStatusOrd = euStatusOrd;
    }

    public Date getDateEnter(){
        return this.dateEnter;
    }
    public void setDateEnter(Date dateEnter){
        this.dateEnter = dateEnter;
    }

    public Date getDateStart(){
        return this.dateStart;
    }
    public void setDateStart(Date dateStart){
        this.dateStart = dateStart;
    }

    public String getFlagDurg(){
        return this.flagDurg;
    }
    public void setFlagDurg(String flagDurg){
        this.flagDurg = flagDurg;
    }

    public String getFlagSelf(){
        return this.flagSelf;
    }
    public void setFlagSelf(String flagSelf){
        this.flagSelf = flagSelf;
    }

    public String getFlagNote(){
        return this.flagNote;
    }
    public void setFlagNote(String flagNote){
        this.flagNote = flagNote;
    }

    public String getFlagBase(){
        return this.flagBase;
    }
    public void setFlagBase(String flagBase){
        this.flagBase = flagBase;
    }

    public String getFlagBl(){
        return this.flagBl;
    }
    public void setFlagBl(String flagBl){
        this.flagBl = flagBl;
    }

    public String getPkDept(){
        return this.pkDept;
    }
    public void setPkDept(String pkDept){
        this.pkDept = pkDept;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
    }

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public String getPkEmpInput(){
        return this.pkEmpInput;
    }
    public void setPkEmpInput(String pkEmpInput){
        this.pkEmpInput = pkEmpInput;
    }

    public String getNameEmpInput(){
        return this.nameEmpInput;
    }
    public void setNameEmpInput(String nameEmpInput){
        this.nameEmpInput = nameEmpInput;
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

    public Date getDateSign(){
        return this.dateSign;
    }
    public void setDateSign(Date dateSign){
        this.dateSign = dateSign;
    }

    public String getPkEmpChk(){
        return this.pkEmpChk;
    }
    public void setPkEmpChk(String pkEmpChk){
        this.pkEmpChk = pkEmpChk;
    }

    public String getNameEmpChk(){
        return this.nameEmpChk;
    }
    public void setNameEmpChk(String nameEmpChk){
        this.nameEmpChk = nameEmpChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public Date getDateLastEx(){
        return this.dateLastEx;
    }
    public void setDateLastEx(Date dateLastEx){
        this.dateLastEx = dateLastEx;
    }

    public Date getDatePlanEx(){
        return this.datePlanEx;
    }
    public void setDatePlanEx(Date datePlanEx){
        this.datePlanEx = datePlanEx;
    }

    public Date getDateStop(){
        return this.dateStop;
    }
    public void setDateStop(Date dateStop){
        this.dateStop = dateStop;
    }

    public String getPkEmpStop(){
        return this.pkEmpStop;
    }
    public void setPkEmpStop(String pkEmpStop){
        this.pkEmpStop = pkEmpStop;
    }

    public String getNameEmpStop(){
        return this.nameEmpStop;
    }
    public void setNameEmpStop(String nameEmpStop){
        this.nameEmpStop = nameEmpStop;
    }

    public String getFlagStop(){
        return this.flagStop;
    }
    public void setFlagStop(String flagStop){
        this.flagStop = flagStop;
    }

    public Date getDateStopChk(){
        return this.dateStopChk;
    }
    public void setDateStopChk(Date dateStopChk){
        this.dateStopChk = dateStopChk;
    }

    public String getPkEmpStopChk(){
        return this.pkEmpStopChk;
    }
    public void setPkEmpStopChk(String pkEmpStopChk){
        this.pkEmpStopChk = pkEmpStopChk;
    }

    public String getNameEmpStopChk(){
        return this.nameEmpStopChk;
    }
    public void setNameEmpStopChk(String nameEmpStopChk){
        this.nameEmpStopChk = nameEmpStopChk;
    }

    public String getFlagStopChk(){
        return this.flagStopChk;
    }
    public void setFlagStopChk(String flagStopChk){
        this.flagStopChk = flagStopChk;
    }

    public Date getDateErase(){
        return this.dateErase;
    }
    public void setDateErase(Date dateErase){
        this.dateErase = dateErase;
    }

    public String getPkEmpErase(){
        return this.pkEmpErase;
    }
    public void setPkEmpErase(String pkEmpErase){
        this.pkEmpErase = pkEmpErase;
    }

    public String getNameEmpErase(){
        return this.nameEmpErase;
    }
    public void setNameEmpErase(String nameEmpErase){
        this.nameEmpErase = nameEmpErase;
    }

    public String getFlagErase(){
        return this.flagErase;
    }
    public void setFlagErase(String flagErase){
        this.flagErase = flagErase;
    }

    public Date getDateEraseChk(){
        return this.dateEraseChk;
    }
    public void setDateEraseChk(Date dateEraseChk){
        this.dateEraseChk = dateEraseChk;
    }

    public String getPkEmpEraseChk(){
        return this.pkEmpEraseChk;
    }
    public void setPkEmpEraseChk(String pkEmpEraseChk){
        this.pkEmpEraseChk = pkEmpEraseChk;
    }

    public String getNameEraseChk(){
        return this.nameEraseChk;
    }
    public void setNameEraseChk(String nameEraseChk){
        this.nameEraseChk = nameEraseChk;
    }

    public String getFlagEraseChk(){
        return this.flagEraseChk;
    }
    public void setFlagEraseChk(String flagEraseChk){
        this.flagEraseChk = flagEraseChk;
    }

    public String getFlagCp(){
        return this.flagCp;
    }
    public void setFlagCp(String flagCp){
        this.flagCp = flagCp;
    }

    public String getFlagDoctor(){
        return this.flagDoctor;
    }
    public void setFlagDoctor(String flagDoctor){
        this.flagDoctor = flagDoctor;
    }

    public Integer getInfantNo() {
		return infantNo;
	}
	public void setInfantNo(Integer infantNo) {
		this.infantNo = infantNo;
	}
	public String getPkEvent(){
        return this.pkEvent;
    }
    public void setPkEvent(String pkEvent){
        this.pkEvent = pkEvent;
    }

    public String getFlagPrint(){
        return this.flagPrint;
    }
    public void setFlagPrint(String flagPrint){
        this.flagPrint = flagPrint;
    }

    public String getFlagMedout(){
        return this.flagMedout;
    }
    public void setFlagMedout(String flagMedout){
        this.flagMedout = flagMedout;
    }

    public String getEuExctype(){
        return this.euExctype;
    }
    public void setEuExctype(String euExctype){
        this.euExctype = euExctype;
    }

    public String getPkOrdExc(){
        return this.pkOrdExc;
    }
    public void setPkOrdExc(String pkOrdExc){
        this.pkOrdExc = pkOrdExc;
    }

    public String getFlagEmer(){
        return this.flagEmer;
    }
    public void setFlagEmer(String flagEmer){
        this.flagEmer = flagEmer;
    }

    public String getFlagThera(){
        return this.flagThera;
    }
    public void setFlagThera(String flagThera){
        this.flagThera = flagThera;
    }

    public String getFlagPrev(){
        return this.flagPrev;
    }
    public void setFlagPrev(String flagPrev){
        this.flagPrev = flagPrev;
    }

    public String getFlagFit(){
        return this.flagFit;
    }
    public void setFlagFit(String flagFit){
        this.flagFit = flagFit;
    }

    public Double getQuanBed(){
        return this.quanBed;
    }
    public void setQuanBed(Double quanBed){
        this.quanBed = quanBed;
    }

    public String getNoteOrd(){
        return this.noteOrd;
    }
    public void setNoteOrd(String noteOrd){
        this.noteOrd = noteOrd;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getFlagSign(){
        return this.flagSign;
    }
    public void setFlagSign(String flagSign){
        this.flagSign = flagSign;
    }
    /**
	 * 数据更新状态
	 */
	private String rowStatus;
	
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	/**
	 * 处方号
	 */
    private String presNo;
    
	public String getPresNo() {
		return presNo;
	}
	public void setPresNo(String presNo) {
		this.presNo = presNo;
	}
	/**
	 * 路径启用记录
	 */
	private String pkCprec;
	
	//路径运用阶段
	private String pkCpphase;
	/**
	 * 变异主键
	 */
	private String pkCpexp;
	//变异原因名称
	private String nameExp;
	/**
	 * 变异原因备注
	 */
	private String expNote;

	private String euBoil;
    /**
     * 送达时间
     */
	private Date dateSend;
	
	private Integer friedNum;
	
	private Integer usageCount;
	
	private Double dosagePack;
	
	private String dtBoiltype;
	
	 //监测类型
	private String euMonitorType; 
    //预防类型
	private String euPrevtype;
    //治疗类型
	private String euTheratype;
    //手术预防类型
	private String dtOpprevtype;
    //录入人
	private String pkEmpEntry;
    //录入人
	private String nameEmpEntry;
    //录入日期
	private Date dateEntry;
	
	private String pkOrdanti;
	
	
	
	
	
	public String getNameExp() {
		return nameExp;
	}
	public void setNameExp(String nameExp) {
		this.nameExp = nameExp;
	}
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getPkOrdanti() {
		return pkOrdanti;
	}
	public void setPkOrdanti(String pkOrdanti) {
		this.pkOrdanti = pkOrdanti;
	}
	public String getEuMonitorType() {
		return euMonitorType;
	}
	public void setEuMonitorType(String euMonitorType) {
		this.euMonitorType = euMonitorType;
	}
	public String getEuPrevtype() {
		return euPrevtype;
	}
	public void setEuPrevtype(String euPrevtype) {
		this.euPrevtype = euPrevtype;
	}
	public String getEuTheratype() {
		return euTheratype;
	}
	public void setEuTheratype(String euTheratype) {
		this.euTheratype = euTheratype;
	}
	public String getDtOpprevtype() {
		return dtOpprevtype;
	}
	public void setDtOpprevtype(String dtOpprevtype) {
		this.dtOpprevtype = dtOpprevtype;
	}
	public String getPkEmpEntry() {
		return pkEmpEntry;
	}
	public void setPkEmpEntry(String pkEmpEntry) {
		this.pkEmpEntry = pkEmpEntry;
	}
	public String getNameEmpEntry() {
		return nameEmpEntry;
	}
	public void setNameEmpEntry(String nameEmpEntry) {
		this.nameEmpEntry = nameEmpEntry;
	}
	public Date getDateEntry() {
		return dateEntry;
	}
	public void setDateEntry(Date dateEntry) {
		this.dateEntry = dateEntry;
	}
	public String getDtBoiltype() {
		return dtBoiltype;
	}
	public void setDtBoiltype(String dtBoiltype) {
		this.dtBoiltype = dtBoiltype;
	}
	public Integer getFriedNum() {
		return friedNum;
	}
	public void setFriedNum(Integer friedNum) {
		this.friedNum = friedNum;
	}
	public Integer getUsageCount() {
		return usageCount;
	}
	public void setUsageCount(Integer usageCount) {
		this.usageCount = usageCount;
	}
	public Double getDosagePack() {
		return dosagePack;
	}
	public void setDosagePack(Double dosagePack) {
		this.dosagePack = dosagePack;
	}
	public String getEuBoil() {
		return euBoil;
	}
	public void setEuBoil(String euBoil) {
		this.euBoil = euBoil;
	}
	public String getPkCprec() {
		return pkCprec;
	}
	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
	}
	public String getPkCpexp() {
		return pkCpexp;
	}
	public void setPkCpexp(String pkCpexp) {
		this.pkCpexp = pkCpexp;
	}
	public String getExpNote() {
		return expNote;
	}
	public void setExpNote(String expNote) {
		this.expNote = expNote;
	}
	/**
	 * CA签名记录
	 */
	private CnSignCa cnSignCa;
	
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}
	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}
	public String getEuSt() {
		return euSt;
	}
	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}
	private String descBody;
	private String dtType;
	private String dtRistype;
	private String dtColltype;
	private String dtContype;
	private String dtSamptype;
	private String dtPrestype;
	private String dtProperties;
	

	
	public String getDtProperties() {
		return dtProperties;
	}
	public void setDtProperties(String dtProperties) {
		this.dtProperties = dtProperties;
	}
	public String getDtPrestype() {
		return dtPrestype;
	}
	public void setDtPrestype(String dtPrestype) {
		this.dtPrestype = dtPrestype;
	}
	public String getDescBody() {
		return descBody;
	}
	public void setDescBody(String descBody) {
		this.descBody = descBody;
	}
	public String getDtType() {
		return dtType;
	}
	public void setDtType(String dtType) {
		this.dtType = dtType;
	}
	
	public String getDtRistype() {
		return dtRistype;
	}
	public void setDtRistype(String dtRistype) {
		this.dtRistype = dtRistype;
	}
	public String getDtColltype() {
		return dtColltype;
	}
	public void setDtColltype(String dtColltype) {
		this.dtColltype = dtColltype;
	}
	public String getDtContype() {
		return dtContype;
	}
	public void setDtContype(String dtContype) {
		this.dtContype = dtContype;
	}
	public String getDtSamptype() {
		return dtSamptype;
	}
	public void setDtSamptype(String dtSamptype) {
		this.dtSamptype = dtSamptype;
	}
	public String getDtUsagenote() {
		return dtUsagenote;
	}
	public void setDtUsagenote(String dtUsagenote) {
		this.dtUsagenote = dtUsagenote;
	}
	public Integer getFirstNum() {
		return firstNum;
	}
	public void setFirstNum(Integer firstNum) {
		this.firstNum = firstNum;
	}
	public String getFlagPivas() {
		return flagPivas;
	}
	public void setFlagPivas(String flagPivas) {
		this.flagPivas = flagPivas;
	}
	public String getDtHerbusage() {
		return dtHerbusage;
	}
	public void setDtHerbusage(String dtHerbusage) {
		this.dtHerbusage = dtHerbusage;
	}
	private String flagFasting;
	
	private String dtPatitrans;
	
	private String flagHerbSelf;
	
	private String pkDiag;
	
	private String nameDiag;

	private String nameSymp;

	private String codeDiag;

	private String codeSymp;

	private String pkSymp;
	
	private String flagBed;
	
	private String dtPois;

	/******************此处为手术医嘱新增字段，无法进行代码拆分，先支持功能********************/
	private String noteDise;//病情描述--暂时支持功能，后期拆分代码

    private  String purpose;//检查目的
    private  String descDiag;//诊断描述
    public String getNoteDise() {
        return noteDise;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getDescDiag() {
        return descDiag;
    }

    public void setDescDiag(String descDiag) {
        this.descDiag = descDiag;
    }

    public void setNoteDise(String noteDise) {
        this.noteDise = noteDise;
    }
    /**************************************/
    public String getDtPois() {
		return dtPois;
	}
	public void setDtPois(String dtPois) {
		this.dtPois = dtPois;
	}
	public String getFlagFasting() {
		return flagFasting;
	}
	public void setFlagFasting(String flagFasting) {
		this.flagFasting = flagFasting;
	}
	public String getDtPatitrans() {
		return dtPatitrans;
	}
	public void setDtPatitrans(String dtPatitrans) {
		this.dtPatitrans = dtPatitrans;
	}
	public String getFlagHerbSelf() {
		return flagHerbSelf;
	}
	public void setFlagHerbSelf(String flagHerbSelf) {
		this.flagHerbSelf = flagHerbSelf;
	}
	public String getPkDiag() {
		return pkDiag;
	}
	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getFlagBed() {
		return flagBed;
	}
	public void setFlagBed(String flagBed) {
		this.flagBed = flagBed;
	}
	public Integer getGroupno() {
		return groupno;
	}
	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
	}
	public String getPkEmpEx() {
		return pkEmpEx;
	}
	public void setPkEmpEx(String pkEmpEx) {
		this.pkEmpEx = pkEmpEx;
	}
	public String getNameEmpEx() {
		return nameEmpEx;
	}
	public void setNameEmpEx(String nameEmpEx) {
		this.nameEmpEx = nameEmpEx;
	}
	public String getCodeSupplyAdd() {
		return codeSupplyAdd;
	}
	public void setCodeSupplyAdd(String codeSupplyAdd) {
		this.codeSupplyAdd = codeSupplyAdd;
	}

    public String getEuOrdtype() {
        return euOrdtype;
    }

    public void setEuOrdtype(String euOrdtype) {
        this.euOrdtype = euOrdtype;
    }

    public String getDtHpprop() {
        return dtHpprop;
    }

    public void setDtHpprop(String dtHpprop) {
        this.dtHpprop = dtHpprop;
    }

    public Date getDateSend() {
        return dateSend;
    }

    public void setDateSend(Date dateSend) {
        this.dateSend = dateSend;
    }

    public String getFlagOcc() {
        return flagOcc;
    }

    public void setFlagOcc(String flagOcc) {
        this.flagOcc = flagOcc;
    }

    public String getPkDeptJob() {
        return pkDeptJob;
    }

    public void setPkDeptJob(String pkDeptJob) {
        this.pkDeptJob = pkDeptJob;
    }

    public String getPkDeptArea() {
        return pkDeptArea;
    }

    public void setPkDeptArea(String pkDeptArea) {
        this.pkDeptArea = pkDeptArea;
    }

    public String getFlagDisp() {
        return flagDisp;
    }

    public void setFlagDisp(String flagDisp) {
        this.flagDisp = flagDisp;
    }

    public String getQuanBack() {
        return quanBack;
    }

    public void setQuanBack(String quanBack) {
        this.quanBack = quanBack;
    }
	public String getPkDeptAreaapp() {
		return pkDeptAreaapp;
	}
	public void setPkDeptAreaapp(String pkDeptAreaapp) {
		this.pkDeptAreaapp = pkDeptAreaapp;
	}

    public String getFlagRescue() {
        return flagRescue;
    }

    public void setFlagRescue(String flagRescue) {
        this.flagRescue = flagRescue;
    }

    public Integer getPlanOccNum() {
        return planOccNum;
    }

    public void setPlanOccNum(Integer planOccNum) {
        this.planOccNum = planOccNum;
    }

    public Date getDateTake() {
        return dateTake;
    }

    public void setDateTake(Date dateTake) {
        this.dateTake = dateTake;
    }

    public String getDtReasonErase() {
        return dtReasonErase;
    }

    public void setDtReasonErase(String dtReasonErase) {
        this.dtReasonErase = dtReasonErase;
    }

    public String getFlagSpUnit() {
        return flagSpUnit;
    }

    public void setFlagSpUnit(String flagSpUnit) {
        this.flagSpUnit = flagSpUnit;
    }

    public String getEuEasonDis() {
        return euEasonDis;
    }

    public void setEuEasonDis(String euEasonDis) {
        this.euEasonDis = euEasonDis;
    }

    public Double getAmountDisc() {
        return amountDisc;
    }

    public void setAmountDisc(Double amountDisc) {
        this.amountDisc = amountDisc;
    }

    public String getFlagDiscst() {
        return flagDiscst;
    }

    public void setFlagDiscst(String flagDiscst) {
        this.flagDiscst = flagDiscst;
    }

    public String getFlagPlan() {
        return flagPlan;
    }

    public void setFlagPlan(String flagPlan) {
        this.flagPlan = flagPlan;
    }

    public String getNameSymp() {
        return nameSymp;
    }

    public void setNameSymp(String nameSymp) {
        this.nameSymp = nameSymp;
    }

    public String getCodeDiag() {
        return codeDiag;
    }

    public void setCodeDiag(String codeDiag) {
        this.codeDiag = codeDiag;
    }

    public String getCodeSymp() {
        return codeSymp;
    }

    public void setCodeSymp(String codeSymp) {
        this.codeSymp = codeSymp;
    }

    public String getPkSymp() {
        return pkSymp;
    }

    public void setPkSymp(String pkSymp) {
        this.pkSymp = pkSymp;
    }
}