package com.zebone.nhis.common.module.cn.ipdw;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.nhis.common.module.cn.cp.CpRecExp;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: CN_OP_APPLY 
 *
 * @since 2016-09-12 10:30:28
 */
@Table(value="CN_OP_APPLY")
public class CnOpApply extends BaseModule  {

	@PK
	@Field(value="PK_ORDOP",id=KeyId.UUID)
    private String pkOrdop;

	@Field(value="PK_CNORD")
    private String pkCnord;

	@Field(value="EU_OPTYPE")
    private String euOptype;

	@Field(value="PK_DIAG_PRE")
    private String pkDiagPre;

	@Field(value="DESC_DIAG_PRE")
    private String descDiagPre;

	@Field(value="DT_OPLEVEL")
    private String dtOplevel;

	@Field(value="PK_OP")
    private String pkOp;

	@Field(value="DESC_OP")
    private String descOp;

	@Field(value="DESC_OP_SUB")
    private String descOpSub;

	@Field(value="DT_ANAE")
    private String dtAnae;
	
	@Field(value="DT_ASALEVEL")
    private String dtAsalevel;

	@Field(value="DT_ASEPSIS")
    private String dtAsepsis;

	@Field(value="DT_POSI")
    private String dtPosi;

	@Field(value="PK_EMP_PHY_OP")
    private String pkEmpPhyOp;

	@Field(value="NAME_EMP_PHY_OP")
    private String nameEmpPhyOp;

	@Field(value="PK_EMP_ANAE")
    private String pkEmpAnae;

	@Field(value="NAME_EMP_ANAE")
    private String nameEmpAnae;

	@Field(value="PK_EMP_ASIS")
    private String pkEmpAsis;

	@Field(value="NAME_EMP_ASIS")
    private String nameEmpAsis;

	@Field(value="PK_EMP_ASIS2")
    private String pkEmpAsis2;

	@Field(value="NAME_EMP_ASIS2")
    private String nameEmpAsis2;

	@Field(value="PK_EMP_ASIS3")
    private String pkEmpAsis3;

	@Field(value="NAME_EMP_ASIS3")
    private String nameEmpAsis3;

	@Field(value="PK_EMP_SCRUB")
    private String pkEmpScrub;

	@Field(value="NAME_EMP_SCRUB")
    private String nameEmpScrub;

	@Field(value="PK_EMP_CIRCUL")
    private String pkEmpCircul;

	@Field(value="NAME_EMP_CRICUL")
    private String nameEmpCricul;

	@Field(value="DT_OPBODY")
    private String dtOpbody;

	@Field(value="DATE_APPLY")
    private Date dateApply;

	@Field(value="DATE_PLAN")
    private Date datePlan;

	@Field(value="PK_OPT")
    private String pkOpt;

	@Field(value="DATE_CONFIRM")
    private Date dateConfirm;

	@Field(value="TICKETNO")
    private Integer ticketno;

