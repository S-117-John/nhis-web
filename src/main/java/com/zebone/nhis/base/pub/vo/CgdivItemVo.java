package com.zebone.nhis.base.pub.vo;

import com.zebone.nhis.common.module.base.bd.price.BdHpCgdivItem;
import com.zebone.nhis.common.module.base.bd.srv.BdItemcate;

public class CgdivItemVo extends BdHpCgdivItem{
	
	public BdItemcate itemCate;
	
	public String name;
	public String codeDiv;
	
	public String nameDiv;
	
	


	public BdItemcate getItemCate() {
		return itemCate;
	}
	public void setItemCate(BdItemcate itemCate) {
		this.itemCate = itemCate;
	}
	public String getCodeDiv() {
		return codeDiv;
	}
	public void setCodeDiv(String codeDiv) {
		this.codeDiv = codeDiv;
	}
	public String getNameDiv() {
		return nameDiv;
	}
	public void setNameDiv(String nameDiv) {
		this.nameDiv = nameDiv;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
