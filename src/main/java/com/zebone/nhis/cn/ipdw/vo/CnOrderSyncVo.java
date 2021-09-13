package com.zebone.nhis.cn.ipdw.vo;

import com.zebone.nhis.common.module.cn.ipdw.CnOrderSync;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.Field.FieldType;



public class CnOrderSyncVo extends CnOrderSync {
	private String pkItem;
	private String itemCode;
	private String itemName;
	
	public String getPkItem() {
		return pkItem;
	}
	public void setPkItem(String pkItem) {
		this.pkItem = pkItem;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
}
