package com.zebone.nhis.common.module.cn.ipdw;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;


/**
 * Table: BD_CP_TASK 
 *
 * @since 2019-05-13 
 */
@Table(value="BD_CP_TASK")
public class BdCpTask extends BaseModule  {

	@PK
	@Field(value="PK_CPTASK",id=KeyId.UUID)
    private String pkCptask;

	@Field(value="PK_ORG")
	private String pkOrg;
	
	@Field(value="EU_TYPE")
    private String euType;
	
	@Field(value="SORTNO")
    private Integer sortno;

	@Field(value="NAME_TASK")
    private String nameTask;

	@Field(value="SPCODE")
    private String spcode;

	@Field(value="D_CODE")
    private String dCode;

	@Field(value="CODE_ORDTYPE")
    private String codeOrdtype;
	
	@Field(value="EU_REPTYPE")
	private String euReptype;
	
	@Field(value="DT_PHARM")
	private String dtPharm;
	
	@Field(value="PK_MENU")
	private String pkMenu;
	
	@Field(value="FLAG_ACTIVE")
	private String flagActive;
	
	@Field(value="FLAG_MULT")
	private String flagMult;

	public String getFlagMult() {
		return flagMult;
	}

	public void setFlagMult(String flagMult) {
		this.flagMult = flagMult;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getPkCptask() {
		return pkCptask;
	}

	public void setPkCptask(String pkCptask) {
		this.pkCptask = pkCptask;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public Integer getSortno() {
		return sortno;
	}

	public void setSortno(Integer sortno) {
		this.sortno = sortno;
	}

	public String getNameTask() {
		return nameTask;
	}

	public void setNameTask(String nameTask) {
		this.nameTask = nameTask;
	}

	public String getSpcode() {
		return spcode;
	}

	public void setSpcode(String spcode) {
		this.spcode = spcode;
	}

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}

	public String getCodeOrdtype() {
		return codeOrdtype;
	}

	public void setCodeOrdtype(String codeOrdtype) {
		this.codeOrdtype = codeOrdtype;
	}

	public String getEuReptype() {
		return euReptype;
	}

	public void setEuReptype(String euReptype) {
		this.euReptype = euReptype;
	}

	public String getDtPharm() {
		return dtPharm;
	}

	public void setDtPharm(String dtPharm) {
		this.dtPharm = dtPharm;
	}

	public String getPkMenu() {
		return pkMenu;
	}

	public void setPkMenu(String pkMenu) {
		this.pkMenu = pkMenu;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	
	
}