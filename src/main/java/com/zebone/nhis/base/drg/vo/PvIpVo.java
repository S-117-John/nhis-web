package com.zebone.nhis.base.drg.vo;

import java.util.Date;

import com.zebone.nhis.common.module.pv.PvIp;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

@Table(value="PV_IP")
public class PvIpVo extends PvIp{
	
	private static final long serialVersionUID = 1L;

	/** PK_PVIP - 就诊主键 */
	@PK
	@Field(value="PK_PVIP",id=KeyId.UUID)
    private String pkPvip;

    /** PK_IP_NOTICE - 入院通知单主键 */
	@Field(value="PK_IP_NOTICE")
    private String pkIpNotice;

    /** PK_PV - 就诊 */
	@Field(value="PK_PV")
    private String pkPv;

    /** IP_TIMES - 住院次数 */
	@Field(value="IP_TIMES")
    private Integer ipTimes;

    /** DATE_NOTICE - 通知出院日期 */
	@Field(value="DATE_NOTICE")
    private Date dateNotice;

    /** FLAG_OPT - 是否手术标志 */
	@Field(value="FLAG_OPT")
    private String flagOpt;

    /** DT_LEVEL_NS - 护理级别编码 */
	@Field(value="DT_LEVEL_NS")
    private String dtLevelNs;

    /** DT_LEVEL_DISE - 病情等级 */
	@Field(value="DT_LEVEL_DISE")
    private String dtLevelDise;

    /** DT_LEVEL_NUTR - 营养等级 */
	@Field(value="DT_LEVEL_NUTR")
    private String dtLevelNutr;

    /** DT_OUTCOMES - 病情转归 */
	@Field(value="DT_OUTCOMES")
    private String dtOutcomes;

    /** DT_STTYPE_INS - 结算类型 */
	@Field(value="DT_STTYPE_INS")
    private String dtSttypeIns;
	
    /** FLAG_INFANT - 新生儿标志 */
	@Field(value="FLAG_INFANT")
    private String flagInfant;

    /** QUAN_INFANT - 新生儿数量 */
	@Field(value="QUAN_INFANT")
    private Long quanInfant;

    /** EU_STATUS_DOC - 病案转归状态:0 病案提交 1 病历签收   2 病案编码 */
	@Field(value="EU_STATUS_DOC")
    private String euStatusDoc;

    /** DATE_COMMIT_DOC - 病案转归日期 */
	@Field(value="DATE_COMMIT_DOC")
    private Date dateCommitDoc;

    /** FLAG_GA - 终末质控标志 */
	@Field(value="FLAG_GA")
    private String flagGa;

    /** FLAG_GA_NS - 护理终末质控标志 */
	@Field(value="FLAG_GA_NS")
    private String flagGaNs;

    /** DT_INTYPE - 来院方式:1.急诊  2.门诊  3.其他医疗机构转入  9.其他 */
	@Field(value="DT_INTYPE")
    private String dtIntype;

    /** DT_OUTTYPE - 出院方式:1 医嘱离院，2 医嘱转医院，3 医嘱转基层，4 非医嘱离院，5 死亡，9其他 */
	@Field(value="DT_OUTTYPE")
    private String dtOuttype;

    /** PK_DEPT_ADMIT - 办理入院时的科室 */
	@Field(value="PK_DEPT_ADMIT")
    private String pkDeptAdmit;

    /** PK_DEPT_NS_ADMIT - 办理入院时的病区 */
	@Field(value="PK_DEPT_NS_ADMIT")
    private String pkDeptNsAdmit;

    /** PK_DEPT_DIS - 办理出院时的科室 */
	@Field(value="PK_DEPT_DIS")
    private String pkDeptDis;

    /** PK_DEPT_NS_DIS - 办理出院时的病区 */
	@Field(value="PK_DEPT_NS_DIS")
    private String pkDeptNsDis;
	
	/** FLAG_PREST - 预结算标志 */
	@Field(value="FLAG_PREST")
    private String flagPrest;

    /** DATE_PREST - 预结算日期 */
	@Field(value="DATE_PREST")
    private Date datePrest;

    /** PK_EMP_PREST - 预结算操作员 */
	@Field(value="PK_EMP_PREST")
    private String pkEmpPrest;

    /** NAME_EMP_PREST - 操作员姓名 */
	@Field(value="NAME_EMP_PREST")
    private String nameEmpPrest;
	
	/** CODE_DIAG - 诊断编码 */
	@Field(value="CODE_DIAG")
	private String codeDiag;
	
	/** NAME_DIAG - 诊断名称 */
	@Field(value="NAME_DIAG")
	private String nameDiag;
	
	
	/**感染标志*/
	@Field(value="FLAG_INFECTED")
	private String flagInfected;
	
    public String getPkPvip(){
        return this.pkPvip;
    }
    public void setPkPvip(String pkPvip){
        this.pkPvip = pkPvip;
    }

    public String getPkIpNotice(){
        return this.pkIpNotice;
    }
    public void setPkIpNotice(String pkIpNotice){
        this.pkIpNotice = pkIpNotice;
    }

    public String getPkPv(){
        return this.pkPv;
    }
    public void setPkPv(String pkPv){
        this.pkPv = pkPv;
    }

    public Integer getIpTimes(){
        return this.ipTimes;
    }
    public void setIpTimes(Integer ipTimes){
        this.ipTimes = ipTimes;
    }

