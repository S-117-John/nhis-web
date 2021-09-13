package com.zebone.nhis.scm.material.vo;

import com.zebone.nhis.common.module.scm.pub.BdPd;

@SuppressWarnings("serial")
public class BdPdBaseVo extends BdPd {
	
	/*计量单位*/
	private String nameUnitMin;
	
	/*物品类型*/
	private String namePdtype;
	
	/*生产厂家*/
	private String nameFactory;
	
	/*来源分类*/
	private String nameAbrd;
	
	/*存储要求*/
	private String nameStoretype;
	
	/*物品分类*/
	private String namePdcate;
	
	/*对应的收费项目*/
	private String nameItem;

	public String getNameUnitMin() {
		return nameUnitMin;
	}

	public void setNameUnitMin(String nameUnitMin) {
		this.nameUnitMin = nameUnitMin;
	}

	public String getNamePdtype() {
		return namePdtype;
	}

	public void setNamePdtype(String namePdtype) {
		this.namePdtype = namePdtype;
	}

	public String getNameFactory() {
		return nameFactory;
	}

	public void setNameFactory(String nameFactory) {
		this.nameFactory = nameFactory;
	}

	public String getNameAbrd() {
		return nameAbrd;
	}

	public void setNameAbrd(String nameAbrd) {
		this.nameAbrd = nameAbrd;
	}

	public String getNameStoretype() {
		return nameStoretype;
	}

	public void setNameStoretype(String nameStoretype) {
		this.nameStoretype = nameStoretype;
	}

	public String getNamePdcate() {
		return namePdcate;
	}

	public void setNamePdcate(String namePdcate) {
		this.namePdcate = namePdcate;
	}

	public String getNameItem() {
		return nameItem;
	}

	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}
	
}
