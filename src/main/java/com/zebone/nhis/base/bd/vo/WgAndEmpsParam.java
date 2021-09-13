package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.res.OrgDeptWg;
import com.zebone.nhis.common.module.base.bd.res.OrgDeptWgEmp;

public class WgAndEmpsParam {

	/**
	 * 医疗组
	 */
	private OrgDeptWg wg = new OrgDeptWg();
	
	/**
	 * 医疗组人员
	 */
	private List<OrgDeptWgEmp> wgemps = new ArrayList<OrgDeptWgEmp>();
	
	//是否审核页面 0：不是  1:是
	private String  isParameterApprove;

	public OrgDeptWg getWg() {
		return wg;
	}

	public void setWg(OrgDeptWg wg) {
		this.wg = wg;
	}

	public List<OrgDeptWgEmp> getWgemps() {
		return wgemps;
	}

	public void setWgemps(List<OrgDeptWgEmp> wgemps) {
		this.wgemps = wgemps;
	}
	
	public String getIsParameterApprove() {
		return isParameterApprove;
	}

	public void setIsParameterApprove(String isParameterApprove) {
		this.isParameterApprove = isParameterApprove;
	}
}
