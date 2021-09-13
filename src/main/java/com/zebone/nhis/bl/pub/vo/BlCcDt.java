package com.zebone.nhis.bl.pub.vo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.zebone.nhis.common.module.bl.BlCc;
import com.zebone.nhis.common.module.bl.BlCcPay;

public class BlCcDt {
	
	private BlCc blCc;
	
	private Collection<BlCcPay> blCcPayList;
	
	private List<InvInfo> InvInfo;
	private List<InvalidStInv> InvalidList;
	
	//欠款金额
	private Double amtAr;
	
	//支付信息集合(深大用)
	private List<PayInfoSd> payListSd;
	
	//扩展参数
	private Map<String,Object> extParam;
	
	//扩展参数集合
	private List<Map<String,Object>> extListParam;
	
	public List<Map<String, Object>> getExtListParam() {
		return extListParam;
	}

	public void setExtListParam(List<Map<String, Object>> extListParam) {
		this.extListParam = extListParam;
	}

	public List<PayInfoSd> getPayListSd() {
		return payListSd;
	}

	public void setPayListSd(List<PayInfoSd> payListSd) {
		this.payListSd = payListSd;
	}

	public Map<String, Object> getExtParam() {
		return extParam;
	}

	public void setExtParam(Map<String, Object> extParam) {
		this.extParam = extParam;
	}

	public Double getAmtAr() {
		return amtAr;
	}

	public void setAmtAr(Double amtAr) {
		this.amtAr = amtAr;
	}

	public List<InvInfo> getInvInfo() {
		return InvInfo;
	}

	public void setInvInfo(List<InvInfo> invInfo) {
		InvInfo = invInfo;
	}

	public List<InvalidStInv> getInvalidList() {
		return InvalidList;
	}

	public void setInvalidList(List<InvalidStInv> invalidList) {
		InvalidList = invalidList;
	}
	
	private Double amtGet;
	public Double getAmtGet() {
		return amtGet;
	}

	public void setAmtGet(Double amtGet) {
		this.amtGet = amtGet;
	}

	public BlCc getBlCc() {
		return blCc;
	}

	public void setBlCc(BlCc blCc) {
		this.blCc = blCc;
	}

	public Collection<BlCcPay> getBlCcPayList() {
		return blCcPayList;
	}

	public void setBlCcPayList(Collection<BlCcPay> blCcPayList) {
		this.blCcPayList = blCcPayList;
	}

}
