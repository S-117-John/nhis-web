package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdOrg;

public class BdOrdOrgVo extends BdOrdOrg {

	/**
	 * 1 -- 新勾选, 0 -- 原来已勾选, -1 -- 去掉了
	 */
	private String flag;

	/**
	 * 1 -- 新勾选, 0 -- 原来已勾选, -1 -- 去掉了
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * 1 -- 新勾选, 0 -- 原来已勾选, -1 -- 去掉了
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
}
