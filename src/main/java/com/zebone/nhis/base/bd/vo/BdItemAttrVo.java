package com.zebone.nhis.base.bd.vo;

public class BdItemAttrVo {
	
	//自定义属性主键
	private String pkDictattr;
	
	// 模板属性主键
	private String pkDictattrtemp;
	
	// 属性编码
	private String codeAttr;
	
	// 属性名称
	private String nameAttr;
	
	// 属性值
	private String valAttr;
	
	// 值域说明
	private String descAttr;
	
	// 使用机构
	private String pkOrgUse;
	
	// 使用机构名称
	private String orgUseName;

	public String getPkDictattr() {
		return pkDictattr;
	}

	public void setPkDictattr(String pkDictattr) {
		this.pkDictattr = pkDictattr;
	}

	public String getPkDictattrtemp() {
		return pkDictattrtemp;
	}

	public void setPkDictattrtemp(String pkDictattrtemp) {
		this.pkDictattrtemp = pkDictattrtemp;
	}

	public String getCodeAttr() {
		return codeAttr;
	}

	public void setCodeAttr(String codeAttr) {
		this.codeAttr = codeAttr;
	}

	public String getNameAttr() {
		return nameAttr;
	}

	public void setNameAttr(String nameAttr) {
		this.nameAttr = nameAttr;
	}

	public String getValAttr() {
		return valAttr;
	}

	public void setValAttr(String valAttr) {
		this.valAttr = valAttr;
	}

	public String getDescAttr() {
		return descAttr;
	}

	public void setDescAttr(String descAttr) {
		this.descAttr = descAttr;
	}

	public String getPkOrgUse() {
		return pkOrgUse;
	}

	public void setPkOrgUse(String pkOrgUse) {
		this.pkOrgUse = pkOrgUse;
	}

	public String getOrgUseName() {
		return orgUseName;
	}

	public void setOrgUseName(String orgUseName) {
		this.orgUseName = orgUseName;
	}
	
	
	
}
