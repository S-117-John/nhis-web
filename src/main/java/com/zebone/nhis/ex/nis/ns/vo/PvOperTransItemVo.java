package com.zebone.nhis.ex.nis.ns.vo;

import com.zebone.nhis.common.module.ex.nis.ns.PvOperTransItem;

public class PvOperTransItemVo extends PvOperTransItem {
	
	private String shortname;
	
	private String nameItem;

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}
}
