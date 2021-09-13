package com.zebone.nhis.base.bd.vo;

import com.zebone.nhis.common.module.base.bd.price.BdChap;

public class BdChapAndPkItemVo extends BdChap{
	private String code;
	private String name;
	private String spec;
	private String PkItem;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getPkItem() {
		return PkItem;
	}
	public void setPkItem(String pkItem) {
		PkItem = pkItem;
	}
}
