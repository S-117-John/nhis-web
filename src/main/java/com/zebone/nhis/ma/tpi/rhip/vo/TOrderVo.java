package com.zebone.nhis.ma.tpi.rhip.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_ORDER 
 *
 * @since 2016-10-13 03:18:37
 */
@Table(value="CN_ORDER")
public class TOrderVo extends BaseModule  {

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

	@Field(value="ORDSN_PARENT")
    private Integer ordsnParent;

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

	@Field(value="UNIT_NAME_DOS")
    private String unitNameDos;
	
	@Field(value="QUAN")
    private Double quan;

	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="SUPPLY_SPCODE")
    private String supplySpcode;
	
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

	@Field(value="FLAG_SIGN")
    private String flagSign;
	
	@Field(value="EU_INTERN")
	private String euIntern;
	
	@Field(value="EU_ST")
	private String euSt;

	@Field(value="EMP_CODE")
	private String empCode;
	
	@Field(value="UNIT_NAME")
	private String unitName;

	@Field(value="EMP_CODE_CHK")
	private String empCodeChk;

	@Field(value="DEPT_CODE_ORIG")
	private String deptCodeOrig;

	@Field(value="DEPT_CODE")
	private String deptCode;
	
	@Field(value="DEPT_CODE_EXEC_ORIG")
	private String deptCodeExecOrig;
	
	@Field(value="GROUPNO")
	private String groupno;
	
	@Field(value="SUPPLY_CATE")
	private String supplyCate;
	
	
	@Field(value="DT_PHARM")
	private String dtPharm;
	
	@Field(value="PHARM_BACODE")
	private String pharmBacode;
	
	@Field(value="DEPT_NAME_EXEC")
	private String deptNameExec;
	
	@Field(value="DEPT_CODE_EXEC")
	private String deptCodeExec;
	
	@Field(value="DT_DOSAGE")
	private String dtDosage;
	
	@Field(value="APPR_NO")
	private String apprNo;
	
	@Field(value="FACTORY_CODE")
	private String factoryCode;

	@Field(value="FACTORY_NAME")
	private String factoryName;

	@Field(value="SUPPLY")
	private String supply;
	
	@Field(value="UNIT_CODE")
	private String unitCode;

	
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
	/**
	 * 变异项目
	 */
	private String pkCpexp;
	/**
	 * 变异项目备注
	 */
	private String expNote;


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

	public String getEuSt() {
		return euSt;
	}
	public void setEuSt(String euSt) {
		this.euSt = euSt;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getSupplySpcode() {
		return supplySpcode;
	}
	public void setSupplySpcode(String supplySpcode) {
		this.supplySpcode = supplySpcode;
	}
	public String getUnitNameDos() {
		return unitNameDos;
	}
	public void setUnitNameDos(String unitNameDos) {
		this.unitNameDos = unitNameDos;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getEmpCodeChk() {
		return empCodeChk;
	}
	public void setEmpCodeChk(String empCodeChk) {
		this.empCodeChk = empCodeChk;
	}
	public String getDeptCodeOrig() {
		return deptCodeOrig;
	}
	public void setDeptCodeOrig(String deptCodeOrig) {
		this.deptCodeOrig = deptCodeOrig;
	}
	public String getDeptCodeExecOrig() {
		return deptCodeExecOrig;
	}
	public void setDeptCodeExecOrig(String deptCodeExecOrig) {
		this.deptCodeExecOrig = deptCodeExecOrig;
	}
	public String getGroupno() {
		return groupno;
	}
	public void setGroupno(String groupno) {
		this.groupno = groupno;
	}
	public String getSupplyCate() {
		return supplyCate;
	}
	public void setSupplyCate(String supplyCate) {
		this.supplyCate = supplyCate;
	}
	public String getDtPharm() {
		return dtPharm;
	}
	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}
	public String getPharmBacode() {
		return pharmBacode;
	}
	public void setPharmBacode(String pharmBacode) {
		this.pharmBacode = pharmBacode;
	}
	public String getDeptNameExec() {
		return deptNameExec;
	}
	public void setDeptNameExec(String deptNameExec) {
		this.deptNameExec = deptNameExec;
	}
	public String getDeptCodeExec() {
		return deptCodeExec;
	}
	public void setDeptCodeExec(String deptCodeExec) {
		this.deptCodeExec = deptCodeExec;
	}
	public String getDtDosage() {
		return dtDosage;
	}
	public void setDtDosage(String dtDosage) {
		this.dtDosage = dtDosage;
	}
	public String getApprNo() {
		return apprNo;
	}
	public void setApprNo(String apprNo) {
		this.apprNo = apprNo;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getFactoryName() {
		return factoryName;
	}
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}
	public String getSupply() {
		return supply;
	}
	public void setSupply(String supply) {
		this.supply = supply;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
    
}