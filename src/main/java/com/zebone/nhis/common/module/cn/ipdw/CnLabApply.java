package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_LAB_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_LAB_APPLY")
public class CnLabApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDLIS",id=KeyId.UUID)
    private String pkOrdlis;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="DESC_DIAG")
    private String descDiag;

	@Field(value="PURPOSE")
    private String purpose;

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

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="DATE_COL")
    private Date dateCol;

	@Field(value="DATE_ACPT")
    private Date dateAcpt;

	@Field(value="DATE_RPT")
    private Date dateRpt;

	@Field(value="NOTE")
    private String note;

	@Field(value="FLAG_PRT")
    private String flagPrt;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_EMP_COL")
    private String pkEmpCol;

	@Field(value="NAME_EMP_COL")
    private String nameEmpCol;

	@Field(value="FORM_APP")
    private String formApp;
	
	private Integer groupno;

    private Integer ordsn;

    private Integer ordsnParent;
    
    private Date dateStart;
    
    private String pkEmpInput;
    
    private String NameEmpInput;
	
    
    
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
    

    public Integer getGroupno() {
		return groupno;
	}
	public void setGroupno(Integer groupno) {
		this.groupno = groupno;
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
	public String getPkOrdlis(){
        return this.pkOrdlis;
    }
    public void setPkOrdlis(String pkOrdlis){
        this.pkOrdlis = pkOrdlis;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getDescDiag(){
        return this.descDiag;
    }
    public void setDescDiag(String descDiag){
        this.descDiag = descDiag;
    }

    public String getPurpose(){
        return this.purpose;
    }
    public void setPurpose(String purpose){
        this.purpose = purpose;
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

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
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
	 * 开立科室
	 */
	private String pkDept;
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
	 * 开立人
	 */
	private String nameEmpOrd;

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
	
	
	
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
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
	public String getCodeOrdType() {
		return codeOrdType;
	}
	public void setCodeOrdType(String codeOrdType) {
		this.codeOrdType = codeOrdType;
	}
	public String getRowStatus() {
		return rowStatus;
	}
	public void setRowStatus(String rowStatus) {
		this.rowStatus = rowStatus;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
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
	public double getQuan() {
		return quan;
	}
	public void setQuan(double quan) {
		this.quan = quan;
	}
	public String getFlagEmer() {
		return flagEmer;
	}
	public void setFlagEmer(String flagEmer) {
		this.flagEmer = flagEmer;
	}
	public String getPkOrd() {
		return pkOrd;
	}
	public void setPkOrd(String pkOrd) {
		this.pkOrd = pkOrd;
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
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
	public String getNameEmpOrd() {
		return nameEmpOrd;
	}
	public void setNameEmpOrd(String nameEmpOrd) {
		this.nameEmpOrd = nameEmpOrd;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
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
	public String getPkEmpCol() {
		return pkEmpCol;
	}
	public void setPkEmpCol(String pkEmpCol) {
		this.pkEmpCol = pkEmpCol;
	}
	public String getNameEmpCol() {
		return nameEmpCol;
	}
	public void setNameEmpCol(String nameEmpCol) {
		this.nameEmpCol = nameEmpCol;
	}
	public String getFormApp() {
		return formApp;
	}
	public void setFormApp(String formApp) {
		this.formApp = formApp;
	}
	
	
}