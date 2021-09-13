package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.wf.BdWf;
import com.zebone.nhis.common.module.base.bd.wf.BdWfcate;

public class BdWfcateParam {
	
	private BdWfcate bdWfcate;
	
	private List<BdWf> bdWf;

	public BdWfcate getBdWfcate() {
		return bdWfcate;
	}

	public void setBdWfcate(BdWfcate bdWfcate) {
		this.bdWfcate = bdWfcate;
	}

	public List<BdWf> getBdWf() {
		return bdWf;
	}

	public void setBdWf(List<BdWf> bdWf) {
		this.bdWf = bdWf;
	}
	
	

}
