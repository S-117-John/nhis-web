package com.zebone.nhis.base.bd.vo;

import java.util.ArrayList;
import java.util.List;

import com.zebone.nhis.common.module.base.bd.srv.BdAudit;
import com.zebone.nhis.common.module.base.bd.srv.BdAuditItemcate;

public class AuditAndItemCatesParam {

	/**
	 * 财务核算项目
	 */
	private BdAudit auditInfo = new BdAudit();
	
	/**
	 * 财务核算和收费项目对照
	 */
	private List<BdAuditItemcate> itemCateList = new ArrayList<BdAuditItemcate>();

	public BdAudit getAuditInfo() {
		return auditInfo;
	}

	public void setAuditInfo(BdAudit auditInfo) {
		this.auditInfo = auditInfo;
	}

	public List<BdAuditItemcate> getItemCateList() {
		return itemCateList;
	}

	public void setItemCateList(List<BdAuditItemcate> itemCateList) {
		this.itemCateList = itemCateList;
	}
	
}
