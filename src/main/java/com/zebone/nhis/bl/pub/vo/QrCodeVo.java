package com.zebone.nhis.bl.pub.vo;

import java.util.List;

/**
 * 微信支付宝二维码处理VO
 * @author ZhangMenghao
 */
public class QrCodeVo {

	private String weixinQrCode;  // 微信二维码
	private String aliPayQrCode;  // 支付宝二维码
	private List<String> pkExtpays; // 关联的第三方表主键
	
	public String getWeixinQrCode() {
		return weixinQrCode;
	}
	public void setWeixinQrCode(String weixinQrCode) {
		this.weixinQrCode = weixinQrCode;
	}
	public String getAliPayQrCode() {
		return aliPayQrCode;
	}
	public void setAliPayQrCode(String aliPayQrCode) {
		this.aliPayQrCode = aliPayQrCode;
	}
	public List<String> getPkExtpays() {
		return pkExtpays;
	}
	public void setPkExtpays(List<String> pkExtpays) {
		this.pkExtpays = pkExtpays;
	}
}
