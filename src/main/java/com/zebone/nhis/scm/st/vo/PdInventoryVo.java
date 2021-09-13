package com.zebone.nhis.scm.st.vo;

import com.zebone.nhis.common.module.scm.st.PdInvDetail;
import com.zebone.nhis.common.module.scm.st.PdInventory;

/**
 * 盘点新增页面所需页面
 * @author Administrator
 *
 */
public class PdInventoryVo extends PdInventory{
	private PdInvDetail invPdvo;

	public PdInvDetail getInvPdvo() {
		return invPdvo;
	}

	public void setInvPdvo(PdInvDetail invPdvo) {
		this.invPdvo = invPdvo;
	}
	
}
