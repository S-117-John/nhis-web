package com.zebone.nhis.common.module.scm.pub;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_PD_DECATE - bd_pd_decate
 * 
 * @since 2016-11-07 11:12:34
 */
@Table(value = "BD_PD_DECATE")
public class BdPdDecate extends BaseModule {

	@PK
	@Field(value = "PK_PDDECATE", id = KeyId.UUID)
	private String pkPddecate;

	/** EU_RANGE - 0 全局，1科室 */
	@Field(value = "EU_RANGE")
	private String euRange;

	@Field(value = "PK_ORG_USE")
	private String pkOrgUse;

	@Field(value = "PK_DEPT_USE")
	private String pkDeptUse;

	@Field(value = "CODE_DECATE")
	private String codeDecate;

	@Field(value = "NAME_DECATE")
	private String nameDecate;

	@Field(value = "WHERESQL")
	private String wheresql;

	@Field(value = "NOTE")
	private String note;

	@Field(value = "MODITY_TIME")
	private Date modityTime;

	@Field(value = "EU_DOCTYPE")
	private String euDoctype;

	@Field(value = "Flag_Label")
	private String flagLabel;

	/** EU_RANGE - 0 全局，1科室 */
	@Field(value = "EU_TYPE")

	private String euType;

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getEuDoctype() {
		return euDoctype;
	}

	public void setEuDoctype(String euDoctype) {
		this.euDoctype = euDoctype;
	}

	public String getFlagLabel() {
		return flagLabel;
	}

	public void setFlagLabel(String flagLabel) {
		this.flagLabel = flagLabel;
	}

	public String getPkPddecate() {

		return this.pkPddecate;
	}

	public void setPkPddecate(String pkPddecate) {

		this.pkPddecate = pkPddecate;
	}

	public String getEuRange() {

		return this.euRange;
	}

	public void setEuRange(String euRange) {

		this.euRange = euRange;
	}

	public String getPkOrgUse() {

		return this.pkOrgUse;
	}

	public void setPkOrgUse(String pkOrgUse) {

		this.pkOrgUse = pkOrgUse;
	}

	public String getPkDeptUse() {

		return this.pkDeptUse;
	}

	public void setPkDeptUse(String pkDeptUse) {

		this.pkDeptUse = pkDeptUse;
	}

	public String getCodeDecate() {

		return this.codeDecate;
	}

	public void setCodeDecate(String codeDecate) {

		this.codeDecate = codeDecate;
	}

	public String getNameDecate() {

		return this.nameDecate;
	}

	public void setNameDecate(String nameDecate) {

		this.nameDecate = nameDecate;
	}

	public String getWheresql() {

		return this.wheresql;
	}

	public void setWheresql(String wheresql) {

		this.wheresql = wheresql;
	}

	public String getNote() {

		return this.note;
	}

	public void setNote(String note) {

		this.note = note;
	}

	public Date getModityTime() {

		return this.modityTime;
	}

	public void setModityTime(Date modityTime) {

		this.modityTime = modityTime;
	}
}