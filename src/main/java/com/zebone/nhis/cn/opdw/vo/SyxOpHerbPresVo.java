package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

public class SyxOpHerbPresVo {

	/**
	 * 草药代煎费用逻辑，需要匹配医院。0：默认逻辑；1：代表人民医院
	 */
	private String djf;
	private List<SyxCnOpPresVo> addingList;
	private List<SyxCnOpPresVo> deletingList;
	private List<SyxCnOpPresVo> editingList;
	public List<SyxCnOpPresVo> getAddingList() {
		return addingList;
	}
	public void setAddingList(List<SyxCnOpPresVo> addingList) {
		this.addingList = addingList;
	}
	public List<SyxCnOpPresVo> getDeletingList() {
		return deletingList;
	}
	public void setDeletingList(List<SyxCnOpPresVo> deletingList) {
		this.deletingList = deletingList;
	}
	public List<SyxCnOpPresVo> getEditingList() {
		return editingList;
	}
	public void setEditingList(List<SyxCnOpPresVo> editingList) {
		this.editingList = editingList;
	}

	public String getDjf() {
		return djf;
	}

	public void setDjf(String djf) {
		this.djf = djf;
	}
}
