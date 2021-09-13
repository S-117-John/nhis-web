package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.pi.PatInfo;
import org.apache.commons.collections.MapUtils;

public class SyxOpOrderVo {

	private List<SyxCnOrderVo> addingList;
	private List<SyxCnOrderVo> deletingList;
	private List<SyxCnOrderVo> editingList;
	private PatInfo patInfo;
	private String flagEx;

	public List<SyxCnOrderVo> getAddingList() {
		return addingList;
	}
	public void setAddingList(List<SyxCnOrderVo> addingList) {
		this.addingList = addingList;
	}
	public List<SyxCnOrderVo> getDeletingList() {
		return deletingList;
	}
	public void setDeletingList(List<SyxCnOrderVo> deletingList) {
		this.deletingList = deletingList;
	}
	public List<SyxCnOrderVo> getEditingList() {
		return editingList;
	}
	public void setEditingList(List<SyxCnOrderVo> editingList) {
		this.editingList = editingList;
	}
	public PatInfo getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(PatInfo patInfo) {
		this.patInfo = patInfo;
	}

	public String getFlagEx() {
		return flagEx;
	}

	public void setFlagEx(String flagEx) {
		this.flagEx = flagEx;
	}
}
