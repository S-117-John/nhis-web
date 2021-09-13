package com.zebone.nhis.compay.ins.lb.service.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebJs;
import com.zebone.nhis.common.module.pi.PiMaster;

public class PageInsSuzhounhWebJsListVo {
	
	private List<InsSuzhounhWebJs> insSuzhounhWebJs;
	
	private int totalCount;
	
	public List<InsSuzhounhWebJs> getInsSuzhounhWebJs() {
		return insSuzhounhWebJs;
	}

	public void setInsSuzhounhWebJs(List<InsSuzhounhWebJs> insSuzhounhWebJs) {
		this.insSuzhounhWebJs = insSuzhounhWebJs;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
