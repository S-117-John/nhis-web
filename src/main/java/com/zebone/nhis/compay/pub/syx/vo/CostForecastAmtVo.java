package com.zebone.nhis.compay.pub.syx.vo;

import com.zebone.platform.modules.dao.build.au.Field;

public class CostForecastAmtVo {
	/* 总费用 */
	@Field(value = "AMT")
	private double amt;
	/* 未结费用 */
	@Field(value = "AMT_NOSETTLE")
	private double amtNosettle;
	/* 内部医保患者自付 */
	@Field(value = "AMT_PI")
	private double amtPi;

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public double getAmtNosettle() {
		return amtNosettle;
	}

	public void setAmtNosettle(double amtNosettle) {
		this.amtNosettle = amtNosettle;
	}

	public double getAmtPi() {
		return amtPi;
	}

	public void setAmtPi(double amtPi) {
		this.amtPi = amtPi;
	}

}
