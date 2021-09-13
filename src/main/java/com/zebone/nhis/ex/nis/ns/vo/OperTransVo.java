package com.zebone.nhis.ex.nis.ns.vo;

import java.util.List;

import com.zebone.nhis.common.module.ex.nis.ns.PvOperTrans;
import com.zebone.nhis.common.module.ex.nis.ns.PvOperTransItem;

public class OperTransVo extends PvOperTrans {

	private List<PvOperTransItem> ListTransItem;

	public List<PvOperTransItem> getListTransItem() {
		return ListTransItem;
	}

	public void setListTransItem(List<PvOperTransItem> listTransItem) {
		ListTransItem = listTransItem;
	}
}
