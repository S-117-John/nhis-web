package com.zebone.nhis.common.module.base.ou;

import java.util.Date;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;

/**
 * 角色功能授权表
 * @author Xulj
 *
 */
@Table(value="bd_ou_role_oper")
public class BdOuRoleOper{

	/**
	 * 角色功能主键
	 */
	@PK
	@Field(value="pk_roleoper",id=KeyId.UUID)
	private String pkRoleoper;
	
	/**
	 * 角色主键
	 */
	@Field(value="pk_role")
	private String pkRole;
	
	/**
	 * 功能主键
	 */
	@Field(value="pk_oper")
	private String pkOper;

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
	
	@Field(value="PK_MENU")
    private String pkMenu;
	
	public String getPkRoleoper() {
		return pkRoleoper;
	}

	public void setPkRoleoper(String pkRoleoper) {
		this.pkRoleoper = pkRoleoper;
	}

	public String getPkRole() {
		return pkRole;
	}

	public void setPkRole(String pkRole) {
		this.pkRole = pkRole;
	}

	public String getPkOper() {
		return pkOper;
	}

	public void setPkOper(String pkOper) {
		this.pkOper = pkOper;
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

	public String getPkMenu() {
		return pkMenu;
	}

	public void setPkMenu(String pkMenu) {
		this.pkMenu = pkMenu;
	}
	
}
