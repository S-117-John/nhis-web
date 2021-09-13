package com.zebone.nhis.cn.opdw.vo;

import com.zebone.nhis.common.module.ex.nis.ns.ExRisOcc;

public class ExRisOccVo extends ExRisOcc{
	private int pageNum;
    private int pageSize;
    private String nameOrd;
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
	public String getNameOrd() {
		return nameOrd;
	}
	public void setNameOrd(String nameOrd) {
		this.nameOrd = nameOrd;
	}
    
}
