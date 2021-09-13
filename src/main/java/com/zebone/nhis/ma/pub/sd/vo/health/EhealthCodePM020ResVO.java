package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 PM020 响应类
 * @author zhangtao
 *
 */
public class EhealthCodePM020ResVO {

	/**
	 * 平台退费流水号(渠道退款受理凭证号)
	 */
	private String refOrderSerial;
	
	/**
	 * 平台原交易订单号
	 */
	private String orderSerial;

	public String getRefOrderSerial() {
		return refOrderSerial;
	}

	public void setRefOrderSerial(String refOrderSerial) {
		this.refOrderSerial = refOrderSerial;
	}

	public String getOrderSerial() {
		return orderSerial;
	}

	public void setOrderSerial(String orderSerial) {
		this.orderSerial = orderSerial;
	}

}
