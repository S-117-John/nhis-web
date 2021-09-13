package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 用户组角色表
 * @author Xulj
 *
 */
@Table(value="bd_ou_usrgrp_role")
public class BdOuUsrgrpRole{

	/**
	 * 角色用户主键
	 */
	@Field(value="pk_usrgrp_role",id=KeyId.UUID)
	private String pkUsrgrpRole;
	
	/**
	 * 用户组主键
	 */
	@Field(value="pk_usrgrp")
	private String pkUsrgrp;
	
	/**
	 * 角色主键
	 */
	@Field(value="pk_role")
	private String pkRole;
	
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
	
	public String getPkUsrgrpRole() {
		return pkUsrgrpRole;
	}

	public void setPkUsrgrpRole(String pkUsrgrpRole) {
		this.pkUsrgrpRole = pkUsrgrpRole;
	}

	public String getPkUsrgrp() {
		return pkUsrgrp;
	}

	public void setPkUsrgrp(String pkUsrgrp) {
		this.pkUsrgrp = pkUsrgrp;
	}

	public String getPkRole() {
		return pkRole;
	}

	public void setPkRole(String pkRole) {
		this.pkRole = pkRole;
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

}
