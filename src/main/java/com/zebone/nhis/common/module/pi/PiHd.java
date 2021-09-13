package com.zebone.nhis.common.module.pi;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import java.util.Date;

/**
 * Table: PI_HD  - 患者信息_血液透析
 *
 * @since 2018-11-23 10:12:41
 */
@Table(value="PI_HD")
public class PiHd extends BaseModule{
	
	/** PK_FAMILY - 血液透析主键 */
	@PK
	@Field(value="PK_PIHD",id=KeyId.UUID)
	private String pkPihd;
	
	/** 机构 **/
	@Field(value="PK_ORG")
	private String pkOrg;
	
	/** 信息来源 **/
	@Field(value="EU_MCSRC")
	private String euMcsrc;
	
	/** 患者主键 **/
	@Field(value="PK_PI")
	private String pkPi;
	
	/** 透析号码 **/
	@Field(value="CODE_HD")
	private String codeHd;
	
	/** 透析方式 **/
	@Field(value="DT_HDTYPE")
	private String dtHdtype;
	
	/** 首次肾脏替代治疗日期 **/
	@Field(value="DATE_FIRST")
	private Date dateFirst;
	
	/** 死亡日期 **/
	@Field(value="DATE_DEATH")
	private Date dateDeath;
	
	/** 建档日期 **/
	@Field(value="DATE_ARCH")
	private Date dateArch;
	
	/** 建档医生 **/
	@Field(value="PK_EMP")
	private String pkEmp;
	
	/** 建档医生姓名 **/
	@Field(value="NAME_EMP")
	private String nameEmp;
	
	/** 状态 **/
	@Field(value="EU_STATUS")
	private String euStatus;
	
	/** 备注 **/
	@Field(value="NOTE")
	private String note;
	
	/** 周透析次数 **/
	@Field(value="CNT_WEEK")
	private Integer cntWeek;

	public String getPkPihd() {
		return pkPihd;
	}

	public void setPkPihd(String pkPihd) {
		this.pkPihd = pkPihd;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getEuMcsrc() {
		return euMcsrc;
	}

	public void setEuMcsrc(String euMcsrc) {
		this.euMcsrc = euMcsrc;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getCodeHd() {
		return codeHd;
	}

	public void setCodeHd(String codeHd) {
		this.codeHd = codeHd;
	}

	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public Date getDateFirst() {
		return dateFirst;
	}

	public void setDateFirst(Date dateFirst) {
		this.dateFirst = dateFirst;
	}

	public Date getDateDeath() {
		return dateDeath;
	}

	public void setDateDeath(Date dateDeath) {
		this.dateDeath = dateDeath;
	}

	public Date getDateArch() {
		return dateArch;
	}

	public void setDateArch(Date dateArch) {
		this.dateArch = dateArch;
	}

	public String getPkEmp() {
		return pkEmp;
	}

	public void setPkEmp(String pkEmp) {
		this.pkEmp = pkEmp;
	}

	public String getNameEmp() {
		return nameEmp;
	}

	public void setNameEmp(String nameEmp) {
		this.nameEmp = nameEmp;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getCntWeek() {
		return cntWeek;
	}

	public void setCntWeek(Integer cntWeek) {
		this.cntWeek = cntWeek;
	}
	
}
