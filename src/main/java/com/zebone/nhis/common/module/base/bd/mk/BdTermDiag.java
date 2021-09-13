package com.zebone.nhis.common.module.base.bd.mk;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * 
 * 标准诊断编码定义
 * Table BD_TERM_DIAG - bd_term_diag
 * @date 2016年8月26日
 *
 */
@Table(value="BD_TERM_DIAG")
public class BdTermDiag{

	/** 诊断主键 */
	@PK
	@Field(value="PK_DIAG",id=KeyId.UUID)
    private String pkDiag;
	
	/** 编码体系 0X ICD：00 ICD10，01 ICD9。1X 中医：10 中医，11 蒙医。99 未指定 */
	@Field(value="DT_SYSTEM")
	private String dtSystem;
	
	/** 健康档案标准分类 */
	@Field(value="DT_CODE_EHR")
	private String dtCodeEhr;
	
	/** 诊断编码 */
	@Field(value="DIAGCODE")
	private String diagcode;
	
	/** 诊断名称 */
	@Field(value="DIAGNAME")
	private String diagname;
	
	/** 拼音码 */
	@Field(value="PY_CODE")
	private String pyCode;
	
	/** 自定义码 */
	@Field(value="D_CODE")
	private String dCode;
	
	/** 上级诊断编码 */
	@Field(value="PK_FATHER")
	private String pkFather;
	
	/** 公共服务慢病 */
	@Field(value="DT_CODE_PH")
	private String dtCodePh;
	
	/** 患者标识 */
	@Field(value="FLAG_PI")
	private String flagPi;
	
	/** 手术级别:1 一级，2 二级，3 三级，4 四级 */
	@Field(value="LEVEL_OP")
	private String levelOp;
	
	/** 自定义分类 */
	@Field(value="DT_DIAGCATE")
	private String dtDiagcate;
	
	/** 传染病分类 */
	@Field(value="DT_INFECTIONCATE")
	private String dtInfectioncate;
	
	/** 主数据ID */
	@Field(value="ID_MASTER")
	private String idMaster;
	
	@Field(value="FLAG_STOP")
	private String flagStop;
	
	@Field(value="DT_DIAGTYPE")
	private String dtDiagtype;
	
	@Field(value="VERSION")
	private String version;
	
	@Field(value="CODE_ADD")
	private String codeAdd;
	
	@Field(value="CODE_STAT")
	private String codeStat;
	
	@Field(value="DT_OPLEVEL")
	private String dtOplevel;

	
	public String getDtInfectioncate() {
		return dtInfectioncate;
	}

	public void setDtInfectioncate(String dtInfectioncate) {
		this.dtInfectioncate = dtInfectioncate;
	}

	public String getDtOplevel() {
		return dtOplevel;
	}

	public void setDtOplevel(String dtOplevel) {
		this.dtOplevel = dtOplevel;
	}

	public String getFlagStop() {
		return flagStop;
	}

	public void setFlagStop(String flagStop) {
		this.flagStop = flagStop;
	}

	public String getDtDiagtype() {
		return dtDiagtype;
	}

	public void setDtDiagtype(String dtDiagtype) {
		this.dtDiagtype = dtDiagtype;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCodeAdd() {
		return codeAdd;
	}

	public void setCodeAdd(String codeAdd) {
		this.codeAdd = codeAdd;
	}

	public String getCodeStat() {
		return codeStat;
	}

	public void setCodeStat(String codeStat) {
		this.codeStat = codeStat;
	}

	public String getIdMaster() {
		return idMaster;
	}

	public void setIdMaster(String idMaster) {
		this.idMaster = idMaster;
	}

	public String getDtDiagcate() {
		return dtDiagcate;
	}

	public void setDtDiagcate(String dtDiagcate) {
		this.dtDiagcate = dtDiagcate;
	}

	public String getPkDiag() {
		return pkDiag;
	}

	public void setPkDiag(String pkDiag) {
		this.pkDiag = pkDiag;
	}

	public String getDtSystem() {
		return dtSystem;
	}

	public void setDtSystem(String dtSystem) {
		this.dtSystem = dtSystem;
	}

	public String getDtCodeEhr() {
		return dtCodeEhr;
	}

	public void setDtCodeEhr(String dtCodeEhr) {
		this.dtCodeEhr = dtCodeEhr;
	}

	public String getDiagcode() {
		return diagcode;
	}

	public void setDiagcode(String diagcode) {
		this.diagcode = diagcode;
	}

	public String getDiagname() {
		return diagname;
	}

	public void setDiagname(String diagname) {
		this.diagname = diagname;
	}

	public String getPyCode() {
		return pyCode;
	}

	public void setPyCode(String pyCode) {
		this.pyCode = pyCode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getPkFather() {
		return pkFather;
	}

	public void setPkFather(String pkFather) {
		this.pkFather = pkFather;
	}

	public String getDtCodePh() {
		return dtCodePh;
	}

	public void setDtCodePh(String dtCodePh) {
		this.dtCodePh = dtCodePh;
	}

	public String getFlagPi() {
		return flagPi;
	}

	public void setFlagPi(String flagPi) {
		this.flagPi = flagPi;
	}

	public String getLevelOp() {
		return levelOp;
	}

	public void setLevelOp(String levelOp) {
		this.levelOp = levelOp;
	}

}