	@Field(value="DATE_BEGIN")
    private Date dateBegin;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="NOTE")
    private String note;

	@Field(value="DATE_CONFIRM_DEPT")
    private Date dateConfirmDept;

	@Field(userfield="pkEmp",userfieldscop=FieldType.ALL)
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="FLAG_AGAIN")
    private String flagAgain;
	
	@Field(value="DESC_ICJD")
    private  String descIcjd;
	
	@Field(value="DESC_UNICJD")
    private String descUnicjd;
	
	@Field(value="PURP_AGAIN")
    private String purpAgain;
	
	@Field(value="DATE_HEAD")
	private Date dateHead;
	
	@Field(value="FLAG_HEAD")
	private String flagHead;
	
	@Field(value="PK_EMP_HEAD")
	private String pkEmpHead;
	
	@Field(value="NAME_EMP_HEAD")
	private String nameEmpHead;
	
	@Field(value="WEIGHT")
	private Double weight;
	
	@Field(value="DT_BLOOD_ABO")
	private String dtBloodAbo;
	
	@Field(value="DT_BLOOD_RH")
	private String dtBloodRh;
	
	@Field(value="DESC_INFEC")
	private String descInfec;
	
	@Field(value="PK_DEPT_ANAE")
	private String pkDeptAnae;
	
	@Field(value="DESC_CPB")
	private String descCpb;
	
	@Field(value="FLAG_ED")
	private String flagEd;
	
	@Field(value="SPEC_EQUIPMENT")
	private String specEquipment;
	
	@Field(value="DURATION")
	private BigDecimal duration;
    @Field(value="NAME_OP")
	private String nameOp;
    @Field(value="FLAG_FROZEN")
    private String flagFrozen; //术中冰冻
    @Field(value="FLAG_CARM")
    private String flagCarm;//C型臂
    @Field(value = "FLAG_FINISH_ANAE")
    private String flagFinishAnae; //麻醉完成标记

    @Field(value = "EU_ERLEVEL")
    private String euErlevel; //急诊手术级别

	public String getPkOrdop(){
        return this.pkOrdop;
    }
    public void setPkOrdop(String pkOrdop){
        this.pkOrdop = pkOrdop;
    }

    public String getPkCnord(){
        return this.pkCnord;
    }
    public void setPkCnord(String pkCnord){
        this.pkCnord = pkCnord;
    }

    public String getEuOptype(){
        return this.euOptype;
    }
    public void setEuOptype(String euOptype){
        this.euOptype = euOptype;
    }

    public String getPkDiagPre(){
        return this.pkDiagPre;
    }
    public void setPkDiagPre(String pkDiagPre){
        this.pkDiagPre = pkDiagPre;
    }

    public String getDescDiagPre(){
        return this.descDiagPre;
    }
    public void setDescDiagPre(String descDiagPre){
        this.descDiagPre = descDiagPre;
    }

    public String getDtOplevel(){
        return this.dtOplevel;
    }
    public void setDtOplevel(String dtOplevel){
        this.dtOplevel = dtOplevel;
    }

    public String getPkOp(){
        return this.pkOp;
    }
    public void setPkOp(String pkOp){
        this.pkOp = pkOp;
    }

    public String getDescOp(){
        return this.descOp;
    }
    public void setDescOp(String descOp){
        this.descOp = descOp;
    }

    public String getDescOpSub(){
        return this.descOpSub;
    }
    public void setDescOpSub(String descOpSub){
        this.descOpSub = descOpSub;
    }

    public String getDtAnae(){
        return this.dtAnae;
    }
    public void setDtAnae(String dtAnae){
        this.dtAnae = dtAnae;
    }

    public String getDtAsepsis(){
        return this.dtAsepsis;
    }
    public void setDtAsepsis(String dtAsepsis){
        this.dtAsepsis = dtAsepsis;
    }

    public String getDtPosi(){
        return this.dtPosi;
    }
    public void setDtPosi(String dtPosi){
        this.dtPosi = dtPosi;
    }

    public String getPkEmpPhyOp(){
        return this.pkEmpPhyOp;
    }
    public void setPkEmpPhyOp(String pkEmpPhyOp){
        this.pkEmpPhyOp = pkEmpPhyOp;
    }

    public String getNameEmpPhyOp(){
        return this.nameEmpPhyOp;
    }
    public void setNameEmpPhyOp(String nameEmpPhyOp){
        this.nameEmpPhyOp = nameEmpPhyOp;
    }

    public String getPkEmpAnae(){
        return this.pkEmpAnae;
    }
    public void setPkEmpAnae(String pkEmpAnae){
        this.pkEmpAnae = pkEmpAnae;
    }

    public String getNameEmpAnae(){
        return this.nameEmpAnae;
    }
    public void setNameEmpAnae(String nameEmpAnae){
        this.nameEmpAnae = nameEmpAnae;
    }

    public String getPkEmpAsis(){
        return this.pkEmpAsis;
    }
    public void setPkEmpAsis(String pkEmpAsis){
        this.pkEmpAsis = pkEmpAsis;
    }

    public String getNameEmpAsis(){
        return this.nameEmpAsis;
    }
    public void setNameEmpAsis(String nameEmpAsis){
        this.nameEmpAsis = nameEmpAsis;
    }

    public String getPkEmpAsis2(){
        return this.pkEmpAsis2;
    }
    public void setPkEmpAsis2(String pkEmpAsis2){
        this.pkEmpAsis2 = pkEmpAsis2;
    }

    public String getNameEmpAsis2(){
        return this.nameEmpAsis2;
    }
    public void setNameEmpAsis2(String nameEmpAsis2){
        this.nameEmpAsis2 = nameEmpAsis2;
    }

    public String getPkEmpAsis3(){
        return this.pkEmpAsis3;
    }
    public void setPkEmpAsis3(String pkEmpAsis3){
        this.pkEmpAsis3 = pkEmpAsis3;
    }

    public String getNameEmpAsis3(){
        return this.nameEmpAsis3;
    }
    public void setNameEmpAsis3(String nameEmpAsis3){
        this.nameEmpAsis3 = nameEmpAsis3;
    }

    public String getPkEmpScrub(){
        return this.pkEmpScrub;
    }
    public void setPkEmpScrub(String pkEmpScrub){
        this.pkEmpScrub = pkEmpScrub;
    }

    public String getNameEmpScrub(){
        return this.nameEmpScrub;
    }
    public void setNameEmpScrub(String nameEmpScrub){
        this.nameEmpScrub = nameEmpScrub;
    }

    public String getPkEmpCircul(){
        return this.pkEmpCircul;
    }
    public void setPkEmpCircul(String pkEmpCircul){
        this.pkEmpCircul = pkEmpCircul;
    }

    public String getNameEmpCricul(){
        return this.nameEmpCricul;
    }
    public void setNameEmpCricul(String nameEmpCricul){
        this.nameEmpCricul = nameEmpCricul;
    }

    public String getDtOpbody(){
        return this.dtOpbody;
    }
    public void setDtOpbody(String dtOpbody){
        this.dtOpbody = dtOpbody;
    }

    public Date getDateApply(){
        return this.dateApply;
    }
    public void setDateApply(Date dateApply){
        this.dateApply = dateApply;
    }

    public Date getDatePlan(){
        return this.datePlan;
    }
    public void setDatePlan(Date datePlan){
        this.datePlan = datePlan;
    }

    public String getPkOpt(){
        return this.pkOpt;
    }
    public void setPkOpt(String pkOpt){
        this.pkOpt = pkOpt;
    }

    public Date getDateConfirm(){
        return this.dateConfirm;
    }
    public void setDateConfirm(Date dateConfirm){
        this.dateConfirm = dateConfirm;
    }

    public Integer getTicketno(){
        return this.ticketno;
    }
    public void setTicketno(Integer ticketno){
        this.ticketno = ticketno;
    }

    public Date getDateBegin(){
        return this.dateBegin;
    }
    public void setDateBegin(Date dateBegin){
        this.dateBegin = dateBegin;
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

    public Date getDateConfirmDept(){
        return this.dateConfirmDept;
    }
    public void setDateConfirmDept(Date dateConfirmDept){
        this.dateConfirmDept = dateConfirmDept;
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
	//----------vo used------------------------------------------------------------------------
	/**
	 * 数据更新状态
	 */
	private String RowStatus;
	/**
	 * 申请单号
	 */
	private String codeApply;
	/**
	 * 就诊id
	 */
	private String pkPv;
	/**
	 * 患者id
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
	 * 手术名称
	 */
	private String opName;
	/**
	 * 术前诊断名称
	 */
	private String diagOpName;
	/**
	 * 开立科室
	 */
	private String pkDept;
	/**
	 * 附加手术
	 */
	private List<CnOpSubjoin> subOpList = new ArrayList<CnOpSubjoin>();
	/**
	 * 附加手术--删除用
	 */
	private List<CnOpSubjoin> subOpListForDel = new ArrayList<CnOpSubjoin>();
	/**
	 * 角色
	 */
	private List<CnOpJoin> cnOpJoin = new ArrayList<CnOpJoin>();
	/**
	 * 术前诊断
	 */
	private List<CnOpDiag> cnOpDiag = new ArrayList<CnOpDiag>();
	
	/**
	 * 临床路径
	 */
	//private List<CpRecExpParam> cpRecExpList = new ArrayList<CpRecExpParam>();
	
	//临床路径变异记录
	private List<CpRecExp> cpRecExpList = new ArrayList<CpRecExp>();
	/**
	 * 临床-CA签名记录
	 */
	private CnSignCa cnSignCa=new CnSignCa();
	/**
	 * 手术申请对应的医嘱
	 */
	private CnOrder cnOrder=new CnOrder();
	
	/**
	 * 手术申请对应的手术安排（已排班）
	 */
	private ExOpSch exOpSch=new ExOpSch();
	
	/**
	 * 手术台楼栋
	 */
	private String namePlace;
	
	/**
	 * 医嘱打印标志
	 */
	private String flagPrintOrd;
	
	
	
	
	
	public List<CpRecExp> getCpRecExpList() {
		return cpRecExpList;
	}
	public void setCpRecExpList(List<CpRecExp> cpRecExpList) {
		this.cpRecExpList = cpRecExpList;
	}
	public String getFlagPrintOrd() {
		return flagPrintOrd;
	}
	public void setFlagPrintOrd(String flagPrintOrd) {
		this.flagPrintOrd = flagPrintOrd;
	}
	public String getNamePlace() {
		return namePlace;
	}
	public void setNamePlace(String namePlace) {
		this.namePlace = namePlace;
	}
	public ExOpSch getExOpSch() {
		return exOpSch;
	}
	public void setExOpSch(ExOpSch exOpSch) {
		this.exOpSch = exOpSch;
	}
	public CnOrder getCnOrder() {
		return cnOrder;
	}
	public void setCnOrder(CnOrder cnOrder) {
		this.cnOrder = cnOrder;
	}
	public CnSignCa getCnSignCa() {
		return cnSignCa;
	}
	public void setCnSignCa(CnSignCa cnSignCa) {
		this.cnSignCa = cnSignCa;
	}
	public List<CnOpSubjoin> getSubOpListForDel() {
		return subOpListForDel;
	}
	public void setSubOpListForDel(List<CnOpSubjoin> subOpListForDel) {
		this.subOpListForDel = subOpListForDel;
	}
	public String getRowStatus() {
		return RowStatus;
	}
	public void setRowStatus(String rowStatus) {
		RowStatus = rowStatus;
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
	public String getOpName() {
		return opName;
	}
	public void setOpName(String opName) {
		this.opName = opName;
	}
	public List<CnOpSubjoin> getSubOpList() {
		return subOpList;
	}
	public void setSubOpList(List<CnOpSubjoin> subOpList) {
		this.subOpList = subOpList;
	}
	public List<CnOpJoin> getCnOpJoin() {
		return cnOpJoin;
	}
	public void setCnOpJoin(List<CnOpJoin> cnOpJoin) {
		this.cnOpJoin = cnOpJoin;
	}
	public String getDiagOpName() {
		return diagOpName;
	}
	public void setDiagOpName(String diagOpName) {
		this.diagOpName = diagOpName;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
    private String flagCp;

	public String getFlagCp() {
		return flagCp;
	}
	public void setFlagCp(String flagCp) {
		this.flagCp = flagCp;
	}
	public String getPkDept() {
		return pkDept;
	}
	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}
    
	private String pkDeptNs;

	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	public String getFlagAgain() {
		return flagAgain;
	}
	public void setFlagAgain(String flagAgain) {
		this.flagAgain = flagAgain;
	}
	public String getDescIcjd() {
		return descIcjd;
	}
	public void setDescIcjd(String descIcjd) {
		this.descIcjd = descIcjd;
	}
	public String getDescUnicjd() {
		return descUnicjd;
	}
	public void setDescUnicjd(String descUnicjd) {
		this.descUnicjd = descUnicjd;
	}
	public String getPurpAgain() {
		return purpAgain;
	}
	public void setPurpAgain(String purpAgain) {
		this.purpAgain = purpAgain;
	}
	public Date getDateHead() {
		return dateHead;
	}
	public void setDateHead(Date dateHead) {
		this.dateHead = dateHead;
	}
	public String getFlagHead() {
		return flagHead;
	}
	public void setFlagHead(String flagHead) {
		this.flagHead = flagHead;
	}
	public String getPkEmpHead() {
		return pkEmpHead;
	}
	public void setPkEmpHead(String pkEmpHead) {
		this.pkEmpHead = pkEmpHead;
	}
	public String getNameEmpHead() {
		return nameEmpHead;
	}
	public void setNameEmpHead(String nameEmpHead) {
		this.nameEmpHead = nameEmpHead;
	}
	private String euIntern;

	public String getEuIntern() {
		return euIntern;
	}
	public void setEuIntern(String euIntern) {
		this.euIntern = euIntern;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	public String getDtBloodAbo() {
		return dtBloodAbo;
	}
	public void setDtBloodAbo(String dtBloodAbo) {
		this.dtBloodAbo = dtBloodAbo;
	}
	public String getDtBloodRh() {
		return dtBloodRh;
	}
	public void setDtBloodRh(String dtBloodRh) {
		this.dtBloodRh = dtBloodRh;
	}
	public String getDescInfec() {
		return descInfec;
	}
	public void setDescInfec(String descInfec) {
		this.descInfec = descInfec;
	}
	public String getPkDeptAnae() {
		return pkDeptAnae;
	}
	public void setPkDeptAnae(String pkDeptAnae) {
		this.pkDeptAnae = pkDeptAnae;
	}
	public String getDescCpb() {
		return descCpb;
	}
	public void setDescCpb(String descCpb) {
		this.descCpb = descCpb;
	}
	public String getFlagEd() {
		return flagEd;
	}
	public void setFlagEd(String flagEd) {
		this.flagEd = flagEd;
	}
	public String getSpecEquipment() {
		return specEquipment;
	}
	public void setSpecEquipment(String specEquipment) {
		this.specEquipment = specEquipment;
	}
	public List<CnOpDiag> getCnOpDiag() {
		return cnOpDiag;
	}
	public void setCnOpDiag(List<CnOpDiag> cnOpDiag) {
		this.cnOpDiag = cnOpDiag;
	}
	public String getDtAsalevel() {
		return dtAsalevel;
	}
	public void setDtAsalevel(String dtAsalevel) {
		this.dtAsalevel = dtAsalevel;
	}
	public BigDecimal getDuration() {
		return duration;
	}
	public void setDuration(BigDecimal duration) {
		this.duration = duration;
	}

    public String getNameOp() {
        return nameOp;
    }

    public void setNameOp(String nameOp) {
        this.nameOp = nameOp;
    }

    public String getFlagFrozen() {
        return flagFrozen;
    }

    public void setFlagFrozen(String flagFrozen) {
        this.flagFrozen = flagFrozen;
    }

    public String getFlagCarm() {
        return flagCarm;
    }

    public void setFlagCarm(String flagCarm) {
        this.flagCarm = flagCarm;
    }

    public String getFlagFinishAnae() {
        return flagFinishAnae;
    }

    public void setFlagFinishAnae(String flagFinishAnae) {
        this.flagFinishAnae = flagFinishAnae;
    }

    public String getEuErlevel() {
        return euErlevel;
    }

    public void setEuErlevel(String euErlevel) {
        this.euErlevel = euErlevel;
    }
}