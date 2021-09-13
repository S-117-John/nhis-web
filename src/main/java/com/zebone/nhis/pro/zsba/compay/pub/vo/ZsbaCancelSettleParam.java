package com.zebone.nhis.pro.zsba.compay.pub.vo;

import com.zebone.nhis.common.module.pay.BlExtPay;

public class ZsbaCancelSettleParam {

	private String pkSettle;
	private String payMode;
	private String ssAmt;
	private BlExtPay blExtPay;
	private String pkPosTr;
	//1：中山医保 2：省内异地 3：省内工伤 4：跨省
	private String insType;
	//医保取消结算主键
	private String pkInsSt;
	public String getPkSettle() {
		return pkSettle;
	}
	public void setPkSettle(String pkSettle) {
		this.pkSettle = pkSettle;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getSsAmt() {
		return ssAmt;
	}
	public void setSsAmt(String ssAmt) {
		this.ssAmt = ssAmt;
	}
	public BlExtPay getBlExtPay() {
		return blExtPay;
	}
	public void setBlExtPay(BlExtPay blExtPay) {
		this.blExtPay = blExtPay;
	}
	public String getPkPosTr() {
		return pkPosTr;
	}
	public void setPkPosTr(String pkPosTr) {
		this.pkPosTr = pkPosTr;
	}
	public String getInsType() {
		return insType;
	}
	public void setInsType(String insType) {
		this.insType = insType;
	}
	public String getPkInsSt() {
		return pkInsSt;
	}
	public void setPkInsSt(String pkInsSt) {
		this.pkInsSt = pkInsSt;
	}

	
}
