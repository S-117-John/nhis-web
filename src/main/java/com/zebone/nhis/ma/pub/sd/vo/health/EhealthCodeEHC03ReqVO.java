package com.zebone.nhis.ma.pub.sd.vo.health;

/**
 * 电子健康码 EHC03 请求类
 * @author zhangtao
 *
 */
public class EhealthCodeEHC03ReqVO {

	/**
	 * 查询方式  1-【电子健康卡ID】必填；2-【证件类型】和【证件号码】必填
	 */
	private String queryType;
	
	/**
	 * 电子健康卡ID
	 */
	private String healthCardId;
	
	/**
	 * 证件类型
	 */
	private String idType;
	
	/**
	 * 证件号
	 */
	private String idNo;
	
	/**
	 * 是否返回电子健康卡二维码图片标识
	 */
	private String qrCodePicFlag;

	public String getQueryType() {
		return queryType;
	}

	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}

	public String getHealthCardId() {
		return healthCardId;
	}

	public void setHealthCardId(String healthCardId) {
		this.healthCardId = healthCardId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getQrCodePicFlag() {
		return qrCodePicFlag;
	}

	public void setQrCodePicFlag(String qrCodePicFlag) {
		this.qrCodePicFlag = qrCodePicFlag;
	}

}
