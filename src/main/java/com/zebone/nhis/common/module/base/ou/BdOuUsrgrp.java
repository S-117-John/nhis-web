package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 用户组
 * @author Xulj
 *
 */
@Table(value="bd_ou_usrgrp")
public class BdOuUsrgrp {

	/**
	 * 用户组主键
	 */
	@PK
	@Field(value="pk_usrgrp",id=KeyId.UUID)
	private String pkUsrgrp;
	
	/**
	 * 用户组编码
	 */
	@Field(value="code_usrgrp")
	private String codeUsrgrp;
	
	/**
	 * 用户组名称
	 */
	@Field(value="name_usrgrp")
	private String nameUsrgrp;
	
	/**
	 * 备注
	 */
	@Field(value="note")
	private String note;
	
	/**
     * 机构主键
     */
	@Field(value="pk_org",userfield="pkOrg",userfieldscop=FieldType.INSERT)
    public String pkOrg;

	/**
     * 创建人
     */
	@Field(userfield="pkEmp",userfieldscop=FieldType.INSERT)
    public String creator;

	/**
     * 创建时间
     */
	@Field(value="create_time",date=FieldType.INSERT)
	public Date createTime;
    

	/**
     * 时间戳
     */
	@Field(date=FieldType.ALL)
	public Date ts;
	
	/**
	 * 拼音码
	 */
	@Field(value="spcode")
	private String spcode;
	
	
	/**
	 * 自定义码
	 */
	@Field(value="d_code")
	private String dCode;

	public String getPkUsrgrp() {
		return pkUsrgrp;
	}

	public void setPkUsrgrp(String pkUsrgrp) {
		this.pkUsrgrp = pkUsrgrp;
	}

	public String getCodeUsrgrp() {
		return codeUsrgrp;
	}

	public void setCodeUsrgrp(String codeUsrgrp) {
		this.codeUsrgrp = codeUsrgrp;
	}

	public String getNameUsrgrp() {
		return nameUsrgrp;
	}

	public void setNameUsrgrp(String nameUsrgrp) {
		this.nameUsrgrp = nameUsrgrp;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getPkOrg() {
		return pkOrg;
	}

	public void setPkOrg(String pkOrg) {
		this.pkOrg = pkOrg;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getTs() {
		return ts;
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

	public String getdCode() {
		return dCode;
	}

	public void setdCode(String dCode) {
		this.dCode = dCode;
	}
	

}
