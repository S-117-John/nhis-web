package com.zebone.nhis.arch.vo;

import java.util.Date;
import java.util.List;

import com.zebone.nhis.common.module.arch.PvArchComtDt;

public class ArchiveDto {

	private String codePv;
	/**
	 * 住院号
	 */
	private String codeIp;

	private String namePi;

	private String codeOp;
	/**
	 * 患者编号
	 */
	private String codePi;

	private String pkDept;

	private String pkOrg;

	private Date dateBegin;

	private Date dateEnd;

	private List<PvArchiveVO> pvArchives;

	private String pkArchive;

	private String codeSys;

	private Date outDateStart;

	private Date outDateEnd;

	private Date inDateStart;

	private Date inDateEnd;

	private Date archDateStart;

	private Date archDateEnd;

	private String EuStatus;

	private String flagArch;

	private String OperateReason;

	private String EuOptype;

	private String pkDoctype;

	private String pkEmpComt;

	
	private String nameEmpAp;
	
	
	private PvArchComtDt pvArchComtDt;

	private String flagArchSearch;
	
	private String ipTimes;//患者住院次数
	
	private String pkPv;
	
	private String archEuStatus;
	
	private String scanEuStatus;
	
	private String scanSql;
	
	
	public String getScanSql() {
		return scanSql;
	}

	public void setScanSql(String scanSql) {
		this.scanSql = scanSql;
	}

	public String getScanEuStatus() {
		return scanEuStatus;
	}

	public void setScanEuStatus(String scanEuStatus) {
		this.scanEuStatus = scanEuStatus;
	}

	public String getArchEuStatus() {
		return archEuStatus;
	}

	public void setArchEuStatus(String archEuStatus) {
		this.archEuStatus = archEuStatus;
	}

	public String getPkPv() {
		return pkPv;
	}

	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}

	public String getIpTimes() {
		return ipTimes;
	}

	public void setIpTimes(String ipTimes) {
		this.ipTimes = ipTimes;
	}

	

	public String getFlagArchSearch() {
		return flagArchSearch;
	}

	public void setFlagArchSearch(String flagArchSearch) {
		this.flagArchSearch = flagArchSearch;
	}

	public String getCodeIp() {
		return codeIp;
	}

	public void setCodeIp(String codeIp) {
		this.codeIp = codeIp;
	}

	public String getNameEmpAp() {
		return nameEmpAp;
	}

	public void setNameEmpAp(String nameEmpAp) {
		this.nameEmpAp = nameEmpAp;
	}

	public Date getOutDateStart() {

		return outDateStart;
	}

	public void setOutDateStart(Date outDateStart) {

		this.outDateStart = outDateStart;
	}

	public Date getOutDateEnd() {

		return outDateEnd;
	}

	public void setOutDateEnd(Date outDateEnd) {

		this.outDateEnd = outDateEnd;
	}

	public Date getArchDateStart() {

		return archDateStart;
	}

	public void setArchDateStart(Date archDateStart) {

		this.archDateStart = archDateStart;
	}

	public Date getArchDateEnd() {

		return archDateEnd;
	}

	public void setArchDateEnd(Date archDateEnd) {

		this.archDateEnd = archDateEnd;
	}

	public String getCodePv() {

		return codePv;
	}

	public void setCodePv(String codePv) {

		this.codePv = codePv;
	}

	public String getNamePi() {

		return namePi;
	}

	public void setNamePi(String namePi) {

		this.namePi = namePi;
	}

	public String getCodePi() {

		return codePi;
	}

	public void setCodePi(String codePi) {

		this.codePi = codePi;
	}

	public String getPkDept() {

		return pkDept;
	}

	public void setPkDept(String pkDept) {

		this.pkDept = pkDept;
	}

	public String getPkOrg() {

		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
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

	public String getPkArchive() {

		return pkArchive;
	}

	public void setPkArchive(String pkArchive) {

		this.pkArchive = pkArchive;
	}

	public String getCodeSys() {

		return codeSys;
	}

	public void setCodeSys(String codeSys) {

		this.codeSys = codeSys;
	}

	public Date getInDateStart() {

		return inDateStart;
	}

	public void setInDateStart(Date inDateStart) {

		this.inDateStart = inDateStart;
	}

	public Date getInDateEnd() {

		return inDateEnd;
	}

	public void setInDateEnd(Date inDateEnd) {

		this.inDateEnd = inDateEnd;
	}

	public String getEuStatus() {

		return EuStatus;
	}

	public void setEuStatus(String euStatus) {

		EuStatus = euStatus;
	}

	public String getFlagArch() {

		return flagArch;
	}

	public void setFlagArch(String flagArch) {

		this.flagArch = flagArch;
	}

	public String getOperateReason() {

		return OperateReason;
	}

	public void setOperateReason(String operateReason) {

		OperateReason = operateReason;
	}

	public String getEuOptype() {

		return EuOptype;
	}

	public void setEuOptype(String euOptype) {

		EuOptype = euOptype;
	}

	public PvArchComtDt getPvArchComtDt() {

		return pvArchComtDt;
	}

	public void setPvArchComtDt(PvArchComtDt pvArchComtDt) {

		this.pvArchComtDt = pvArchComtDt;
	}

	public String getPkDoctype() {

		return pkDoctype;
	}

	public void setPkDoctype(String pkDoctype) {

		this.pkDoctype = pkDoctype;
	}

	public String getPkEmpComt() {

		return pkEmpComt;
	}

	public void setPkEmpComt(String pkEmpComt) {

		this.pkEmpComt = pkEmpComt;
	}

	public List<PvArchiveVO> getPvArchives() {
		return pvArchives;
	}

	public void setPvArchives(List<PvArchiveVO> pvArchives) {
		this.pvArchives = pvArchives;
	}

	public String getCodeOp() {
		return codeOp;
	}

	public void setCodeOp(String codeOp) {
		this.codeOp = codeOp;
	}
	
}
