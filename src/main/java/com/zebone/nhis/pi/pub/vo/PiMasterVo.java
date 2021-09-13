package com.zebone.nhis.pi.pub.vo;

import com.zebone.nhis.common.module.pi.PiMaster;

public class PiMasterVo extends PiMaster{
	private String cardNumber;//诊疗卡卡号
	/** 输出格式化年龄*/
	private String ageFormat;

	public String getAgeFormat() {
		return ageFormat;
	}
	public void setAgeFormat(String ageFormat) {
		this.ageFormat = ageFormat;
	}
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

}
