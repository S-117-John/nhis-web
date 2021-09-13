package com.zebone.nhis.scm.material.vo;

import com.zebone.nhis.common.module.scm.pub.BdPdAtt;

@SuppressWarnings("serial")
public class BdPdAttVo extends BdPdAtt {
	
	/*附加属性编码*/
	private String codeAtt;
	
	/*附加属性名称*/
	private String nameAtt;
	
	/*附加属性描述*/
	private String descVal;
	
	/*附加属性默认值*/
	private String valDef;
	
	/*附加属性物品类型*/
	private String dtPdtype;

	public String getCodeAtt() {
		return codeAtt;
	}

	public void setCodeAtt(String codeAtt) {
		this.codeAtt = codeAtt;
	}

	public String getNameAtt() {
		return nameAtt;
	}

	public void setNameAtt(String nameAtt) {
		this.nameAtt = nameAtt;
	}
	
	public String getDescVal() {
		return descVal;
	}

	public void setDescVal(String descVal) {
		this.descVal = descVal;
	}

	public String getValDef() {
		return valDef;
	}

	public void setValDef(String valDef) {
		this.valDef = valDef;
	}

	public String getDtPdtype() {
		return dtPdtype;
	}

	public void setDtPdtype(String dtPdtype) {
		this.dtPdtype = dtPdtype;
	}
}
