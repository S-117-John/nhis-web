package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: PV_ARCH_SEAL
 * 
 * @since 2017-04-27 10:55:38
 */
@Table(value = "PV_ARCH_SEAL")
public class PvArchSeal {

	@PK
	@Field(value = "PK_SEAL", id = KeyId.UUID)
	private String pkSeal;

	@Field(value = "pk_org", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
	private String pkOrg;

	@Field(value = "PK_ARCHIVE")
	private String pkArchive;

	@Field(value = "EU_OPTYPE")
	private String euOptype;

	@Field(value = "DATE_OP")
	private Date dateOp;

	@Field(value = "PK_EMP_OP")
	private String pkEmpOp;

	@Field(value = "NAME_EMP_OP")
	private String nameEmpOp;

	@Field(value = "NOTE")
	private String note;

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

	public String getPkSeal() {

		return this.pkSeal;
	}

	public void setPkSeal(String pkSeal) {

		this.pkSeal = pkSeal;
	}

	public String getPkOrg() {

		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getPkArchive() {

		return this.pkArchive;
	}

	public void setPkArchive(String pkArchive) {

		this.pkArchive = pkArchive;
	}

	public String getEuOptype() {

		return this.euOptype;
	}

	public void setEuOptype(String euOptype) {

		this.euOptype = euOptype;
	}

	public Date getDateOp() {

		return this.dateOp;
	}

	public void setDateOp(Date dateOp) {

		this.dateOp = dateOp;
	}

	public String getPkEmpOp() {

		return this.pkEmpOp;
	}

	public void setPkEmpOp(String pkEmpOp) {

		this.pkEmpOp = pkEmpOp;
	}

	public String getNameEmpOp() {

		return this.nameEmpOp;
	}

	public void setNameEmpOp(String nameEmpOp) {

		this.nameEmpOp = nameEmpOp;
	}

	public String getNote() {

		return this.note;
	}

	public void setNote(String note) {

		this.note = note;
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
}