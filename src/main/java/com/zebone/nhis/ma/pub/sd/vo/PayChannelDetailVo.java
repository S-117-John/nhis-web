package com.zebone.nhis.ma.pub.sd.vo;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 交费渠道列表
 * @author Administrator
 *
 */
public class PayChannelDetailVo {
	
	/**
	 * 交费渠道编码
	 * true
	 *  01	POS刷卡
		02	现金
		03	转账
		04	支付宝
		05 	微信
		06	支票
		07	卡支付
		08	银联卡
		09	自助机缴费
	 */
	@JSONField(ordinal = 1)
	private String payChannelCode;
	
	/**
	 * 交费渠道金额
	 * true
	 */
	@JSONField(ordinal = 2)
	private Double payChannelValue;

	public String getPayChannelCode() {
		return payChannelCode;
	}

	public void setPayChannelCode(String payChannelCode) {
		this.payChannelCode = payChannelCode;
	}

	public Double getPayChannelValue() {
		return payChannelValue;
	}

	public void setPayChannelValue(Double payChannelValue) {
		this.payChannelValue = payChannelValue;
	}
	
	
	
}
