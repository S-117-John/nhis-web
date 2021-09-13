package com.zebone.nhis.sch.appt.vo;

import java.util.Date;

import com.zebone.nhis.common.module.sch.plan.SchSch;

public class DepartmentInformation extends SchSch{
	private String nameDept;
	private int pageNum;
    private int pageSize;
    private Date lastDateTime;
    private int apptDays;
	public String getNameDept() {
		return nameDept;
	}

	public void setNameDept(String nameDept) {
		this.nameDept = nameDept;
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

	public Date getLastDateTime() {
		return lastDateTime;
	}

	public void setLastDateTime(Date lastDateTime) {
		this.lastDateTime = lastDateTime;
	}

	public int getApptDays() {
		return apptDays;
	}

	public void setApptDays(int apptDays) {
		this.apptDays = apptDays;
	}


}
