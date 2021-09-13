package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 用户
 * @author Xulj
 *
 */
@Table(value="bd_ou_user")
public class BdOuUser{

	/**
	 * 用户主键
	 */
	@PK
	@Field(value="pk_user",id=KeyId.UUID)
	private String pkUser;
	
	/**
	 * 用户编码
	 */
	@Field(value="code_user")
	private String codeUser;
	
	/**
	 * 用户类型
	 */
	@Field(value="eu_usertype")
	private String euUsertype;
	
	/**
	 * 用户名称
	 */
	@Field(value="name_user")
	private String nameUser;
	
	/**
	 * 所属用户组
	 */
	@Field(value="pk_usrgrp")
	private String pkUsrgrp;
	
	/**
	 * 关联人员
	 */
	@Field(value="pk_emp")
	private String pkEmp;
	
	/**
	 * 人员姓名
	 */
	@Field(value="name_emp")
	private String nameEmp;
	
	/**
	 * 认证类型
	 */
	@Field(value="eu_certtype")
	private String euCerttype;
	
	/**
	 * 密码
	 */
	@Field(value="pwd")
	private String pwd;
	
	/**
	 * 启用状态
	 */
	@Field(value="flag_active")
	private String flagActive;
	
	/**
	 * 是否锁定
	 */
	@Field(value="is_lock")
	private String isLock;
	
	/**
	 * CAID
	 */
	@Field(value="caid")
	private String caid;

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
	
	public String getPkUser() {
		return pkUser;
	}

	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	public String getCodeUser() {
		return codeUser;
	}

	public void setCodeUser(String codeUser) {
		this.codeUser = codeUser;
	}

	public String getNameUser() {
		return nameUser;
	}

	public void setNameUser(String nameUser) {
		this.nameUser = nameUser;
	}

	public String getPkUsrgrp() {
		return pkUsrgrp;
	}

	public void setPkUsrgrp(String pkUsrgrp) {
		this.pkUsrgrp = pkUsrgrp;
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

	public String getEuCerttype() {
		return euCerttype;
	}

	public void setEuCerttype(String euCerttype) {
		this.euCerttype = euCerttype;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getFlagActive() {
		return flagActive;
	}

	public void setFlagActive(String flagActive) {
		this.flagActive = flagActive;
	}

	public String getIsLock() {
		return isLock;
	}

	public void setIsLock(String isLock) {
		this.isLock = isLock;
	}

	public String getCaid() {
		return caid;
	}

	public void setCaid(String caid) {
		this.caid = caid;
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

	public String getEuUsertype() {
		return euUsertype;
	}

	public void setEuUsertype(String euUsertype) {
		this.euUsertype = euUsertype;
	}
	
	
}
