package com.zebone.nhis.base.ou.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuUser;
import com.zebone.nhis.common.module.base.ou.BdOuUserAdd;
import com.zebone.nhis.common.module.base.ou.BdOuUserRole;
import com.zebone.nhis.common.module.base.ou.BdOuUserUsrgrp;

public class UserAndUserUsrgrpParam {

	/**
	 * 用户
	 */
	//private BdOuUser user = new BdOuUser();
	private BdOuUserAdd user = new BdOuUserAdd();
	
	/**
	 * 共享用户组
	 */
	private List<BdOuUserUsrgrp> userUsrgrpList = new ArrayList<BdOuUserUsrgrp>();

	/*
	 * 用户权限
	 */
	private List<BdOuUserRole> userRoleList =new ArrayList<BdOuUserRole>();
	


	public List<BdOuUserUsrgrp> getUserUsrgrpList() {
		return userUsrgrpList;
	}

	public void setUserUsrgrpList(List<BdOuUserUsrgrp> userUsrgrpList) {
		this.userUsrgrpList = userUsrgrpList;
	}

	public List<BdOuUserRole> getUserRoleList() {
		return userRoleList;
	}

	public void setUserRoleList(List<BdOuUserRole> userRoleList) {
		this.userRoleList = userRoleList;
	}


	public BdOuUserAdd getUser() {
		return user;
	}

	public void setUser(BdOuUserAdd user) {
		this.user = user;
	}
}
