package com.zebone.nhis.ma.lb.vo;

import com.zebone.nhis.common.module.bl.BlEmpInvoice;

public class InvLbVo extends BlEmpInvoice {

	private static final long serialVersionUID = -8810762276252811602L;

	private String euType;
	
	private String nameInvcate;
	
	private String modifierName;
	
	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}

	public String getEuType() {
		return euType;
	}

	public void setEuType(String euType) {
		this.euType = euType;
	}

	public String getNameInvcate() {
		return nameInvcate;
	}

	public void setNameInvcate(String nameInvcate) {
		this.nameInvcate = nameInvcate;
	}

	
	
	
}
