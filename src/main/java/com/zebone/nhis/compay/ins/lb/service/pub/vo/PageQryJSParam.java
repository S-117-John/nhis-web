package com.zebone.nhis.compay.ins.lb.service.pub.vo;

import com.zebone.nhis.common.module.compay.ins.lb.nhyb.InsSuzhounhWebJs;

public class PageQryJSParam extends InsSuzhounhWebJs{
	
	private int pageSize;
	
	private int pageIndex;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
}
