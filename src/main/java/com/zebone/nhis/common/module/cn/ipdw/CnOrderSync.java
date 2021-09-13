package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: CN_ORDER_SYNC 
 *
 * @since 2018-09-04 10:23:13
 */
@Table(value="CN_ORDER_SYNC")
public class CnOrderSync extends BaseModule  {

	@PK
	@Field(value="PK_CNORD_SYNC",id=KeyId.UUID)
    private String pkCnordSync;

	@PK
	@Field(value="PK_CNORD")
    private String pkCnord;
	
	@Field(value="EU_PVTYPE")
    private String euPvtype;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="CODE_OP")
    private String codeOp;
	
	@Field(value="CODE_IP")
    private String codeIp;
	
	@Field(value="NAME")
    private String name;
	
	@Field(value="BIRTH_DATE")
    private Date birthDate;
	
	@Field(value="DT_SEX")
    private String dtSex;
	
	@Field(value="CODE_PV")
    private String codePv;
	
	@Field(value="DATE_EFFE")
    private Date dateEffe;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;

	@Field(value="REQ_TYPE")
    private String reqType;

	@Field(value="EU_ALWAYS")
    private String euAlways;

	@Field(value="GROUPNO")
    private Integer groupno;

	@Field(value="ORDSN")
    private Long ordsn;

	@Field(value="ORDSN_PARENT")
    private Long ordsnParent;

	@Field(value="FLAG_MASTER")
    private String flagMaster;
	
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

	@Field(value="CODE_UNIT_DOS")
    private String codeUnitDos;
	
	@Field(value="PK_UNIT_DOS")
    private String pkUnitDos;

	@Field(value="QUAN")
    private Double quan;

	@Field(value="CODE_UNIT")
    private String codeUnit;
	
	@Field(value="PK_UNIT")
    private String pkUnit;

	@Field(value="QUAN_DISP")
    private String quanDisp;

	@Field(value="CODE_SUPPLY")
    private String codeSupply;

	@Field(value="QUAN_CG")
    private Double quanCg;

	@Field(value="PK_UNIT_CG")
    private String pkUnitCg;

	@Field(value="CODE_UNIT_CG")
    private String codeUnitCg;
	
	@Field(value="PACK_SIZE")
    private Integer packSize;
	
	@Field(value="PRICE")
    private Double price;
	
	@Field(value="PRICE_CG")
    private Double priceCg;

	@Field(value="NOTE_SUPPLY")
    private String noteSupply;

	@Field(value="DT_USAGENOTE")
    private String dtUsagenote;

	@Field(value="DAYS")
    private Integer days;

	@Field(value="DRIP_SPEED")
    private Integer dripSpeed;

	@Field(value="ORDS")
    private Integer ords;

	@Field(value="FLAG_FIRST")
    private String flagFirst;

	@Field(value="FIRST_NUM")
    private Integer firstNum;

	@Field(value="LAST_NUM")
    private Integer lastNum;

	@Field(value="PK_ORG_EXEC")
    private String pkOrgExec;

	@Field(value="CODE_DEPT_EXEC")
    private String codeDeptExec;

	@Field(value="PK_DEPT_EXEC")
    private String pkDeptExec;
	
	@Field(value="EU_STATUS_ORD")
    private String euStatusOrd;

	@Field(value="EU_HPTYPE")
    private String euHptype;
	
	@Field(value="CODE_STORE")
    private String codeStore;
	
	@Field(value="DATE_START")
    private Date dateStart;

	@Field(value="FLAG_DRUG")
    private String flagDrug;
	
	@Field(value="FLAG_PD")
    private String flagPd;
	
	@Field(value="EU_DRUGTYPE")
    private String euDrugtype;
	
	@Field(value="FACTORY_CODE")
    private String factoryCode;

	@Field(value="FLAG_SELF")
    private String flagSelf;

	@Field(value="FLAG_NOTE")
    private String flagNote;

	@Field(value="FLAG_BASE")
    private String flagBase;

	@Field(value="FLAG_BL")
    private String flagBl;

	@Field(value="EU_TYPE_BL")
    private String euTypeBl;
	
	@Field(value="PK_WG")
    private String pkWg;

	@Field(value="DATE_ENTER")
    private Date dateEnter;

	@Field(value="PK_EMP_INPUT")
    private String pkEmpInput;

	@Field(value="NAME_EMP_INPUT")
    private String nameEmpInput;

	@Field(value="FLAG_ITC")
    private String flagItc;

	@Field(value="EU_INTERN")
    private String euIntern;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="CODE_DEPT_NS")
    private String codeDeptNs;
	
	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="CODE_DEPT")
    private String codeDept;
	
	@Field(value="PK_EMP_ORD")
    private String pkEmpOrd;

	@Field(value="CODE_EMP_ORD")
    private String codeEmpOrd;
	
	@Field(value="NAME_EMP_ORD")
    private String nameEmpOrd;

	@Field(value="FLAG_SIGN")
    private String flagSign;

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

	@Field(value="CODE_EMP_STOP")
    private String codeEmpStop;
	
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

	@Field(value="CODE_EMP_ERASE")
    private String codeEmpErase;
	
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
    private String infantNo;

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

	@Field(value="EU_ST")
    private String euSt;

	@Field(value="FLAG_EMER")
    private String flagEmer;

	@Field(value="FLAG_THERA")
    private String flagThera;

	@Field(value="FLAG_PREV")
    private String flagPrev;

	@Field(value="FLAG_FIT")
    private String flagFit;

	@Field(value="FLAG_PIVAS")
    private String flagPivas;

	@Field(value="QUAN_BED")
    private Double quanBed;

	@Field(value="NOTE_ORD")
    private String noteOrd;

	@Field(value="FLAG_SET")
    private String flagSet;
	
	@Field(value="SET_CODE")
    private String setCode;
	
	@Field(value="ORDER_NO")
    private Long orderNo;
	
	@Field(value="ORDER_GROUP_NO")
    private Long orderGroupNo;
	
	@Field(value="SORT_IV")
    private Integer sortIv;

	@Field(value="DT_HERBUSAGE")
    private String dtHerbusage;

	@Field(value="PK_CNORD_RL")
    private String pkCnordRl;

	@Field(value="RATIO_HP")
    private Double ratioHp;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_ORDLIS")
    private String pkOrdlis;

	@Field(value="DESC_DIAG_LIS")
    private String descDiagLis;

	@Field(value="PURPOSE_LIS")
    private String purposeLis;

	@Field(value="DT_SAMPTYPE")
    private String dtSamptype;

	@Field(value="DT_TUBETYPE")
    private String dtTubetype;

	@Field(value="DT_COLTYPE")
    private String dtColtype;

	@Field(value="SAMP_NO")
    private String sampNo;

	@Field(value="PK_DEPT_COL")
    private String pkDeptCol;

	@Field(value="EU_STATUS_LIS")
    private String euStatusLis;

	@Field(value="DATE_COL")
    private Date dateCol;

	@Field(value="DATE_ACPT")
    private Date dateAcpt;

	@Field(value="DATE_RPT")
    private Date dateRpt;

	@Field(value="NOTE_LIS")
    private String noteLis;

	@Field(value="FLAG_PRT_LIS")
    private String flagPrtLis;

	@Field(value="PK_EMP_COL")
    private String pkEmpCol;

	@Field(value="NAME_EMP_COL")
    private String nameEmpCol;

	@Field(value="FORM_APP_LIS")
    private String formAppLis;

	@Field(value="PK_ORDRIS")
    private String pkOrdris;

	@Field(value="DT_RISTYPE")
    private String dtRistype;

	@Field(value="DESC_BODY")
    private String descBody;

	@Field(value="PURPOSE_RIS")
    private String purposeRis;

	@Field(value="PK_MSP")
    private String pkMsp;

	@Field(value="DATE_APPO")
    private Date dateAppo;

	@Field(value="DATE_EXAM")
    private Date dateExam;

	@Field(value="RIS_NOTICE")
    private String risNotice;

	@Field(value="TICKETNO")
    private Long ticketno;

	@Field(value="EU_STATUS_RIS")
    private String euStatusRis;

	@Field(value="FLAG_BED")
    private String flagBed;

	@Field(value="NOTE_RIS")
    private String noteRis;

	@Field(value="PK_DIAG_RIS")
    private String pkDiagRis;

	@Field(value="NAME_DIAG_RIS")
    private String nameDiagRis;

	@Field(value="PK_EMP_APPO")
    private String pkEmpAppo;

	@Field(value="NAME_EMP_APPO")
    private String nameEmpAppo;

	@Field(value="FLAG_PRINT_RIS")
    private String flagPrintRis;

	@Field(value="NOTE_DISE")
    private String noteDise;

	@Field(value="FLAG_FASTING")
    private String flagFasting;

	@Field(value="DT_PATITRANS")
    private String dtPatitrans;

	@Field(value="FORM_APP_RIS")
    private String formAppRis;

	@Field(value="PK_ORDBT")
    private String pkOrdbt;

	@Field(value="PK_DIAG_TRANS")
    private String pkDiagTrans;

	@Field(value="DESC_DIAG_TRANS")
    private String descDiagTrans;

	@Field(value="DT_BTTYPE")
    private String dtBttype;

	@Field(value="DT_BT_ABO")
    private String dtBtAbo;

	@Field(value="DT_BT_RH")
    private String dtBtRh;

	@Field(value="FLAG_LAB")
    private String flagLab;

	@Field(value="FLAG_BTHIS")
    private String flagBthis;

	@Field(value="BT_CONTENT")
    private String btContent;

	@Field(value="PK_UNIT_BT")
    private String pkUnitBt;

	@Field(value="QUAN_BT")
    private Long quanBt;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="FLAG_PREG")
    private String flagPreg;

	@Field(value="FLAG_AL")
    private String flagAl;

	@Field(value="FLAG_BP")
    private String flagBp;

	@Field(value="DATE_BP")
    private Date dateBp;

	@Field(value="QUAN_BP")
    private Long quanBp;

	@Field(value="BARCODE_BP")
    private String barcodeBp;

	@Field(value="PK_EMP_BP")
    private String pkEmpBp;

	@Field(value="NAME_EMP_BP")
    private String nameEmpBp;

	@Field(value="EU_STATUS_TRANS")
    private String euStatusTrans;

	@Field(value="NOTE_TRANS")
    private String noteTrans;

	@Field(value="OPT_TYPE")
    private String optType;

	@Field(value="REMARK")
    private String remark;

	@Field(value="DATE_OPER")
    private Date dateOper;

	@Field(value="CODE_EMP")
    private String codeEmp;

	@Field(value="NAME_EMP")
    private String nameEmp;

	@Field(value="EU_STATUS_PROC")
    private String euStatusProc;
	
	@Field(value="PK_CGIP")
    private String pkCgip;
	
	@Field(value="REQ_SN")
    private Integer reqSn;
	
	@Field(value="req_tab_no")
    private Integer reqTabNo;
	
    private String samptypeName;
	
    private String codeOrdSeq;
    
    private String reqTmpNo;
    
    private String codeType;
    
    public String getPkCnordSync() {
		return pkCnordSync;
	}
	public void setPkCnordSync(String pkCnordSync) {
		this.pkCnordSync = pkCnordSync;
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

    public String getReqType(){
        return this.reqType;
    }
    public void setReqType(String reqType){
        this.reqType = reqType;
    }

    public String getEuAlways(){
        return this.euAlways;
    }
    public void setEuAlways(String euAlways){
        this.euAlways = euAlways;
    }

    public Integer getGroupno(){
        return this.groupno;
    }
    public void setGroupno(Integer groupno){
        this.groupno = groupno;
    }

    public Long getOrdsn(){
        return this.ordsn;
    }
    public void setOrdsn(Long ordsn){
        this.ordsn = ordsn;
    }

    public Long getOrdsnParent(){
        return this.ordsnParent;
    }
    public void setOrdsnParent(Long ordsnParent){
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

    public String getQuanDisp(){
        return this.quanDisp;
    }
    public void setQuanDisp(String quanDisp){
        this.quanDisp = quanDisp;
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

    public Integer getPackSize(){
        return this.packSize;
    }
    public void setPackSize(Integer packSize){
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

    public String getDtUsagenote(){
        return this.dtUsagenote;
    }
    public void setDtUsagenote(String dtUsagenote){
        this.dtUsagenote = dtUsagenote;
    }

    public Integer getDays(){
        return this.days;
    }
    public void setDays(Integer days){
        this.days = days;
    }

    public Integer getDripSpeed(){
        return this.dripSpeed;
    }
    public void setDripSpeed(Integer dripSpeed){
        this.dripSpeed = dripSpeed;
    }

    public Integer getOrds(){
        return this.ords;
    }
    public void setOrds(Integer ords){
        this.ords = ords;
    }

    public String getFlagFirst(){
        return this.flagFirst;
    }
    public void setFlagFirst(String flagFirst){
        this.flagFirst = flagFirst;
    }

    public Integer getFirstNum(){
        return this.firstNum;
    }
    public void setFirstNum(Integer firstNum){
        this.firstNum = firstNum;
    }

    public Integer getLastNum(){
        return this.lastNum;
    }
    public void setLastNum(Integer lastNum){
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

    public Date getDateStart(){
        return this.dateStart;
    }
    public void setDateStart(Date dateStart){
        this.dateStart = dateStart;
    }

    public String getFlagDrug(){
        return this.flagDrug;
    }
    public void setFlagDrug(String flagDrug){
        this.flagDrug = flagDrug;
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

    public String getPkWg(){
        return this.pkWg;
    }
    public void setPkWg(String pkWg){
        this.pkWg = pkWg;
    }

    public Date getDateEnter(){
        return this.dateEnter;
    }
    public void setDateEnter(Date dateEnter){
        this.dateEnter = dateEnter;
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

    public String getFlagItc(){
        return this.flagItc;
    }
    public void setFlagItc(String flagItc){
        this.flagItc = flagItc;
    }

    public String getEuIntern(){
        return this.euIntern;
    }
    public void setEuIntern(String euIntern){
        this.euIntern = euIntern;
    }

    public String getPkDeptNs(){
        return this.pkDeptNs;
    }
    public void setPkDeptNs(String pkDeptNs){
        this.pkDeptNs = pkDeptNs;
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

    public String getFlagSign(){
        return this.flagSign;
    }
    public void setFlagSign(String flagSign){
        this.flagSign = flagSign;
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

    public String getInfantNo(){
        return this.infantNo;
    }
    public void setInfantNo(String infantNo){
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

    public String getEuSt(){
        return this.euSt;
    }
    public void setEuSt(String euSt){
        this.euSt = euSt;
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

    public String getFlagPivas(){
        return this.flagPivas;
    }
    public void setFlagPivas(String flagPivas){
        this.flagPivas = flagPivas;
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

    public Integer getSortIv(){
        return this.sortIv;
    }
    public void setSortIv(Integer sortIv){
        this.sortIv = sortIv;
    }

    public String getDtHerbusage(){
        return this.dtHerbusage;
    }
    public void setDtHerbusage(String dtHerbusage){
        this.dtHerbusage = dtHerbusage;
    }

    public String getPkCnordRl(){
        return this.pkCnordRl;
    }
    public void setPkCnordRl(String pkCnordRl){
        this.pkCnordRl = pkCnordRl;
    }

    public Double getRatioHp(){
        return this.ratioHp;
    }
    public void setRatioHp(Double ratioHp){
        this.ratioHp = ratioHp;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }

    public String getPkOrdlis(){
        return this.pkOrdlis;
    }
    public void setPkOrdlis(String pkOrdlis){
        this.pkOrdlis = pkOrdlis;
    }

    public String getDescDiagLis(){
        return this.descDiagLis;
    }
    public void setDescDiagLis(String descDiagLis){
        this.descDiagLis = descDiagLis;
    }

    public String getPurposeLis(){
        return this.purposeLis;
    }
    public void setPurposeLis(String purposeLis){
        this.purposeLis = purposeLis;
    }

    public String getDtSamptype(){
        return this.dtSamptype;
    }
    public void setDtSamptype(String dtSamptype){
        this.dtSamptype = dtSamptype;
    }

    public String getDtTubetype(){
        return this.dtTubetype;
    }
    public void setDtTubetype(String dtTubetype){
        this.dtTubetype = dtTubetype;
    }

    public String getDtColtype(){
        return this.dtColtype;
    }
    public void setDtColtype(String dtColtype){
        this.dtColtype = dtColtype;
    }

    public String getSampNo(){
        return this.sampNo;
    }
    public void setSampNo(String sampNo){
        this.sampNo = sampNo;
    }

    public String getPkDeptCol(){
        return this.pkDeptCol;
    }
    public void setPkDeptCol(String pkDeptCol){
        this.pkDeptCol = pkDeptCol;
    }

    public String getEuStatusLis(){
        return this.euStatusLis;
    }
    public void setEuStatusLis(String euStatusLis){
        this.euStatusLis = euStatusLis;
    }

    public Date getDateCol(){
        return this.dateCol;
    }
    public void setDateCol(Date dateCol){
        this.dateCol = dateCol;
    }

    public Date getDateAcpt(){
        return this.dateAcpt;
    }
    public void setDateAcpt(Date dateAcpt){
        this.dateAcpt = dateAcpt;
    }

    public Date getDateRpt(){
        return this.dateRpt;
    }
    public void setDateRpt(Date dateRpt){
        this.dateRpt = dateRpt;
    }

    public String getNoteLis(){
        return this.noteLis;
    }
    public void setNoteLis(String noteLis){
        this.noteLis = noteLis;
    }

    public String getFlagPrtLis(){
        return this.flagPrtLis;
    }
    public void setFlagPrtLis(String flagPrtLis){
        this.flagPrtLis = flagPrtLis;
    }

    public String getPkEmpCol(){
        return this.pkEmpCol;
    }
    public void setPkEmpCol(String pkEmpCol){
        this.pkEmpCol = pkEmpCol;
    }

    public String getNameEmpCol(){
        return this.nameEmpCol;
    }
    public void setNameEmpCol(String nameEmpCol){
        this.nameEmpCol = nameEmpCol;
    }

    public String getFormAppLis(){
        return this.formAppLis;
    }
    public void setFormAppLis(String formAppLis){
        this.formAppLis = formAppLis;
    }

    public String getPkOrdris(){
        return this.pkOrdris;
    }
    public void setPkOrdris(String pkOrdris){
        this.pkOrdris = pkOrdris;
    }

    public String getDtRistype(){
        return this.dtRistype;
    }
    public void setDtRistype(String dtRistype){
        this.dtRistype = dtRistype;
    }

    public String getDescBody(){
        return this.descBody;
    }
    public void setDescBody(String descBody){
        this.descBody = descBody;
    }

    public String getPurposeRis(){
        return this.purposeRis;
    }
    public void setPurposeRis(String purposeRis){
        this.purposeRis = purposeRis;
    }

    public String getPkMsp(){
        return this.pkMsp;
    }
    public void setPkMsp(String pkMsp){
        this.pkMsp = pkMsp;
    }

    public Date getDateAppo(){
        return this.dateAppo;
    }
    public void setDateAppo(Date dateAppo){
        this.dateAppo = dateAppo;
    }

    public Date getDateExam(){
        return this.dateExam;
    }
    public void setDateExam(Date dateExam){
        this.dateExam = dateExam;
    }

    public String getRisNotice(){
        return this.risNotice;
    }
    public void setRisNotice(String risNotice){
        this.risNotice = risNotice;
    }

    public Long getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Long ticketno){
        this.ticketno = ticketno;
    }

    public String getEuStatusRis(){
        return this.euStatusRis;
    }
    public void setEuStatusRis(String euStatusRis){
        this.euStatusRis = euStatusRis;
    }

    public String getFlagBed(){
        return this.flagBed;
    }
    public void setFlagBed(String flagBed){
        this.flagBed = flagBed;
    }

    public String getNoteRis(){
        return this.noteRis;
    }
    public void setNoteRis(String noteRis){
        this.noteRis = noteRis;
    }

    public String getPkDiagRis(){
        return this.pkDiagRis;
    }
    public void setPkDiagRis(String pkDiagRis){
        this.pkDiagRis = pkDiagRis;
    }

    public String getNameDiagRis(){
        return this.nameDiagRis;
    }
    public void setNameDiagRis(String nameDiagRis){
        this.nameDiagRis = nameDiagRis;
    }

    public String getPkEmpAppo(){
        return this.pkEmpAppo;
    }
    public void setPkEmpAppo(String pkEmpAppo){
        this.pkEmpAppo = pkEmpAppo;
    }

    public String getNameEmpAppo(){
        return this.nameEmpAppo;
    }
    public void setNameEmpAppo(String nameEmpAppo){
        this.nameEmpAppo = nameEmpAppo;
    }

    public String getFlagPrintRis(){
        return this.flagPrintRis;
    }
    public void setFlagPrintRis(String flagPrintRis){
        this.flagPrintRis = flagPrintRis;
    }

    public String getNoteDise(){
        return this.noteDise;
    }
    public void setNoteDise(String noteDise){
        this.noteDise = noteDise;
    }

    public String getFlagFasting(){
        return this.flagFasting;
    }
    public void setFlagFasting(String flagFasting){
        this.flagFasting = flagFasting;
    }

    public String getDtPatitrans(){
        return this.dtPatitrans;
    }
    public void setDtPatitrans(String dtPatitrans){
        this.dtPatitrans = dtPatitrans;
    }

    public String getFormAppRis(){
        return this.formAppRis;
    }
    public void setFormAppRis(String formAppRis){
        this.formAppRis = formAppRis;
    }

    public String getPkOrdbt(){
        return this.pkOrdbt;
    }
    public void setPkOrdbt(String pkOrdbt){
        this.pkOrdbt = pkOrdbt;
    }

    public String getPkDiagTrans(){
        return this.pkDiagTrans;
    }
    public void setPkDiagTrans(String pkDiagTrans){
        this.pkDiagTrans = pkDiagTrans;
    }

    public String getDescDiagTrans(){
        return this.descDiagTrans;
    }
    public void setDescDiagTrans(String descDiagTrans){
        this.descDiagTrans = descDiagTrans;
    }

    public String getDtBttype(){
        return this.dtBttype;
    }
    public void setDtBttype(String dtBttype){
        this.dtBttype = dtBttype;
    }

    public String getDtBtAbo(){
        return this.dtBtAbo;
    }
    public void setDtBtAbo(String dtBtAbo){
        this.dtBtAbo = dtBtAbo;
    }

    public String getDtBtRh(){
        return this.dtBtRh;
    }
    public void setDtBtRh(String dtBtRh){
        this.dtBtRh = dtBtRh;
    }

    public String getFlagLab(){
        return this.flagLab;
    }
    public void setFlagLab(String flagLab){
        this.flagLab = flagLab;
    }

    public String getFlagBthis(){
        return this.flagBthis;
    }
    public void setFlagBthis(String flagBthis){
        this.flagBthis = flagBthis;
    }

    public String getBtContent(){
        return this.btContent;
    }
    public void setBtContent(String btContent){
        this.btContent = btContent;
    }

    public String getPkUnitBt(){
        return this.pkUnitBt;
    }
    public void setPkUnitBt(String pkUnitBt){
        this.pkUnitBt = pkUnitBt;
    }

    public Long getQuanBt(){
        return this.quanBt;
    }
    public void setQuanBt(Long quanBt){
        this.quanBt = quanBt;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public String getFlagPreg(){
        return this.flagPreg;
    }
    public void setFlagPreg(String flagPreg){
        this.flagPreg = flagPreg;
    }

    public String getFlagAl(){
        return this.flagAl;
    }
    public void setFlagAl(String flagAl){
        this.flagAl = flagAl;
    }

    public String getFlagBp(){
        return this.flagBp;
    }
    public void setFlagBp(String flagBp){
        this.flagBp = flagBp;
    }

    public Date getDateBp(){
        return this.dateBp;
    }
    public void setDateBp(Date dateBp){
        this.dateBp = dateBp;
    }

    public Long getQuanBp(){
        return this.quanBp;
    }
    public void setQuanBp(Long quanBp){
        this.quanBp = quanBp;
    }

    public String getBarcodeBp(){
        return this.barcodeBp;
    }
    public void setBarcodeBp(String barcodeBp){
        this.barcodeBp = barcodeBp;
    }

    public String getPkEmpBp(){
        return this.pkEmpBp;
    }
    public void setPkEmpBp(String pkEmpBp){
        this.pkEmpBp = pkEmpBp;
    }

    public String getNameEmpBp(){
        return this.nameEmpBp;
    }
    public void setNameEmpBp(String nameEmpBp){
        this.nameEmpBp = nameEmpBp;
    }

    public String getEuStatusTrans(){
        return this.euStatusTrans;
    }
    public void setEuStatusTrans(String euStatusTrans){
        this.euStatusTrans = euStatusTrans;
    }

    public String getNoteTrans(){
        return this.noteTrans;
    }
    public void setNoteTrans(String noteTrans){
        this.noteTrans = noteTrans;
    }

    public String getOptType(){
        return this.optType;
    }
    public void setOptType(String optType){
        this.optType = optType;
    }

    public String getRemark(){
        return this.remark;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }

    public Date getDateOper(){
        return this.dateOper;
    }
    public void setDateOper(Date dateOper){
        this.dateOper = dateOper;
    }

    public String getCodeEmp(){
        return this.codeEmp;
    }
    public void setCodeEmp(String codeEmp){
        this.codeEmp = codeEmp;
    }

    public String getNameEmp(){
        return this.nameEmp;
    }
    public void setNameEmp(String nameEmp){
        this.nameEmp = nameEmp;
    }
	public String getEuStatusProc() {
		return euStatusProc;
	}
	public void setEuStatusProc(String euStatusProc) {
		this.euStatusProc = euStatusProc;
	}
	public String getCodePv() {
		return codePv;
	}
	public void setCodePv(String codePv) {
		this.codePv = codePv;
	}
	public String getCodeDeptExec() {
		return codeDeptExec;
	}
	public void setCodeDeptExec(String codeDeptExec) {
		this.codeDeptExec = codeDeptExec;
	}
	public String getEuDrugtype() {
		return euDrugtype;
	}
	public void setEuDrugtype(String euDrugtype) {
		this.euDrugtype = euDrugtype;
	}
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	public String getCodeDeptNs() {
		return codeDeptNs;
	}
	public void setCodeDeptNs(String codeDeptNs) {
		this.codeDeptNs = codeDeptNs;
	}
	public String getCodeDept() {
		return codeDept;
	}
	public void setCodeDept(String codeDept) {
		this.codeDept = codeDept;
	}
	public String getCodeEmpOrd() {
		return codeEmpOrd;
	}
	public void setCodeEmpOrd(String codeEmpOrd) {
		this.codeEmpOrd = codeEmpOrd;
	}
	public String getCodeEmpStop() {
		return codeEmpStop;
	}
	public void setCodeEmpStop(String codeEmpStop) {
		this.codeEmpStop = codeEmpStop;
	}
	public String getCodeEmpErase() {
		return codeEmpErase;
	}
	public void setCodeEmpErase(String codeEmpErase) {
		this.codeEmpErase = codeEmpErase;
	}
	public String getFlagSet() {
		return flagSet;
	}
	public void setFlagSet(String flagSet) {
		this.flagSet = flagSet;
	}
	public String getSetCode() {
		return setCode;
	}
	public void setSetCode(String setCode) {
		this.setCode = setCode;
	}
	public Long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}
	public Long getOrderGroupNo() {
		return orderGroupNo;
	}
	public void setOrderGroupNo(Long orderGroupNo) {
		this.orderGroupNo = orderGroupNo;
	}
	public String getEuHptype() {
		return euHptype;
	}
	public void setEuHptype(String euHptype) {
		this.euHptype = euHptype;
	}
	public String getCodeStore() {
		return codeStore;
	}
	public void setCodeStore(String codeStore) {
		this.codeStore = codeStore;
	}
	public String getFlagPd() {
		return flagPd;
	}
	public void setFlagPd(String flagPd) {
		this.flagPd = flagPd;
	}
	public String getCodeUnitDos() {
		return codeUnitDos;
	}
	public void setCodeUnitDos(String codeUnitDos) {
		this.codeUnitDos = codeUnitDos;
	}
	public String getCodeUnit() {
		return codeUnit;
	}
	public void setCodeUnit(String codeUnit) {
		this.codeUnit = codeUnit;
	}
	public String getCodeUnitCg() {
		return codeUnitCg;
	}
	public void setCodeUnitCg(String codeUnitCg) {
		this.codeUnitCg = codeUnitCg;
	}
	public String getEuTypeBl() {
		return euTypeBl;
	}
	public void setEuTypeBl(String euTypeBl) {
		this.euTypeBl = euTypeBl;
	}
	public String getPkCgip() {
		return pkCgip;
	}
	public void setPkCgip(String pkCgip) {
		this.pkCgip = pkCgip;
	}
	public String getFlagMaster() {
		return flagMaster;
	}
	public void setFlagMaster(String flagMaster) {
		this.flagMaster = flagMaster;
	}
	public String getCodeOp() {
		return codeOp;
	}
	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	public String getCodeIp() {
		return codeIp;
	}
	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}
	public Integer getReqSn() {
		return reqSn;
	}
	public void setReqSn(Integer reqSn) {
		this.reqSn = reqSn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	public String getDtSex() {
		return dtSex;
	}
	public void setDtSex(String dtSex) {
		this.dtSex = dtSex;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getReqTabNo() {
		return reqTabNo;
	}
	public void setReqTabNo(Integer reqTabNo) {
		this.reqTabNo = reqTabNo;
	}
	public String getSamptypeName() {
		return samptypeName;
	}
	public void setSamptypeName(String samptypeName) {
		this.samptypeName = samptypeName;
	}
	public String getCodeOrdSeq() {
		return codeOrdSeq;
	}
	public void setCodeOrdSeq(String codeOrdSeq) {
		this.codeOrdSeq = codeOrdSeq;
	}
	public String getReqTmpNo() {
		return reqTmpNo;
	}
	public void setReqTmpNo(String reqTmpNo) {
		this.reqTmpNo = reqTmpNo;
	}
	public String getCodeType() {
		return codeType;
	}
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
	
}