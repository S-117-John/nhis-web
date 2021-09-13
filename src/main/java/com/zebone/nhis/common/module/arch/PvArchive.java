package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: PV_ARCHIVE
 * 
 * @since 2017-04-27 10:54:55
 */
@Table(value = "PV_ARCHIVE")
public class PvArchive {

	@PK
	@Field(value = "PK_ARCHIVE", id = KeyId.UUID)
	private String pkArchive;

	@Field(value = "pk_org", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
	private String pkOrg;
	
	@Field(value = "PK_PI")
	private String pkPi;
	
	@Field(value = "PK_PV")
	private String pkPv;

	@Field(value = "CODE_ARCH")
	private String codeArch;

	@Field(value = "FLAG_REV")
	private String flagRev;

	@Field(value = "DATE_REV")
	private Date dateRev;

	@Field(value = "PK_EMP_REV")
	private String pkEmpRev;

	@Field(value = "NAME_EMP_REV")
	private String nameEmpRev;

	@Field(value = "EU_ARCHTYPE")
	private String euArchtype;

	@Field(value = "FLAG_ARCH")
	private String flagArch;

	@Field(value = "DATE_ARCH")
	private Date dateArch;

	@Field(value = "PK_EMP_ARCH")
	private String pkEmpArch;

	@Field(value = "NAME_EMP_ARCH")
	private String nameEmpArch;

	@Field(value = "CNT_TOTAL")
	private String cntTotal;

	@Field(value = "CNT_ARCH")
	private String cntArch;

	@Field(value = "EU_STATUS")
	private String euStatus;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.INSERT)
	private String creator;

	@Field(value = "CREATE_TIME", date = FieldType.INSERT)
	private Date createTime;

	@Field(userfield = "pkEmp", userfieldscop = FieldType.ALL)
	private String modifier;

	@Field(value = "MODIFY_TIME")
	private Date modifyTime;

	@Field(value = "FLAG_DEL")
	private String flagDel;

	@Field(date = FieldType.ALL)
	private Date ts;

	@Field(value = "PATICODE")
	private String patiCode;
	
	@Field(value = "PID")
	private String pid;
	
	@Field(value = "PATINAME")
	private String patiName;
	
	@Field(value = "VISITCODE")
	private String visitCode;
	
	@Field(value = "TIMES")
	private Integer times;
	
	@Field(value = "VISITTYPE")
	private String visittype;
	
	
	private String codeOp;

	
	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}

	public String getPkArchive() {

		return this.pkArchive;
	}

	public void setPkArchive(String pkArchive) {

		this.pkArchive = pkArchive;
	}

	public String getPkOrg() {

		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getPkPv() {

		return this.pkPv;
	}

	public void setPkPv(String pkPv) {

		this.pkPv = pkPv;
	}

	public String getCodeArch() {

		return this.codeArch;
	}

	public void setCodeArch(String codeArch) {

		this.codeArch = codeArch;
	}

	public String getFlagRev() {

		return this.flagRev;
	}

	public void setFlagRev(String flagRev) {

		this.flagRev = flagRev;
	}

	public Date getDateRev() {

		return this.dateRev;
	}

	public void setDateRev(Date dateRev) {

		this.dateRev = dateRev;
	}

	public String getPkEmpRev() {

		return this.pkEmpRev;
	}

	public void setPkEmpRev(String pkEmpRev) {

		this.pkEmpRev = pkEmpRev;
	}

	public String getNameEmpRev() {

		return this.nameEmpRev;
	}

	public void setNameEmpRev(String nameEmpRev) {

		this.nameEmpRev = nameEmpRev;
	}

	public String getEuArchtype() {

		return this.euArchtype;
	}

	public void setEuArchtype(String euArchtype) {

		this.euArchtype = euArchtype;
	}

	public String getFlagArch() {

		return this.flagArch;
	}

	public void setFlagArch(String flagArch) {

		this.flagArch = flagArch;
	}

	public Date getDateArch() {

		return this.dateArch;
	}

	public void setDateArch(Date dateArch) {

		this.dateArch = dateArch;
	}

	public String getPkEmpArch() {

		return this.pkEmpArch;
	}

	public void setPkEmpArch(String pkEmpArch) {

		this.pkEmpArch = pkEmpArch;
	}

	public String getNameEmpArch() {

		return this.nameEmpArch;
	}

	public void setNameEmpArch(String nameEmpArch) {

		this.nameEmpArch = nameEmpArch;
	}

	public String getCntTotal() {

		return this.cntTotal;
	}

	public void setCntTotal(String cntTotal) {

		this.cntTotal = cntTotal;
	}

	public String getCntArch() {

		return this.cntArch;
	}

	public void setCntArch(String cntArch) {

		this.cntArch = cntArch;
	}

	public String getEuStatus() {

		return this.euStatus;
	}

	public void setEuStatus(String euStatus) {

		this.euStatus = euStatus;
	}

	public String getCreator() {

		return this.creator;
	}

	public void setCreator(String creator) {

		this.creator = creator;
	}

	public Date getCreateTime() {

		return this.createTime;
	}

	public void setCreateTime(Date createTime) {

		this.createTime = createTime;
	}

	public String getModifier() {

		return this.modifier;
	}

	public void setModifier(String modifier) {

		this.modifier = modifier;
	}

	public Date getModifyTime() {

		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {

		this.modifyTime = modifyTime;
	}

	public String getFlagDel() {

		return this.flagDel;
	}

	public void setFlagDel(String flagDel) {

		this.flagDel = flagDel;
	}

	public Date getTs() {

		return this.ts;
	}

	public void setTs(Date ts) {

		this.ts = ts;
	}

	public String getPatiCode() {
		return patiCode;
	}

	public void setPatiCode(String patiCode) {
		this.patiCode = patiCode;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getPatiName() {
		return patiName;
	}

	public void setPatiName(String patiName) {
		this.patiName = patiName;
	}

	public String getVisitCode() {
		return visitCode;
	}

	public void setVisitCode(String visitCode) {
		this.visitCode = visitCode;
	}

	public String getVisittype() {
		return visittype;
	}

	public void setVisittype(String visittype) {
		this.visittype = visittype;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}


	
}