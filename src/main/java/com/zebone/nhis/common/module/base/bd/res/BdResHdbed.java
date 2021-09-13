package com.zebone.nhis.common.module.base.bd.res;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * Table: BD_RES_HDBED
 * 
 * @since 
 */
@Table(value="BD_RES_HDBED")
public class BdResHdbed extends BaseModule{
	@PK
	@Field(value="PK_HDBED",id=KeyId.UUID)
    private String pkHdbed;
	
	@Field(value="PK_ORG")
    private String pkOrg;
	
	@Field(value="CODE_BED")
    private String codeBed;

	@Field(value="NAME_BED")
    private String nameBed;
	
	@Field(value="SORTNO")
    private int sortno;

	@Field(value="PK_DEPT")
    private String pkDept;

	@Field(value="PK_DEPT_NS")
    private String pkDeptNs;

	@Field(value="PK_MSP")
    private String pkMsp;

	@Field(value="DT_HDTYPE")
    private String dtHdtype;

	@Field(value="FLAG_OPEN")
    private String flagOpen;

	@Field(value="EU_STATUS")
    private String euStatus;

	@Field(value="PK_PI")
    private String pkPi;

	@Field(value="PK_WORKCALENDAR")
    private String pkWorkcalendar;

	@Field(value="DT_DATESLOTTYPE")
    private String dtDateslottype;

	@Field(value="NOTE")
    private String note;

	public String getPkHdbed() {
		return pkHdbed;
	}

	public void setPkHdbed(String pkHdbed) {
		this.pkHdbed = pkHdbed;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCodeBed() {
		return codeBed;
	}

	public void setCodeBed(String codeBed) {
		this.codeBed = codeBed;
	}

	public String getNameBed() {
		return nameBed;
	}

	public void setNameBed(String nameBed) {
		this.nameBed = nameBed;
	}

	public int getSortno() {
		return sortno;
	}

	public void setSortno(int sortno) {
		this.sortno = sortno;
	}

	public String getPkDept() {
		return pkDept;
	}

	public void setPkDept(String pkDept) {
		this.pkDept = pkDept;
	}

	public String getPkDeptNs() {
		return pkDeptNs;
	}

	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}

	public String getPkMsp() {
		return pkMsp;
	}

	public void setPkMsp(String pkMsp) {
		this.pkMsp = pkMsp;
	}

	public String getDtHdtype() {
		return dtHdtype;
	}

	public void setDtHdtype(String dtHdtype) {
		this.dtHdtype = dtHdtype;
	}

	public String getFlagOpen() {
		return flagOpen;
	}

	public void setFlagOpen(String flagOpen) {
		this.flagOpen = flagOpen;
	}

	public String getEuStatus() {
		return euStatus;
	}

	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}

	public String getPkPi() {
		return pkPi;
	}

	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}

	public String getPkWorkcalendar() {
		return pkWorkcalendar;
	}

	public void setPkWorkcalendar(String pkWorkcalendar) {
		this.pkWorkcalendar = pkWorkcalendar;
	}

	public String getDtDateslottype() {
		return dtDateslottype;
	}

	public void setDtDateslottype(String dtDateslottype) {
		this.dtDateslottype = dtDateslottype;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
