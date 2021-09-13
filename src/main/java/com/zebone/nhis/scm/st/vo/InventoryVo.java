package com.zebone.nhis.scm.st.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdInventory;

public class InventoryVo extends PdInventory {
	private String invtype;// 盘点类型

	private List<InventoryDtVo> dtlist;//盘点明细
	
	private List<String> delDtList;//删除的明细主键

	public List<String> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<String> delDtList) {
		this.delDtList = delDtList;
	}

	public List<InventoryDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<InventoryDtVo> dtlist) {
		this.dtlist = dtlist;
	}

	public String getInvtype() {
		return invtype;
	}

	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}

}
