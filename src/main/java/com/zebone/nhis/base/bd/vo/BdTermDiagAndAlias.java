package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.mk.BdTermDiag;
import com.zebone.nhis.common.module.base.bd.mk.BdTermDiagAlias;

public class BdTermDiagAndAlias extends BdTermDiag{

	public BdTermDiag diag;
	
	public List<BdTermDiagAlias> alias;

	
	public BdTermDiag getDiag() {
		return diag;
	}

	public void setDiag(BdTermDiag diag) {
		this.diag = diag;
	}

	public List<BdTermDiagAlias> getAlias() {
		return alias;
	}

	public void setAlias(List<BdTermDiagAlias> alias) {
		this.alias = alias;
	}
	
}
