package com.zebone.nhis.sch.appt.vo;

import com.zebone.nhis.common.module.base.bd.srv.BdItem;

public class InquireChargesVo extends BdItem{
    private String unitname;
    private int pageNum;
    private int pageSize;
    

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

}
