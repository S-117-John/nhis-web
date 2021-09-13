package com.zebone.nhis.common.module.arch;

import java.util.Date;

import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * Table: BD_ARCH_SRVCONF
 * 
 * @since 2017-04-25 05:17:19
 */
@Table(value = "BD_ARCH_SRVCONF")
public class BdArchSrvconf {

	@PK
	@Field(value = "PK_SRVCONF", id = KeyId.UUID)
	private String pkSrvconf;

	@Field(value = "pk_org", userfield = "pkOrg", userfieldscop = FieldType.INSERT)
	private String pkOrg;

	@Field(value = "CODE")
	private String code;

	@Field(value = "NAME")
	private String name;

	@Field(value = "URL_SRV")
	private String urlSrv;

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

	public String getPkSrvconf() {

		return this.pkSrvconf;
	}

	public void setPkSrvconf(String pkSrvconf) {

		this.pkSrvconf = pkSrvconf;
	}

	public String getPkOrg() {

		return this.pkOrg;
	}

	public void setPkOrg(String pkOrg) {

		this.pkOrg = pkOrg;
	}

	public String getCode() {

		return this.code;
	}

	public void setCode(String code) {

		this.code = code;
	}

	public String getName() {

		return this.name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getUrlSrv() {

		return urlSrv;
	}

	public void setUrlSrv(String urlSrv) {

		this.urlSrv = urlSrv;
	}

	public String getNote() {

		return note;
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