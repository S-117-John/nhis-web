package com.zebone.nhis.scm.material.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.st.PdInventory;

@SuppressWarnings("serial")
public class MtlInventoryVo extends PdInventory {
	private String invtype;// 盘点类型

	private List<MtlInventoryDtVo> dtlist;//盘点明细
	
	private List<String> delDtList;//删除的明细主键

	public List<String> getDelDtList() {
		return delDtList;
	}

	public void setDelDtList(List<String> delDtList) {
		this.delDtList = delDtList;
	}

	public List<MtlInventoryDtVo> getDtlist() {
		return dtlist;
	}

	public void setDtlist(List<MtlInventoryDtVo> dtlist) {
		this.dtlist = dtlist;
	}

	public String getInvtype() {
		return invtype;
	}

	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}

}
