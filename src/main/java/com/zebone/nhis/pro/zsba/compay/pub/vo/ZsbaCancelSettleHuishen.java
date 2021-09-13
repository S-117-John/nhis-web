package com.zebone.nhis.pro.zsba.compay.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlDeposit;
import com.zebone.nhis.common.module.bl.BlSettle;

public class ZsbaCancelSettleHuishen {

	private ZsbaBlSettle blSettle;
	private List<ZsbaBlDeposit > blDepositList;
	
	
	public ZsbaBlSettle getBlSettle() {
		return blSettle;
	}
	public void setBlSettle(ZsbaBlSettle blSettle) {
		this.blSettle = blSettle;
	}
	public List<ZsbaBlDeposit> getBlDepositList() {
		return blDepositList;
	}
	public void setBlDepositList(List<ZsbaBlDeposit> blDepositList) {
		this.blDepositList = blDepositList;
	}
	
	
	
}
