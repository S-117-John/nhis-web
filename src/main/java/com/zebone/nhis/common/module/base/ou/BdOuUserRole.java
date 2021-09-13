package com.zebone.nhis.common.module.base.ou;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.KeyId;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

/**
 * bd_ou_menu表结构
 * @author Administrator
 *
 */
@Table(value="bd_ou_user_role")
public class BdOuUserRole extends BaseModule{

	/**
	 * 角色用户
	 * 主键
	 */
	@PK
	@Field(value="pk_userrole",id=KeyId.UUID)
	private String pkUserrole;
	
	/**
	 * 用户主键
	 */
	@Field(value="pk_user")
	private String pkUser;
	
	/**
	 * 角色主键
	 */
	@Field(value="pk_role")
	private String pkRole;

	public String getPkUserrole() {
		return pkUserrole;
	}

	public void setPkUserrole(String pkUserrole) {
		this.pkUserrole = pkUserrole;
	}

	public String getPkUser() {
		return pkUser;
	}

	public void setPkUser(String pkUser) {
		this.pkUser = pkUser;
	}

	public String getPkRole() {
		return pkRole;
	}

	public void setPkRole(String pkRole) {
		this.pkRole = pkRole;
	}
}
