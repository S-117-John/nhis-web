package com.zebone.nhis.cn.opdw.vo;

import java.util.List;

import com.zebone.nhis.common.module.pi.PatInfo;

public class SyxOpReqOrderVo {

	private PatInfo patInfo;
	private List<SyxCnOrderVo> addingList;
	private List<SyxCnOrderVo> deletingList;
	private List<SyxCnOrderVo> editingList;
	private List<SyxCnOrderVo> modifyList;
	private String flagQuan;//是否重新给quan字段赋值
	private String flagEX;//是否生成执行表
	private String flagBl;//是否有药品收费；
	
	
	public PatInfo getPatInfo() {
		return patInfo;
	}
	public void setPatInfo(PatInfo patInfo) {
		this.patInfo = patInfo;
	}
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
	public List<SyxCnOrderVo> getModifyList() {
		return modifyList;
	}
	public void setModifyList(List<SyxCnOrderVo> modifyList) {
		this.modifyList = modifyList;
	}

	public String getFlagQuan() {
		return flagQuan;
	}

	public void setFlagQuan(String flagQuan) {
		this.flagQuan = flagQuan;
	}

	public String getFlagEX() {
		return flagEX;
	}

	public void setFlagEX(String flagEX) {
		this.flagEX = flagEX;
	}

	public String getFlagBl() {
		return flagBl;
	}

	public void setFlagBl(String flagBl) {
		this.flagBl = flagBl;
	}
}
