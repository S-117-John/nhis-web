package com.zebone.nhis.base.ou.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuRole;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpRole;

public class RoleAndUsrgrpParam {

	/**
	 * 角色
	 */
	private BdOuRole bdOuRole = new BdOuRole();
	
	/**
	 * 用户组列表
	 */
	private List<BdOuUsrgrpRole> usrgrpRoles = new ArrayList<BdOuUsrgrpRole>();
	
	/**
	 * 用户组列表(取消)
	 */
	private List<BdOuUsrgrpRole> usrgrpRolesCancel = new ArrayList<BdOuUsrgrpRole>();

	public BdOuRole getBdOuRole() {
		return bdOuRole;
	}

	public void setBdOuRole(BdOuRole bdOuRole) {
		this.bdOuRole = bdOuRole;
	}

	public List<BdOuUsrgrpRole> getUsrgrpRoles() {
		return usrgrpRoles;
	}

	public void setUsrgrpRoles(List<BdOuUsrgrpRole> usrgrpRoles) {
		this.usrgrpRoles = usrgrpRoles;
	}

	public List<BdOuUsrgrpRole> getUsrgrpRolesCancel() {
		return usrgrpRolesCancel;
	}

	public void setUsrgrpRolesCancel(List<BdOuUsrgrpRole> usrgrpRolesCancel) {
		this.usrgrpRolesCancel = usrgrpRolesCancel;
	}
	
}