    public Date getDateNotice(){
        return this.dateNotice;
    }
    public void setDateNotice(Date dateNotice){
        this.dateNotice = dateNotice;
    }

    public String getFlagOpt(){
        return this.flagOpt;
    }
    public void setFlagOpt(String flagOpt){
        this.flagOpt = flagOpt;
    }

    public String getDtLevelNs(){
        return this.dtLevelNs;
    }
    public void setDtLevelNs(String dtLevelNs){
        this.dtLevelNs = dtLevelNs;
    }

    public String getDtLevelDise(){
        return this.dtLevelDise;
    }
    public void setDtLevelDise(String dtLevelDise){
        this.dtLevelDise = dtLevelDise;
    }

    public String getDtLevelNutr(){
        return this.dtLevelNutr;
    }
    public void setDtLevelNutr(String dtLevelNutr){
        this.dtLevelNutr = dtLevelNutr;
    }

    public String getDtOutcomes(){
        return this.dtOutcomes;
    }
    public void setDtOutcomes(String dtOutcomes){
        this.dtOutcomes = dtOutcomes;
    }

    public String getFlagInfant(){
        return this.flagInfant;
    }
    public void setFlagInfant(String flagInfant){
        this.flagInfant = flagInfant;
    }

    public Long getQuanInfant(){
        return this.quanInfant;
    }
    public void setQuanInfant(Long quanInfant){
        this.quanInfant = quanInfant;
    }

    public String getEuStatusDoc(){
        return this.euStatusDoc;
    }
    public void setEuStatusDoc(String euStatusDoc){
        this.euStatusDoc = euStatusDoc;
    }

    public Date getDateCommitDoc(){
        return this.dateCommitDoc;
    }
    public void setDateCommitDoc(Date dateCommitDoc){
        this.dateCommitDoc = dateCommitDoc;
    }

    public String getFlagGa(){
        return this.flagGa;
    }
    public void setFlagGa(String flagGa){
        this.flagGa = flagGa;
    }

    public String getFlagGaNs(){
        return this.flagGaNs;
    }
    public void setFlagGaNs(String flagGaNs){
        this.flagGaNs = flagGaNs;
    }

    public String getDtIntype(){
        return this.dtIntype;
    }
    public void setDtIntype(String dtIntype){
        this.dtIntype = dtIntype;
    }

    public String getDtOuttype(){
        return this.dtOuttype;
    }
    public void setDtOuttype(String dtOuttype){
        this.dtOuttype = dtOuttype;
    }

    public String getPkDeptAdmit(){
        return this.pkDeptAdmit;
    }
    public void setPkDeptAdmit(String pkDeptAdmit){
        this.pkDeptAdmit = pkDeptAdmit;
    }

    public String getPkDeptNsAdmit(){
        return this.pkDeptNsAdmit;
    }
    public void setPkDeptNsAdmit(String pkDeptNsAdmit){
        this.pkDeptNsAdmit = pkDeptNsAdmit;
    }

    public String getPkDeptDis(){
        return this.pkDeptDis;
    }
    public void setPkDeptDis(String pkDeptDis){
        this.pkDeptDis = pkDeptDis;
    }

    public String getPkDeptNsDis(){
        return this.pkDeptNsDis;
    }
    public void setPkDeptNsDis(String pkDeptNsDis){
        this.pkDeptNsDis = pkDeptNsDis;
    }
	public String getFlagPrest() {
		return flagPrest;
	}
	public void setFlagPrest(String flagPrest) {
		this.flagPrest = flagPrest;
	}
	public Date getDatePrest() {
		return datePrest;
	}
	public void setDatePrest(Date datePrest) {
		this.datePrest = datePrest;
	}
	public String getPkEmpPrest() {
		return pkEmpPrest;
	}
	public void setPkEmpPrest(String pkEmpPrest) {
		this.pkEmpPrest = pkEmpPrest;
	}
	public String getNameEmpPrest() {
		return nameEmpPrest;
	}
	public void setNameEmpPrest(String nameEmpPrest) {
		this.nameEmpPrest = nameEmpPrest;
	}
	public String getDtSttypeIns() {
		return dtSttypeIns;
	}
	public void setDtSttypeIns(String dtSttypeIns) {
		this.dtSttypeIns = dtSttypeIns;
	}
	public String getCodeDiag() {
		return codeDiag;
	}
	public void setCodeDiag(String codeDiag) {
		this.codeDiag = codeDiag;
	}
	public String getNameDiag() {
		return nameDiag;
	}
	public void setNameDiag(String nameDiag) {
		this.nameDiag = nameDiag;
	}
	public String getFlagInfected() {
		return flagInfected;
	}
	public void setFlagInfected(String flagInfected) {
		this.flagInfected = flagInfected;
	}

	@Field(value="eu_status_drg")
	public String euStatusDrg;
	@Field(value="date_drg")
	public Date dateDrg;
	public String getEuStatusDrg() {
		return euStatusDrg;
	}
	public void setEuStatusDrg(String euStatusDrg) {
		this.euStatusDrg = euStatusDrg;
	}
	public Date getDateDrg() {
		return dateDrg;
	}
	public void setDateDrg(Date dateDrg) {
		this.dateDrg = dateDrg;
	}
	
	
}
