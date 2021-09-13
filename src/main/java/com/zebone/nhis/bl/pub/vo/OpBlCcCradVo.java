package com.zebone.nhis.bl.pub.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;

public class OpBlCcCradVo {
	private BlCc blCc;
	/** 制卡收费 */
	private List<BlCcPay> blCcPay;
	public BlCc getBlCc() {
		return blCc;
	}
	public void setBlCc(BlCc blCc) {
		this.blCc = blCc;
	}
	public List<BlCcPay> getBlCcPay() {
		return blCcPay;
	}
	public void setBlCcPay(List<BlCcPay> blCcPay) {
		this.blCcPay = blCcPay;
	}
	
		
	
}
