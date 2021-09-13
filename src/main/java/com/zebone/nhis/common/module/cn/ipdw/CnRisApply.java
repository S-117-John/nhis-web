package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_RIS_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_RIS_APPLY")
public class CnRisApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDRIS",id=KeyId.UUID)
    private String pkOrdris;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="NOTE_DISE")
    private String noteDise;

	@Field(value="DT_RISTYPE")
    private String dtRistype;

	@Field(value="DESC_BODY")
    private String descBody;

	@Field(value="PURPOSE")
    private String purpose;

	@Field(value="PK_MSP")
    private String pkMsp;

	@Field(value="DATE_APPO")
    private Date dateAppo;

	@Field(value="DATE_EXAM")
    private Date dateExam;

	@Field(value="RIS_NOTICE")
    private String risNotice;

	@Field(value="TICKETNO")
    private Integer ticketno;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="FLAG_BED")
    private String flagBed;

	@Field(value="NOTE")
    private String note;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="NAME_DIAG")
    private String nameDiag;

	@Field(value="PK_EMP_APPO")
    private String pkEmpAppo;

	@Field(value="NAME_EMP_APPO")
    private String nameEmpAppo;

	@Field(value="FLAG_PRINT")
	private String flagPrint;
	
	/*标识医技科室是否打印*/
	@Field(value="FLAG_PRINT2")
	private String flagPrint2;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="FLAG_FASTING")
    private String flagFasting;
	
	@Field(value="DT_PATITRANS")
	private String dtPatitrans;
	
	@Field(value="MODITY_TIME")
	private Date modityTime;
	
	@Field(value="form_app")
	private String formApp;
	
	private Integer groupno;

    private Integer ordsn;

    private Integer ordsnParent;
    
    private Date dateStart;
    
    private String pkEmpInput;
    
    private String NameEmpInput;
	
    private String pkDept;
    
    
    public String getFlagPrint2() {
		return flagPrint2;
	}
	public void setFlagPrint2(String flagPrint2) {
		this.flagPrint2 = flagPrint2;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
	public String getNameEmpInput() {
		return NameEmpInput;
	}
	public void setNameEmpInput(String nameEmpInput) {
		NameEmpInput = nameEmpInput;
	}
	public String getPkEmpInput() {
		return pkEmpInput;
	}
	public void setPkEmpInput(String pkEmpInput) {
		this.pkEmpInput = pkEmpInput;
	}
	public Date getDateStart() {
		return dateStart;
	}
	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}
	public Integer getOrdsn() {
		return ordsn;
	}
	public void setOrdsn(Integer ordsn) {
		this.ordsn = ordsn;
	}
	public Integer getOrdsnParent() {
		return ordsnParent;
	}
	public void setOrdsnParent(Integer ordsnParent) {
		this.ordsnParent = ordsnParent;
	}
	public Integer getGroupno() {
		return groupno;
	}
	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
	}
	public String getFormApp() {
		return formApp;
	}
	public void setFormApp(String formApp) {
		this.formApp = formApp;
	}
	public String getPkOrdris(){
        return this.pkOrdris;
    }
    public void setPkOrdris(String pkOrdris){
        this.pkOrdris = pkOrdris;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }
    public String getNoteDise() {
		return noteDise;
	}
	public void setNoteDise(String noteDise) {
		this.noteDise = noteDise;
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

    public String getPurpose(){
        return this.purpose;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
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

    public Integer getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Integer ticketno){
        this.ticketno = ticketno;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getFlagBed(){
        return this.flagBed;
    }
    public void setFlagBed(String flagBed){
        this.flagBed = flagBed;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getNameDiag(){
        return this.nameDiag;
    }
    public void setNameDiag(String nameDiag){
        this.nameDiag = nameDiag;
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

    public String getFlagPrint(){
        return this.flagPrint;
    }
    public void setFlagPrint(String flagPrint){
        this.flagPrint = flagPrint;
    }

    public String getModifier(){
        return this.modifier;
    }
    public void setModifier(String modifier){
        this.modifier = modifier;
    }

    public Date getModityTime(){
        return this.modityTime;
    }
    public void setModityTime(Date modityTime){
        this.modityTime = modityTime;
    }
    /**
	 * 数据更新状态
	 */
	private String rowStatus;
	/**
	 * 申请单号
	 */
	private String codeApply;
	/**
	 * 就诊id
	 */
	private String pkPv;
	/**
	 * 患者主键
	 */
	private String pkPi;
	/**
	 * 执行机构
	 */
	private String pkOrgExec;
	/**
	 * 执行科室
	 */
	private String pkDeptExec;
	/**
	 * 医嘱状态
	 * @return
	 */
	private String euStatusOrd;
	/**
	 * 数量
	 */
	private double quan ;
	/**
	 * 加急标志
	 */
	private String flagEmer;
	/**
	 * 医嘱类型编码
	 */
	private String pkOrd;
	/**
	 * 医嘱编码
	 */
	private String codeOrd;
	/**
	 * 医嘱名称
	 */
	private String nameOrd;
	/**
	 * 开立病区
	 */
	private String pkDeptNs;
	/**
	 * 医嘱备注
	 */
	private String noteOrd;
	/**
	 * 计费标志
	 */
	private String flagBl;
	/**
	 * 医嘱类型
	 */
	private String codeOrdType;
	/**
	 * 婴儿序号
	 */
    private Integer infantNo;
	/**
	 * CA签名记录
	 */
	private CnSignCa cnSignCa;
	
	/**
	 * 路径启用记录
	 */
	private String pkCprec;
	//路径运用阶段
		private String pkCpphase;
	/**
	 * 变异项目
	 */
	private String pkCpexp;
	//变异原因名称
	private String nameExp;
	/**
	 * 变异项目备注
	 */
	private String expNote;
	
	
	
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getNameExp() {
		return nameExp;
	}
	public void setNameExp(String nameExp) {
		this.nameExp = nameExp;
	}
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}
	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}
	
	public Integer getInfantNo() {
		return infantNo;
	}
	public void setInfantNo(Integer infantNo) {
		this.infantNo = infantNo;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getCodeOrd() {
		return codeOrd;
	}
	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
	}
	
	public String getCodeOrdType() {
		return codeOrdType;
	}
	public void setCodeOrdType(String codeOrdType) {
		this.codeOrdType = codeOrdType;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public double getQuan() {
		return quan;
	}
	public void setQuan(double quan) {
		this.quan = quan;
	}
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getPkOrgExec() {
		return pkOrgExec;
	}
	public void setPkOrgExec(String pkOrgExec) {
		this.pkOrgExec = pkOrgExec;
	}
	public String getPkDeptExec() {
		return pkDeptExec;
	}
	public void setPkDeptExec(String pkDeptExec) {
		this.pkDeptExec = pkDeptExec;
	}
	public String getEuStatusOrd() {
		return euStatusOrd;
	}
	public void setEuStatusOrd(String euStatusOrd) {
		this.euStatusOrd = euStatusOrd;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
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
	private String priceCg;
	
	public String getPriceCg() {
		return priceCg;
	}
	public void setPriceCg(String priceCg) {
		this.priceCg = priceCg;
	}
	private String euIntern;

	public String getEuIntern() {
		return euIntern;
	}
	public void setEuIntern(String euIntern) {
		this.euIntern = euIntern;
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
}