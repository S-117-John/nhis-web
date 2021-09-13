package com.zebone.nhis.ma.pub.sd.vo;
/**
 * 发票开立返回结果集VO
 * @author Administrator
 *
 */
public class EnotePrintResVo {
	
	/**
	 * 电子票据代码
	 * true
	 */
	private String billBatchCode;
	
	/**
	 * 电子票据号码
	 * true
	 */
	private String billNo;
	
	/**
	 * 电子校验码
	 * true
	 */
	private String random;
	
	/**
	 * 电子票据生成时间
	 * true
	 * yyyyMMddHHmmssSSS
	 */
	private String createTime;
	
	/**
	 * 电子票据二维码图片数据
	 * true
	 * 该值已Base64编码，解析时需要Base64解码,图片格式为:PNG
	 */
	private String billQRCode;
	
	/**
	 * 电子票据H5页面URL
	 * true
	 */
	private String pictureUrl;
	
	/**
	 * 电子票据外网H5页面URL
	 * false
	 */
	private String pictureNetUrl;

	public String getBillBatchCode() {
		return billBatchCode;
	}

	public void setBillBatchCode(String billBatchCode) {
		this.billBatchCode = billBatchCode;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRandom() {
		return random;
	}

	public void setRandom(String random) {
		this.random = random;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBillQRCode() {
		return billQRCode;
	}

	public void setBillQRCode(String billQRCode) {
		this.billQRCode = billQRCode;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPictureNetUrl() {
		return pictureNetUrl;
	}

	public void setPictureNetUrl(String pictureNetUrl) {
		this.pictureNetUrl = pictureNetUrl;
	}
	
}
