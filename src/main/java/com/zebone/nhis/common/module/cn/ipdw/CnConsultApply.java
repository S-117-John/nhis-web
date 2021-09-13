package com.zebone.nhis.common.module.cn.ipdw;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_CONSULT_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_CONSULT_APPLY")
public class CnConsultApply extends BaseModule  {

	@PK
	@Field(value="PK_CONS",id=KeyId.UUID)
    private String pkCons;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="DATE_CONS")
    private Date dateCons;
	
	@Field(value="EU_TYPE")
    private String euType;

	@Field(value="DT_CONLEVEL")
    private String dtConlevel;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="REASON")
    private String reason;

	@Field(value="ILL_SUMMARY")
    private String illSummary;
	
	@Field(value="CODE_CONSULT")
    private String codeConsult;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;
	/*
	 * 检查检验
	 */
	@Field(value="examine")
    private String examine;
	/*
	 * 诊断名称
	 */
	@Field(value="diagname")
    private String diagname;
	/*
	 * 查体
	 */
	@Field(value="objective")
    private String objective;
	
    private Integer ordsn;
    private Integer ordsnParent;
    private Integer groupno;
    private String pkPv;
    private String codeOrd;
    private String pkPi;
    private String nameOrd;
    private String codeApply;
    private String codeOrdtype;
    private String pkOrgExec;
    private String pkDeptExec;
    private Double priceCg;
    private String flagBl;
    private String pkOrd;
    private String flagEmer;
    private String noteOrd;
    private List<CnConsultResponse> consultResList;
	private List<CnConsultResponse> consultResListForDel;
	private CnOrder consultCnOrd;
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
	
	//CA认证信息
	private CnSignCa cnSignCa;
	
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}
	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}
	public String getPkCprec() {
		return pkCprec;
	}
	public void setPkCprec(String pkCprec) {
		this.pkCprec = pkCprec;
	}
	public String getPkCpphase() {
		return pkCpphase;
	}
	public void setPkCpphase(String pkCpphase) {
		this.pkCpphase = pkCpphase;
	}
	public String getPkCpexp() {
		return pkCpexp;
	}
	public void setPkCpexp(String pkCpexp) {
		this.pkCpexp = pkCpexp;
	}
	public String getNameExp() {
		return nameExp;
	}
	public void setNameExp(String nameExp) {
		this.nameExp = nameExp;
	}
	public String getExpNote() {
		return expNote;
	}
	public void setExpNote(String expNote) {
		this.expNote = expNote;
	}
	public CnOrder getConsultCnOrd() {
		return consultCnOrd;
	}
	public void setConsultCnOrd(CnOrder consultCnOrd) {
		this.consultCnOrd = consultCnOrd;
	}
	public String getNoteOrd() {
		return noteOrd;
	}
	public void setNoteOrd(String noteOrd) {
		this.noteOrd = noteOrd;
	}
	public String getCodeConsult() {
		return codeConsult;
	}
	public void setCodeConsult(String codeConsult) {
		this.codeConsult = codeConsult;
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
	public Double getPriceCg() {
		return priceCg;
	}
	public void setPriceCg(Double priceCg) {
		this.priceCg = priceCg;
	}
	public String getFlagBl() {
		return flagBl;
	}
	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
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
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
	public String getCodeOrd() {
		return codeOrd;
	}
	public void setCodeOrd(String codeOrd) {
		this.codeOrd = codeOrd;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
	public String getCodeApply() {
		return codeApply;
	}
	public void setCodeApply(String codeApply) {
		this.codeApply = codeApply;
	}
	public String getCodeOrdtype() {
		return codeOrdtype;
	}
	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}
	public List<CnConsultResponse> getConsultResList() {
		return consultResList;
	}
	public void setConsultResList(List<CnConsultResponse> consultResList) {
		this.consultResList = consultResList;
	}
	public List<CnConsultResponse> getConsultResListForDel() {
		return consultResListForDel;
	}
	public void setConsultResListForDel(List<CnConsultResponse> consultResListForDel) {
		this.consultResListForDel = consultResListForDel;
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
	public String getPkCons(){
        return this.pkCons;
    }
    public void setPkCons(String pkCons){
        this.pkCons = pkCons;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
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

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public String getEuType(){
        return this.euType;
    }
    public void setEuType(String euType){
        this.euType = euType;
    }

    public String getDtConlevel(){
        return this.dtConlevel;
    }
    public void setDtConlevel(String dtConlevel){
        this.dtConlevel = dtConlevel;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getReason(){
        return this.reason;
    }
    public void setReason(String reason){
        this.reason = reason;
    }

    public String getIllSummary(){
        return this.illSummary;
    }
    public void setIllSummary(String illSummary){
        this.illSummary = illSummary;
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
	public Date getDateCons() {
		return dateCons;
	}
	public void setDateCons(Date dateCons) {
		this.dateCons = dateCons;
	}
	public String getExamine() {
		return examine;
	}
	public void setExamine(String examine) {
		this.examine = examine;
	}
	public String getDiagname() {
		return diagname;
	}
	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}
	public String getObjective() {
		return objective;
	}
	public void setObjective(String objective) {
		this.objective = objective;
	}
	
}