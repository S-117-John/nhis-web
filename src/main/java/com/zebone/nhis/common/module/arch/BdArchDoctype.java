package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_ARCH_DOCTYPE
 * 
 * @since 2017-04-25 03:32:37
 */
@Table(value = "BD_ARCH_DOCTYPE")
public class BdArchDoctype {

	@PK
	@Field(value = "PK_DOCTYPE", id = KeyId.UUID)
	private String pkDoctype;

	@Field(value = "pk_org", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
	private String pkOrg;

	@Field(value = "CODE_DOCTYPE")
	private String codeDoctype;

	@Field(value = "NAME_DOCTYPE")
	private String nameDoctype;

	@Field(value = "EU_DOCTYPE")
	private String euDoctype;

	@Field(value = "DT_SYSTYPE")
	private String dtSystype;

	@Field(value = "CODE_MAP")
	private String codeMap;

	@Field(value = "CODE_EXT")
	private String codeExt;
	
	@Field(value = "SPCODE")
	private String spcode;

	@Field(value = "SORTNO")
	private Integer sortno;

	@Field(value = "NOTE")
	private String note;

	@Field(value = "FLAG_ACTIVE")
	private String flagActive;

	@Field(value = "FLAG_PRT")
	private String flagPrt;

	@Field(value = "FLAG_NEC")
	private String flagNec;

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

	public String getPkDoctype() {

		return this.pkDoctype;
	}

	public void setPkDoctype(String pkDoctype) {

		this.pkDoctype = pkDoctype;
	}

	public String getPkOrg() {

		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getCodeDoctype() {

		return this.codeDoctype;
	}

	public void setCodeDoctype(String codeDoctype) {

		this.codeDoctype = codeDoctype;
	}

	public String getNameDoctype() {

		return this.nameDoctype;
	}

	public void setNameDoctype(String nameDoctype) {

		this.nameDoctype = nameDoctype;
	}

	public String getEuDoctype() {

		return this.euDoctype;
	}

	public void setEuDoctype(String euDoctype) {

		this.euDoctype = euDoctype;
	}

	public String getDtSystype() {

		return this.dtSystype;
	}

	public void setDtSystype(String dtSystype) {

		this.dtSystype = dtSystype;
	}

	public String getCodeMap() {

		return this.codeMap;
	}

	public void setCodeMap(String codeMap) {

		this.codeMap = codeMap;
	}

	public Integer getSortno() {

		return sortno;
	}

	public void setSortno(Integer sortno) {

		this.sortno = sortno;
	}

	public String getNote() {

		return note;
	}

	public void setNote(String note) {

		this.note = note;
	}

	public String getFlagActive() {

		return this.flagActive;
	}

	public void setFlagActive(String flagActive) {

		this.flagActive = flagActive;
	}

	public String getFlagPrt() {

		return this.flagPrt;
	}

	public void setFlagPrt(String flagPrt) {

		this.flagPrt = flagPrt;
	}

	public String getFlagNec() {

		return this.flagNec;
	}

	public void setFlagNec(String flagNec) {

		this.flagNec = flagNec;
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

	public String getSpcode() {

		return spcode;
	}

	public void setSpcode(String spcode) {

		this.spcode = spcode;
	}

	public String getCodeExt() {
		return codeExt;
	}

	public void setCodeExt(String codeExt) {
		this.codeExt = codeExt;
	}

}