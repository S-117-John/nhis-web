package com.zebone.nhis.common.module.pv;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;


/**
 * Table: PV_IP_NOTICE  - 患者就诊-入院通知单 
 *
 * @since 2016-09-21 03:56:34
 */
@Table(value="PV_IP_NOTICE")
public class PvIpNotice extends BaseModule  {
	
	private static final long serialVersionUID = 1L;

	/** PK_IN_NOTICE - 入院通知单主键 */
	@PK
	@Field(value="PK_IN_NOTICE",id=KeyId.UUID)
    private String pkInNotice;

    /** PK_PI - 患者主键 */
	@Field(value="PK_PI")
    private String pkPi;

    /** PK_HP - 医保计划 */
	@Field(value="PK_HP")
    private String pkHp;

    /** PK_PV_OP - 就诊编码_门诊:对应开立此入院通知单的门诊就诊主键 */
	@Field(value="PK_PV_OP")
    private String pkPvOp;

    /** PK_PV_IP - 就诊编码_住院:入院后对应的入院就诊主键 */
	@Field(value="PK_PV_IP")
    private String pkPvIp;

    /** PK_DEPT_OP - 门诊就诊科室 */
	@Field(value="PK_DEPT_OP")
    private String pkDeptOp;

    /** PK_EMP_OP - 门诊医生 */
	@Field(value="PK_EMP_OP")
    private String pkEmpOp;

    /** NAME_EMP_OP - 门诊医生名称 */
	@Field(value="NAME_EMP_OP")
    private String nameEmpOp;

    /** PK_DIAG_MAJ - 门诊主要诊断_西医 */
	@Field(value="PK_DIAG_MAJ")
    private String pkDiagMaj;

    /** DESC_DIAG_MAJ - 门诊主要诊断描述_西医 */
	@Field(value="DESC_DIAG_MAJ")
    private String descDiagMaj;

    /** DESC_DIAG_ELS - 门诊其它诊断描述_西医 */
	@Field(value="DESC_DIAG_ELS")
    private String descDiagEls;

    /** PK_DIAG_TCM - 门诊主要诊断_中医 */
	@Field(value="PK_DIAG_TCM")
    private String pkDiagTcm;

    /** DESC_DIAG_MAJ_TCM - 门诊主要诊断描述_中医 */
	@Field(value="DESC_DIAG_MAJ_TCM")
    private String descDiagMajTcm;

    /** DESC_DIAG_ELS_TCM - 门诊其它诊断描述_中医 */
	@Field(value="DESC_DIAG_ELS_TCM")
    private String descDiagElsTcm;

    /** DT_LEVEL_DISE - 病情状况 */
	@Field(value="DT_LEVEL_DISE")
    private String dtLevelDise;

    /** PK_DEPT_IP - 入院科室 */
	@Field(value="PK_DEPT_IP")
    private String pkDeptIp;

    /** PK_DEPT_NS_IP - 入院病区 */
	@Field(value="PK_DEPT_NS_IP")
    private String pkDeptNsIp;

    /** DT_BEDTYPE - 床位类型 */
	@Field(value="DT_BEDTYPE")
    private String dtBedtype;

    /** DATE_ADMIT - 计划入院日期 */
	@Field(value="DATE_ADMIT")
    private Date dateAdmit;

    /** AMT_DEPO - 预计预交金额度 */
	@Field(value="AMT_DEPO")
    private Double amtDepo;

    /** EU_STATUS - 通知单状态:0 通知单开立; 1 已通知患者; 2 患者入院 */
	@Field(value="EU_STATUS")
    private String euStatus;

    /** DATE_VALID - 有效期 */
	@Field(value="DATE_VALID")
    private Date dateValid;

    /** NOTE - 备注 */
	@Field(value="NOTE")
    private String note;
    /** DT_WAY_IP - 入院方式 */
    @Field(value="DT_WAY_IP")
	private String dtWayIp;
    /** FLAG_READM - 十日内再入院 */
    @Field(value="FLAG_READM")
	private String flagReadm;
    
    /** FLAG_SYGERY - 日间手术 */
    @Field(value="FLAG_SYGERY")
	private String flagSygery;

