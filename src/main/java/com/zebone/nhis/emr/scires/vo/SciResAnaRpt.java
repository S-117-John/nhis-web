package com.zebone.nhis.emr.scires.vo;

import java.util.List;

/**
 * 科研分析查询报表vo
 * @author chengjia
 *
 */
public class SciResAnaRpt {
	
	private int retCode;
	private String retMsg;
	
	private List<RptRsltRec> recList;
	
	public int getRetCode() {
		return retCode;
	}
	public void setRetCode(int retCode) {
		this.retCode = retCode;
	}
	public String getRetMsg() {
		return retMsg;
	}
	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}
	public List<RptRsltRec> getRecList() {
		return recList;
	}
	public void setRecList(List<RptRsltRec> recList) {
		this.recList = recList;
	}
	
}
