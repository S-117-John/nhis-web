package com.zebone.nhis.scm.purchase.vo;

import java.util.List;

public class PlanUpRetvo {
    /// 订单计划编号
    private String ddjhbh ;

    /// 记录数
    private String jls;

    /// 明细
    private List<DrugAdminPlanDtRet> mx;

	public String getDdjhbh() {
		return ddjhbh;
	}

	public void setDdjhbh(String ddjhbh) {
		this.ddjhbh = ddjhbh;
	}

	public String getJls() {
		return jls;
	}

	public void setJls(String jls) {
		this.jls = jls;
	}

	public List<DrugAdminPlanDtRet> getMx() {
		return mx;
	}

	public void setMx(List<DrugAdminPlanDtRet> mx) {
		this.mx = mx;
	}
    
    
}
