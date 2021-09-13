package com.zebone.nhis.bl.pub.vo;

/**
 * 微信付款和支付宝付款的公共入参
 * @author Administrator
 */
public class PayInputVo {

	private String outTradeNo;      //商户订单号       必填
	private String authCode;        //授权码               必填
	private String body;            //商品描述           必填
	private String pkPi;            //患者信息
	private String pkPv;            //就诊主键
	
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getAuthCode() {
		return authCode;
	}
	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getPkPi() {
		return pkPi;
	}
	public void setPkPi(String pkPi) {
		this.pkPi = pkPi;
	}
	public String getPkPv() {
		return pkPv;
	}
	public void setPkPv(String pkPv) {
		this.pkPv = pkPv;
	}
}
