package com.zebone.nhis.emr.scires.vo;

import java.util.Date;

/**
 * 科研分析查询参数vo
 * @author chengjia
 *
 */
public class SciResAnaQry {
	
	private String pkRpt;
	private Date beginDateB;
	private Date endDateB;
	private String euStatus;
	private String euStatusPvNot;
	
	
	public String getPkRpt() {
		return pkRpt;
	}
	public void setPkRpt(String pkRpt) {
		this.pkRpt = pkRpt;
	}
	public Date getBeginDateB() {
		return beginDateB;
	}
	public void setBeginDateB(Date beginDateB) {
		this.beginDateB = beginDateB;
	}
	public Date getEndDateB() {
		return endDateB;
	}
	public void setEndDateB(Date endDateB) {
		this.endDateB = endDateB;
	}
	public String getEuStatus() {
		return euStatus;
	}
	public void setEuStatus(String euStatus) {
		this.euStatus = euStatus;
	}
	public String getEuStatusPvNot() {
		return euStatusPvNot;
	}
	public void setEuStatusPvNot(String euStatusPvNot) {
		this.euStatusPvNot = euStatusPvNot;
	}


}
