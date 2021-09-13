package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArgu;
import com.zebone.nhis.common.module.base.bd.wf.BdWfOrdArguDept;

public class BdWfOrdArguParam {
	
	private BdWfOrdArgu bdWfOrdArgu;
	
	private List<BdWfOrdArguDept> bdWfOrdArguDept;

	public BdWfOrdArgu getBdWfOrdArgu() {
		return bdWfOrdArgu;
	}

	public void setBdWfOrdArgu(BdWfOrdArgu bdWfOrdArgu) {
		this.bdWfOrdArgu = bdWfOrdArgu;
	}

	public List<BdWfOrdArguDept> getBdWfOrdArguDept() {
		return bdWfOrdArguDept;
	}

	public void setBdWfOrdArguDept(List<BdWfOrdArguDept> bdWfOrdArguDept) {
		this.bdWfOrdArguDept = bdWfOrdArguDept;
	}
	
	

}