	private String  pkDiagIp;
	
	private String pkDiagOp;
	
	private String descDiagIp;
	
    private String descDiagOp;
	@Field(value="BED_NO")
    private String bedNo;
	@Field(value="CONTACT_DEPT")
    private String contactDept;
    @Field(value="FLAG_CHG_DEPT")
    private String flagChgDept;// 允许跨科收治'
    @Field(value="FLAG_ICU")
    private String flagIcu;// 收治ICU
    @Field(value="FLAG_SPEC")
    private String flagSpec;// 收治特诊
    @Field(value="FLAG_ISOLATE")
    private String flagIsolate;// 是否隔离
    @Field(value="NAME_REL")
    private String nameRel;// 联系人
    @Field(value="TEL_REL")
    private String telRel;// 联系电话
    @Field(value="DT_PAT_CLS")
    private String dtPatCls;// 患者分类

    @Field(value="DATE_BEGIN")
    private Date dateBegin; //入院日期
    @Field(value="DATE_END")
    private Date dateEnd; //出院日期

    /***
     * 是否新冠病毒检测标志
     */
    @Field(value="FLAG_COVID_CHECK")
    private String flagCovidCheck;
    /***
     * 新冠病毒检测日期
     */
    @Field(value="DATE_COVID")
    private Date dateCovid;
    /***
     * 新冠病毒检测结果
     */
    @Field(value = "EU_RESULT_COVID")
    private String euResultCovid;

    
    public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getContactDept() {
		return contactDept;
	}
	public void setContactDept(String contactDept) {
		this.contactDept = contactDept;
	}
	public String getPkDiagIp() {
		return pkDiagIp;
	}
	public void setPkDiagIp(String pkDiagIp) {
		this.pkDiagIp = pkDiagIp;
	}
	public String getPkDiagOp() {
		return pkDiagOp;
	}
	public void setPkDiagOp(String pkDiagOp) {
		this.pkDiagOp = pkDiagOp;
	}
	public String getDescDiagIp() {
		return descDiagIp;
	}
	public void setDescDiagIp(String descDiagIp) {
		this.descDiagIp = descDiagIp;
	}
	public String getDescDiagOp() {
		return descDiagOp;
	}
	public void setDescDiagOp(String descDiagOp) {
		this.descDiagOp = descDiagOp;
	}
	public String getPkInNotice(){
        return this.pkInNotice;
    }
    public void setPkInNotice(String pkInNotice){
        this.pkInNotice = pkInNotice;
    }

    public String getPkPi(){
        return this.pkPi;
    }
    public void setPkPi(String pkPi){
        this.pkPi = pkPi;
    }

    public String getPkHp(){
        return this.pkHp;
    }
    public void setPkHp(String pkHp){
        this.pkHp = pkHp;
    }

    public String getPkPvOp(){
        return this.pkPvOp;
    }
    public void setPkPvOp(String pkPvOp){
        this.pkPvOp = pkPvOp;
    }

    public String getPkPvIp(){
        return this.pkPvIp;
    }
    public void setPkPvIp(String pkPvIp){
        this.pkPvIp = pkPvIp;
    }

    public String getPkDeptOp(){
        return this.pkDeptOp;
    }
    public void setPkDeptOp(String pkDeptOp){
        this.pkDeptOp = pkDeptOp;
    }

    public String getPkEmpOp(){
        return this.pkEmpOp;
    }
    public void setPkEmpOp(String pkEmpOp){
        this.pkEmpOp = pkEmpOp;
    }

    public String getNameEmpOp(){
        return this.nameEmpOp;
    }
    public void setNameEmpOp(String nameEmpOp){
        this.nameEmpOp = nameEmpOp;
    }

    public String getPkDiagMaj(){
        return this.pkDiagMaj;
    }
    public void setPkDiagMaj(String pkDiagMaj){
        this.pkDiagMaj = pkDiagMaj;
    }

    public String getDescDiagMaj(){
        return this.descDiagMaj;
    }
    public void setDescDiagMaj(String descDiagMaj){
        this.descDiagMaj = descDiagMaj;
    }

    public String getDescDiagEls(){
        return this.descDiagEls;
    }
    public void setDescDiagEls(String descDiagEls){
        this.descDiagEls = descDiagEls;
    }

