package com.zebone.nhis.pro.zsba.mz.pub.vo;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: EX_PRES_OCC  - ex_pres_occ 
 *
 * @since 2016-11-11 02:53:18
 */
@Table(value="EX_PRES_OCC")
public class ZsbaExPresOcc extends BaseModule  {

	@PK
	@Field(value="PK_PRESOCC",id=KeyId.UUID)
    private String pkPresocc;

	@Field(value="PK_PV")
    private String pkPv;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="DT_PRESTYPE")
    private String dtPrestype;

    /** DATE_REG - 用于确定排列顺序 */
	@Field(value="DATE_REG")
    private Date dateReg;

    /** EU_STATUS - 1打印，2配药，3发药，4代煎登记，5代煎发放，8挂起，9取消 */
	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_PRES")
    private String pkPres;

	@Field(value="PRES_NO")
    private String presNo;

	@Field(value="PK_DIAG")
    private String pkDiag;

	@Field(value="PK_ORG_PRES")
    private String pkOrgPres;

	@Field(value="NAME_EMP_PRES")
    private String nameEmpPres;

	@Field(value="DATE_PRES")
    private Date datePres;

	@Field(value="PK_DEPT_PRES")
    private String pkDeptPres;

	@Field(value="PK_EMP_PRES")
    private String pkEmpPres;

	@Field(value="PK_ORG_EX")
    private String pkOrgEx;

	@Field(value="PK_DEPT_EX")
    private String pkDeptEx;

	@Field(value="FLAG_SUSP")
    private String flagSusp;

	@Field(value="FLAG_PREP")
    private String flagPrep;

	@Field(value="DATE_PREP")
    private Date datePrep;

	@Field(value="PK_EMP_PREP")
    private String pkEmpPrep;

	@Field(value="NAME_EMP_PREP")
    private String nameEmpPrep;

	@Field(value="WINNO_PREP")
    private String winnoPrep;

	@Field(value="FLAG_CONF")
    private String flagConf;

	@Field(value="DATE_CONF")
    private Date dateConf;

	@Field(value="PK_EMP_CONF")
    private String pkEmpConf;

	@Field(value="NAME_EMP_CONF")
    private String nameEmpConf;

	@Field(value="WINNO_CONF")
    private String winnoConf;

	@Field(value="FLAG_CANC")
    private String flagCanc;

	@Field(value="DATE_CANC")
    private Date dateCanc;

	@Field(value="PK_EMP_CANC")
    private String pkEmpCanc;

	@Field(value="NAME_EMP_CANC")
    private String nameEmpCanc;

	@Field(value="FLAG_BO")
    private String flagBo;

	@Field(value="DATE_BO")
    private Date dateBo;

	@Field(value="PK_EMP_BO")
    private String pkEmpBo;

	@Field(value="NAME_EMP_BO")
    private String nameEmpBo;

	@Field(value="FLAG_BODE")
    private String flagBode;

	@Field(value="DATE_BODE")
    private Date dateBode;

	@Field(value="PK_EMP_BODE")
    private String pkEmpBode;

	@Field(value="NAME_EMP_BODE")
    private String nameEmpBode;

	@Field(value="FLAG_CG")
    private String flagCg;

	@Field(value="FLAG_CHK")
    private String flagChk;

	@Field(value="DATE_CHK")
    private Date dateChk;

	@Field(value="PK_DEPT_CHK")
    private String pkDeptChk;

	@Field(value="PK_EMP_CHK")
    private String pkEmpChk;

	@Field(value="NAME_EMP_CHK")
    private String nameEmpChk;

	@Field(value="NOTE")
    private String note;

	@Field(value="MODIFIER")
    private String modifier;

	@Field(value="MODITY_TIME")
    private Date modityTime;

	@Field(value="PK_SETTLE")
	private String pkSettle;
	
	@Field(value="FLAG_REG")
	private String flagReg;
	
	@Field(value="EU_DETYPE")
	private String euDetype;

	@Field(value = "FLAG_PREPRINT")
	private String flagPreprint;
	
	@Field(value = "SORT_NO")
	private Integer sortNo;
	
    public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getFlagReg() {
		return flagReg;
	}
	public void setFlagReg(String flagReg) {
		this.flagReg = flagReg;
	}
	public String getPkPresocc(){
        return this.pkPresocc;
    }
    public void setPkPresocc(String pkPresocc){
        this.pkPresocc = pkPresocc;
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

    public String getDtPrestype(){
        return this.dtPrestype;
    }
    public void setDtPrestype(String dtPrestype){
        this.dtPrestype = dtPrestype;
    }

    public Date getDateReg(){
        return this.dateReg;
    }
    public void setDateReg(Date dateReg){
        this.dateReg = dateReg;
    }

    public String getEuStatus(){
        return this.euStatus;
    }
    public void setEuStatus(String euStatus){
        this.euStatus = euStatus;
    }

    public String getPkPres(){
        return this.pkPres;
    }
    public void setPkPres(String pkPres){
        this.pkPres = pkPres;
    }

    public String getPresNo(){
        return this.presNo;
    }
    public void setPresNo(String presNo){
        this.presNo = presNo;
    }

    public String getPkDiag(){
        return this.pkDiag;
    }
    public void setPkDiag(String pkDiag){
        this.pkDiag = pkDiag;
    }

    public String getPkOrgPres(){
        return this.pkOrgPres;
    }
    public void setPkOrgPres(String pkOrgPres){
        this.pkOrgPres = pkOrgPres;
    }

    public String getNameEmpPres(){
        return this.nameEmpPres;
    }
    public void setNameEmpPres(String nameEmpPres){
        this.nameEmpPres = nameEmpPres;
    }

    public Date getDatePres(){
        return this.datePres;
    }
    public void setDatePres(Date datePres){
        this.datePres = datePres;
    }

    public String getPkDeptPres(){
        return this.pkDeptPres;
    }
    public void setPkDeptPres(String pkDeptPres){
        this.pkDeptPres = pkDeptPres;
    }

    public String getPkEmpPres(){
        return this.pkEmpPres;
    }
    public void setPkEmpPres(String pkEmpPres){
        this.pkEmpPres = pkEmpPres;
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

    public String getFlagSusp(){
        return this.flagSusp;
    }
    public void setFlagSusp(String flagSusp){
        this.flagSusp = flagSusp;
    }

    public String getFlagPrep(){
        return this.flagPrep;
    }
    public void setFlagPrep(String flagPrep){
        this.flagPrep = flagPrep;
    }

    public Date getDatePrep(){
        return this.datePrep;
    }
    public void setDatePrep(Date datePrep){
        this.datePrep = datePrep;
    }

    public String getPkEmpPrep(){
        return this.pkEmpPrep;
    }
    public void setPkEmpPrep(String pkEmpPrep){
        this.pkEmpPrep = pkEmpPrep;
    }

    public String getNameEmpPrep(){
        return this.nameEmpPrep;
    }
    public void setNameEmpPrep(String nameEmpPrep){
        this.nameEmpPrep = nameEmpPrep;
    }

    public String getWinnoPrep(){
        return this.winnoPrep;
    }
    public void setWinnoPrep(String winnoPrep){
        this.winnoPrep = winnoPrep;
    }

    public String getFlagConf(){
        return this.flagConf;
    }
    public void setFlagConf(String flagConf){
        this.flagConf = flagConf;
    }

    public Date getDateConf(){
        return this.dateConf;
    }
    public void setDateConf(Date dateConf){
        this.dateConf = dateConf;
    }

    public String getPkEmpConf(){
        return this.pkEmpConf;
    }
    public void setPkEmpConf(String pkEmpConf){
        this.pkEmpConf = pkEmpConf;
    }

    public String getNameEmpConf(){
        return this.nameEmpConf;
    }
    public void setNameEmpConf(String nameEmpConf){
        this.nameEmpConf = nameEmpConf;
    }

    public String getWinnoConf(){
        return this.winnoConf;
    }
    public void setWinnoConf(String winnoConf){
        this.winnoConf = winnoConf;
    }

    public String getFlagCanc(){
        return this.flagCanc;
    }
    public void setFlagCanc(String flagCanc){
        this.flagCanc = flagCanc;
    }

    public Date getDateCanc(){
        return this.dateCanc;
    }
    public void setDateCanc(Date dateCanc){
        this.dateCanc = dateCanc;
    }

    public String getPkEmpCanc(){
        return this.pkEmpCanc;
    }
    public void setPkEmpCanc(String pkEmpCanc){
        this.pkEmpCanc = pkEmpCanc;
    }

    public String getNameEmpCanc(){
        return this.nameEmpCanc;
    }
    public void setNameEmpCanc(String nameEmpCanc){
        this.nameEmpCanc = nameEmpCanc;
    }

    public String getFlagBo(){
        return this.flagBo;
    }
    public void setFlagBo(String flagBo){
        this.flagBo = flagBo;
    }

    public Date getDateBo(){
        return this.dateBo;
    }
    public void setDateBo(Date dateBo){
        this.dateBo = dateBo;
    }

    public String getPkEmpBo(){
        return this.pkEmpBo;
    }
    public void setPkEmpBo(String pkEmpBo){
        this.pkEmpBo = pkEmpBo;
    }

    public String getNameEmpBo(){
        return this.nameEmpBo;
    }
    public void setNameEmpBo(String nameEmpBo){
        this.nameEmpBo = nameEmpBo;
    }

    public String getFlagBode(){
        return this.flagBode;
    }
    public void setFlagBode(String flagBode){
        this.flagBode = flagBode;
    }

    public Date getDateBode(){
        return this.dateBode;
    }
    public void setDateBode(Date dateBode){
        this.dateBode = dateBode;
    }

    public String getPkEmpBode(){
        return this.pkEmpBode;
    }
    public void setPkEmpBode(String pkEmpBode){
        this.pkEmpBode = pkEmpBode;
    }

    public String getNameEmpBode(){
        return this.nameEmpBode;
    }
    public void setNameEmpBode(String nameEmpBode){
        this.nameEmpBode = nameEmpBode;
    }

    public String getFlagCg(){
        return this.flagCg;
    }
    public void setFlagCg(String flagCg){
        this.flagCg = flagCg;
    }

    public String getFlagChk(){
        return this.flagChk;
    }
    public void setFlagChk(String flagChk){
        this.flagChk = flagChk;
    }

    public Date getDateChk(){
        return this.dateChk;
    }
    public void setDateChk(Date dateChk){
        this.dateChk = dateChk;
    }

    public String getPkDeptChk(){
        return this.pkDeptChk;
    }
    public void setPkDeptChk(String pkDeptChk){
        this.pkDeptChk = pkDeptChk;
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

    public String getNote(){
        return this.note;
    }
    public void setNote(String note){
        this.note = note;
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
	public String getEuDetype() {
		return euDetype;
	}
	public void setEuDetype(String euDetype) {
		this.euDetype = euDetype;
	}

    public String getFlagPreprint() {
        return flagPreprint;
    }

    public void setFlagPreprint(String flagPreprint) {
        this.flagPreprint = flagPreprint;
    }
	public Integer getSortNo() {
		return sortNo;
	}
	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}
    
}