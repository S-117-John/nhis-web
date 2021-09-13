package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: PV_ARCH_COMT_DT
 * 
 * @since 2017-04-27 10:56:12
 */
@Table(value = "PV_ARCH_COMT_DT")
public class PvArchComtDt {

	@PK
	@Field(value = "PK_COMTDT", id = KeyId.UUID)
	private String pkComtdt;

	@Field(value = "PK_COMT")
	private String pkComt;

	@Field(value = "PK_DOC")
	private String pkDoc;

	@Field(value = "COMMENT_COMT")
	private String commentComt;

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

	public String getPkComtdt() {

		return this.pkComtdt;
	}

	public void setPkComtdt(String pkComtdt) {

		this.pkComtdt = pkComtdt;
	}

	public String getPkComt() {

		return this.pkComt;
	}

	public void setPkComt(String pkComt) {

		this.pkComt = pkComt;
	}

	public String getPkDoc() {

		return this.pkDoc;
	}

	public void setPkDoc(String pkDoc) {

		this.pkDoc = pkDoc;
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

	public String getCommentComt() {
		return commentComt;
	}

	public void setCommentComt(String commentComt) {
		this.commentComt = commentComt;
	}
	
	
}