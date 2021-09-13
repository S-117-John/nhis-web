package com.zebone.nhis.sch.hd.vo;

import java.util.*;
public class CopySchVo {
	private List<SchedulingByBed> schList;
	private Date start;
	private Date end;
	private String ifCover;
	private String pkDeptNs;
	public List<SchedulingByBed> getSchList() {
		return schList;
	}
	public void setSchList(List<SchedulingByBed> schList) {
		this.schList = schList;
	}
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public String getIfCover() {
		return ifCover;
	}
	public void setIfCover(String ifCover) {
		this.ifCover = ifCover;
	}
	public String getPkDeptNs() {
		return pkDeptNs;
	}
	public void setPkDeptNs(String pkDeptNs) {
		this.pkDeptNs = pkDeptNs;
	}
	
	
}
