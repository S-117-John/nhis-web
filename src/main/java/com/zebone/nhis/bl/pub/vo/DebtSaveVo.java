package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;

public class DebtSaveVo {
	private List<InvInfoVo> invList;
	
	private BlDeposit depo;

	public List<InvInfoVo> getInvList() {
		return invList;
	}

	public void setInvList(List<InvInfoVo> invList) {
		this.invList = invList;
	}

	public BlDeposit getDepo() {
		return depo;
	}

	public void setDepo(BlDeposit depo) {
		this.depo = depo;
	}
	
	
}
