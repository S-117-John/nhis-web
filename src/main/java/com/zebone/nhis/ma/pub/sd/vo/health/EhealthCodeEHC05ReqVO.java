package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 EHC05 请求类
 * @author zhangtao
 *
 */
public class EhealthCodeEHC05ReqVO {

	/**
	 * 识读终端扫电子健康卡二维码的完整内容
	 */
	private String healthCardQrcodeData;
	
	/**
	 * 二维码类型。 0-动态；1-静态；2-不限定。
	 */
	private String limitCodeType;

	public String getHealthCardQrcodeData() {
		return healthCardQrcodeData;
	}

	public void setHealthCardQrcodeData(String healthCardQrcodeData) {
		this.healthCardQrcodeData = healthCardQrcodeData;
	}

	public String getLimitCodeType() {
		return limitCodeType;
	}

	public void setLimitCodeType(String limitCodeType) {
		this.limitCodeType = limitCodeType;
	}

}
