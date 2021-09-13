package com.zebone.nhis.base.bd.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.zebone.nhis.common.module.base.bd.srv.BdOrdOrg;

public class BdOrdOrgExt extends BdOrdOrg {

	/**扩展字段 取自bd_ou_org表**/
	private String nameOrg;

	public String getNameOrg() {
		return nameOrg;
	}

	public void setNameOrg(String nameOrg) {
		this.nameOrg = nameOrg;
	}
}
