package com.zebone.nhis.compay.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;

public class CostHpDictAttrVo {

	@Field(value = "PK_DICTATTR")
	private String pkdictattr;
	@Field(value = "PK_DICTATTRTEMP")
	private String pkdictattrtemp;
	@Field(value = "CODE_ATTR")
	private String codeattr;
	@Field(value = "NAME_ATTR")
	private String nameattr;
	@Field(value = "VAL_ATTR")
	private String valattr;
	@Field(value = "DESC_ATTR")
	private String descattr;
	public String getPkdictattr() {
		return pkdictattr;
	}
	public void setPkdictattr(String pkdictattr) {
		this.pkdictattr = pkdictattr;
	}
	public String getPkdictattrtemp() {
		return pkdictattrtemp;
	}
	public void setPkdictattrtemp(String pkdictattrtemp) {
		this.pkdictattrtemp = pkdictattrtemp;
	}
	public String getCodeattr() {
		return codeattr;
	}
	public void setCodeattr(String codeattr) {
		this.codeattr = codeattr;
	}
	public String getNameattr() {
		return nameattr;
	}
	public void setNameattr(String nameattr) {
		this.nameattr = nameattr;
	}
	public String getValattr() {
		return valattr;
	}
	public void setValattr(String valattr) {
		this.valattr = valattr;
	}
	public String getDescattr() {
		return descattr;
	}
	public void setDescattr(String descattr) {
		this.descattr = descattr;
	}

}
