package com.zebone.nhis.cn.opdw.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.cn.ipdw.BdOrdSet;
import com.zebone.nhis.common.module.cn.ipdw.BdOrdSetDt;

public class SyxOpTemplatesVo {

	private List<BdOrdSet> addingList = new ArrayList<BdOrdSet>();
	private List<BdOrdSet> editingList = new ArrayList<BdOrdSet>();
	private List<BdOrdSet> removingList = new ArrayList<BdOrdSet>();
	private List<BdOrdSetDt> bdOrderSetDt = new ArrayList<BdOrdSetDt>();
	private List<BdOrdSetDt> deleteSetDtList = new ArrayList<BdOrdSetDt>();
	public String checkName;

	public List<BdOrdSet> getAddingList() {
		return addingList;
	}
	public void setAddingList(List<BdOrdSet> addingList) {
		this.addingList = addingList;
	}
	public List<BdOrdSet> getEditingList() {
		return editingList;
	}
	public void setEditingList(List<BdOrdSet> editingList) {
		this.editingList = editingList;
	}
	public List<BdOrdSet> getRemovingList() {
		return removingList;
	}
	public void setRemovingList(List<BdOrdSet> removingList) {
		this.removingList = removingList;
	}
	public List<BdOrdSetDt> getDeleteSetDtList() {
		return deleteSetDtList;
	}
	public void setDeleteSetDtList(List<BdOrdSetDt> deleteSetDtList) {
		this.deleteSetDtList = deleteSetDtList;
	}
	public List<BdOrdSetDt> getBdOrderSetDt() {
		return bdOrderSetDt;
	}
	public void setBdOrderSetDt(List<BdOrdSetDt> bdOrderSetDt) {
		this.bdOrderSetDt = bdOrderSetDt;
	}

	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
}
