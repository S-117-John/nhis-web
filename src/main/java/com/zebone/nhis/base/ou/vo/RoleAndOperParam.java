package com.zebone.nhis.base.ou.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuOper;
import com.zebone.nhis.common.module.base.ou.BdOuRole;
import com.zebone.nhis.common.module.base.ou.BdOuRoleOper;

public class RoleAndOperParam {

	/**
	 * 角色
	 */
	private BdOuRole bdOuRole = new BdOuRole();
	
	/**
	 * 功能列表
	 */
	private List<BdOuRoleOper> roleOpers = new ArrayList<BdOuRoleOper>();

	public BdOuRole getBdOuRole() {
		return bdOuRole;
	}

	public void setBdOuRole(BdOuRole bdOuRole) {
		this.bdOuRole = bdOuRole;
	}

	public List<BdOuRoleOper> getRoleOpers() {
		return roleOpers;
	}

	public void setRoleOpers(List<BdOuRoleOper> roleOpers) {
		this.roleOpers = roleOpers;
	}
}
