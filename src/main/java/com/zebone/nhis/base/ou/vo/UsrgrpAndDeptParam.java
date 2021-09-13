package com.zebone.nhis.base.ou.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.ou.BdOuUsrgrp;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpDept;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpPerm;
import com.zebone.nhis.common.module.base.ou.BdOuUsrgrpRole;

public class UsrgrpAndDeptParam {

	/**
	 * 用户组
	 */
	private BdOuUsrgrp usrgrp = new BdOuUsrgrp();
	
	/**
	 * 和科室的关系
	 */
	List<BdOuUsrgrpDept> deptrelations = new ArrayList<BdOuUsrgrpDept>();

	/**
	 * 和角色的关系
	 */
	List<BdOuUsrgrpRole> rolerelations = new ArrayList<BdOuUsrgrpRole>();

	/**
	 * 和报表的关系
	 */
	List<BdOuUsrgrpPerm> bdreportrelations = new ArrayList<BdOuUsrgrpPerm>();


	public BdOuUsrgrp getUsrgrp() {
		return usrgrp;
	}

	public void setUsrgrp(BdOuUsrgrp usrgrp) {
		this.usrgrp = usrgrp;
	}

	public List<BdOuUsrgrpDept> getDeptrelations() {
		return deptrelations;
	}

	public void setDeptrelations(List<BdOuUsrgrpDept> deptrelations) {
		this.deptrelations = deptrelations;
	}

	public List<BdOuUsrgrpRole> getRolerelations() {
		return rolerelations;
	}

	public void setRolerelations(List<BdOuUsrgrpRole> rolerelations) {
		this.rolerelations = rolerelations;
	}

	public List<BdOuUsrgrpPerm> getBdreportrelations() {
		return bdreportrelations;
	}
	public void setBdreportrelations(List<BdOuUsrgrpPerm> bdreportrelations) {
		this.bdreportrelations = bdreportrelations;
	}

}
