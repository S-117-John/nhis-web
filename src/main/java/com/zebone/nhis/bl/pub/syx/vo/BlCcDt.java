package com.zebone.nhis.bl.pub.syx.vo;

import java.util.List;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcInv;
import com.zebone.nhis.common.module.bl.BlCcPay;

public class BlCcDt {
	
	private BlCc blCc;
	
	private List<BlCcPay> blCcPayList;
	
	private String bdInvStr;//补打发票信息
	
	private String stJzStr;//记账收据数量信息
	
	private List<StInvInfo> invInfoList;//发票号段
	
	private List<InvalidStInv> InvalidList;//作废号段
	
	private List<BlCcInv> blCcInvList;
	
	public List<BlCcInv> getBlCcInvList() {
		return blCcInvList;
	}

	public void setBlCcInvList(List<BlCcInv> blCcInvList) {
		this.blCcInvList = blCcInvList;
	}

	public List<StInvInfo> getInvInfoList() {
		return invInfoList;
	}

	public void setInvInfoList(List<StInvInfo> invInfoList) {
		this.invInfoList = invInfoList;
	}

	public List<InvalidStInv> getInvalidList() {
		return InvalidList;
	}

	public void setInvalidList(List<InvalidStInv> invalidList) {
		InvalidList = invalidList;
	}

	public String getStJzStr() {
		return stJzStr;
	}

	public void setStJzStr(String stJzStr) {
		this.stJzStr = stJzStr;
	}

	public BlCc getBlCc() {
		return blCc;
	}

	public void setBlCc(BlCc blCc) {
		this.blCc = blCc;
	}

	public List<BlCcPay> getBlCcPayList() {
		return blCcPayList;
	}

	public void setBlCcPayList(List<BlCcPay> blCcPayList) {
		this.blCcPayList = blCcPayList;
	}

	public String getBdInvStr() {
		return bdInvStr;
	}

	public void setBdInvStr(String bdInvStr) {
		this.bdInvStr = bdInvStr;
	}
}
