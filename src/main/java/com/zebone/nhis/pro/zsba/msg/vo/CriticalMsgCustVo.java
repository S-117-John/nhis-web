package com.zebone.nhis.pro.zsba.msg.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.support.CvMsg;
import com.zebone.nhis.common.module.base.support.CvMsgCust;

public class CriticalMsgCustVo {
	private CvMsg cm;
	private List<CvMsgCust> cvList;
	public CvMsg getCm() {
		return cm;
	}
	public void setCm(CvMsg cm) {
		this.cm = cm;
	}
	public List<CvMsgCust> getCvList() {
		return cvList;
	}
	public void setCmList(List<CvMsgCust> cvList) {
		this.cvList = cvList;
	} 
}
