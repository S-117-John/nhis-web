package com.zebone.nhis.pi.fix.vo;

import java.util.Date;

import com.zebone.nhis.common.module.pi.PiMaster;

public class PageQryPiParam extends PiMaster{
	
	private int pageSize;
	
	private int pageIndex;

	/**0：禁用函数查询Age （null）1: 启用*/
	private String ageFormatFlag;

	//出生日期范围-开始日期
	private Date birthBegin;
	
	//出生日期范围-结束日期
	private Date birthEnd;
	

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

	public String getAgeFormatFlag() {
		return ageFormatFlag;
	}

	public void setAgeFormatFlag(String ageFormatFlag) {
		this.ageFormatFlag = ageFormatFlag;
	}
	
	public Date getBirthBegin() {
		return birthBegin;
	}

	public void setBirthBegin(Date birthBegin) {
		this.birthBegin = birthBegin;
	}

	public Date getBirthEnd() {
		return birthEnd;
	}

	public void setBirthEnd(Date birthEnd) {
		this.birthEnd = birthEnd;
	}
	
}
