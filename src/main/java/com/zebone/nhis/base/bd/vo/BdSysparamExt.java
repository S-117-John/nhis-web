package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.code.BdSysparam;

public class BdSysparamExt extends BdSysparam {
	
    /**扩展字段 取自bd_res_pc_argu表**/
	private String pkPcargu;
    
	public String getPkPcargu() {
		return pkPcargu;
	}
	public void setPkPcargu(String pkPcargu) {
		this.pkPcargu = pkPcargu;
	}
	
}
