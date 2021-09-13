package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.scm.pub.BdIndtype;
import com.zebone.nhis.common.module.scm.pub.BdPdIndhp;

public class PdIndtypeVo {
	private List<BdPdIndhp> hps;
	
	private BdIndtype pdIndtype;

	public List<BdPdIndhp> getHps() {
		return hps;
	}

	public void setHps(List<BdPdIndhp> hps) {
		this.hps = hps;
	}

	public BdIndtype getPdIndtype() {
		return pdIndtype;
	}

	public void setPdIndtype(BdIndtype pdIndtype) {
		this.pdIndtype = pdIndtype;
	}
	
}
