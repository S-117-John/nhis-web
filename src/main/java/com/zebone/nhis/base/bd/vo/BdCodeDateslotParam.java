package com.zebone.nhis.base.bd.vo;

import java.util.List;

import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslot;
import com.zebone.nhis.common.module.base.bd.code.BdCodeDateslotTime;

public class BdCodeDateslotParam {
	
	private BdCodeDateslot bdCodeDateslot;
	
	private List<BdCodeDateslotSecVO> bdCodeDateslotSec;
	
	private List<BdCodeDateslotTime> bdCodeDateslotTime;

	public BdCodeDateslot getBdCodeDateslot() {
		return bdCodeDateslot;
	}

	public void setBdCodeDateslot(BdCodeDateslot bdCodeDateslot) {
		this.bdCodeDateslot = bdCodeDateslot;
	}

	public List<BdCodeDateslotSecVO> getBdCodeDateslotSec() {
		return bdCodeDateslotSec;
	}

	public void setBdCodeDateslotSec(List<BdCodeDateslotSecVO> bdCodeDateslotSec) {
		this.bdCodeDateslotSec = bdCodeDateslotSec;
	}

	public List<BdCodeDateslotTime> getBdCodeDateslotTime() {
		return bdCodeDateslotTime;
	}

	public void setBdCodeDateslotTime(List<BdCodeDateslotTime> bdCodeDateslotTime) {
		this.bdCodeDateslotTime = bdCodeDateslotTime;
	}
	
	

}
