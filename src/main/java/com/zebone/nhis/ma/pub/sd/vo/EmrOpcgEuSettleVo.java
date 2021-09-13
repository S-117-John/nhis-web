package com.zebone.nhis.ma.pub.sd.vo;

public class EmrOpcgEuSettleVo {
	/**
	 * 唯一标识
	 */
	private String chargeId;
	
	/**
	 * 收费状态
	 * 0：删除，1：已收费，2退费
	 */
	private String euSettle;

	public String getChargeId() {
		return chargeId;
	}

	public void setChargeId(String chargeId) {
		this.chargeId = chargeId;
	}

	public String getEuSettle() {
		return euSettle;
	}

	public void setEuSettle(String euSettle) {
		this.euSettle = euSettle;
	}
	
}