    public String getPkDiagTcm(){
        return this.pkDiagTcm;
    }
    public void setPkDiagTcm(String pkDiagTcm){
        this.pkDiagTcm = pkDiagTcm;
    }

    public String getDescDiagMajTcm(){
        return this.descDiagMajTcm;
    }
    public void setDescDiagMajTcm(String descDiagMajTcm){
        this.descDiagMajTcm = descDiagMajTcm;
    }

    public String getDescDiagElsTcm(){
        return this.descDiagElsTcm;
    }
    public void setDescDiagElsTcm(String descDiagElsTcm){
        this.descDiagElsTcm = descDiagElsTcm;
    }

    public String getDtLevelDise(){
        return this.dtLevelDise;
    }
    public void setDtLevelDise(String dtLevelDise){
        this.dtLevelDise = dtLevelDise;
    }

    public String getPkDeptIp(){
        return this.pkDeptIp;
    }
    public void setPkDeptIp(String pkDeptIp){
        this.pkDeptIp = pkDeptIp;
    }

    public String getPkDeptNsIp(){
        return this.pkDeptNsIp;
    }
    public void setPkDeptNsIp(String pkDeptNsIp){
        this.pkDeptNsIp = pkDeptNsIp;
    }

    public String getDtBedtype(){
        return this.dtBedtype;
    }
    public void setDtBedtype(String dtBedtype){
        this.dtBedtype = dtBedtype;
    }

    public Date getDateAdmit(){
        return this.dateAdmit;
    }
    public void setDateAdmit(Date dateAdmit){
        this.dateAdmit = dateAdmit;
    }

    public Double getAmtDepo(){
        return this.amtDepo;
    }
    public void setAmtDepo(Double amtDepo){
        this.amtDepo = amtDepo;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public Date getDateValid(){
        return this.dateValid;
    }
    public void setDateValid(Date dateValid){
        this.dateValid = dateValid;
    }

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
    }

    public String getDtWayIp() {
        return dtWayIp;
    }

    public void setDtWayIp(String dtWayIp) {
        this.dtWayIp = dtWayIp;
    }

    public String getFlagReadm() {
        return flagReadm;
    }

    public void setFlagReadm(String flagReadm) {
        this.flagReadm = flagReadm;
    }

    public String getFlagCovidCheck() {
        return flagCovidCheck;
    }

    public void setFlagCovidCheck(String flagCovidCheck) {
        this.flagCovidCheck = flagCovidCheck;
    }

    public Date getDateCovid() {
        return dateCovid;
    }

    public void setDateCovid(Date dateCovid) {
        this.dateCovid = dateCovid;
    }

    public String getEuResultCovid() {
        return euResultCovid;
    }

    public void setEuResultCovid(String euResultCovid) {
        this.euResultCovid = euResultCovid;
    }

    public String getFlagChgDept() {
        return flagChgDept;
    }

    public void setFlagChgDept(String flagChgDept) {
        this.flagChgDept = flagChgDept;
    }

    public String getFlagIcu() {
        return flagIcu;
    }

    public void setFlagIcu(String flagIcu) {
        this.flagIcu = flagIcu;
    }

    public String getFlagSpec() {
        return flagSpec;
    }

    public void setFlagSpec(String flagSpec) {
        this.flagSpec = flagSpec;
    }

    public String getFlagIsolate() {
        return flagIsolate;
    }

    public void setFlagIsolate(String flagIsolate) {
        this.flagIsolate = flagIsolate;
    }

    public String getNameRel() {
        return nameRel;
    }

    public void setNameRel(String nameRel) {
        this.nameRel = nameRel;
    }

    public String getTelRel() {
        return telRel;
    }

    public void setTelRel(String telRel) {
        this.telRel = telRel;
    }

    public String getDtPatCls() {
        return dtPatCls;
    }

    public void setDtPatCls(String dtPatCls) {
        this.dtPatCls = dtPatCls;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }    

	public String getFlagSygery() {
		return flagSygery;
	}
	public void setFlagSygery(String flagSygery) {
		this.flagSygery = flagSygery;
	}
}