package com.zebone.nhis.cn.ipdw.vo;

import java.util.List;

public class SchIpVO {

	private String pkSchip;
	
	private String monthSch;
	
	private List<SchIpDtVO> list;

	public String getMonthSch() {
		return monthSch;
	}

	public void setMonthSch(String monthSch) {
		this.monthSch = monthSch;
	}

	public String getPkSchip() {
		return pkSchip;
	}

	public void setPkSchip(String pkSchip) {
		this.pkSchip = pkSchip;
	}

	public List<SchIpDtVO> getList() {
		return list;
	}

	public void setList(List<SchIpDtVO> list) {
		this.list = list;
	}
}
