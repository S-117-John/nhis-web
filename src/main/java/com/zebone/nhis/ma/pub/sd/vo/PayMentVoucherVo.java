package com.zebone.nhis.ma.pub.sd.vo;

/**
 * 预交金缴款信息
 * @author Administrator
 *
 */
public class PayMentVoucherVo {
	
	/**
	 * 预交金凭证代码
	 * true
	 */
	private String voucherBatchCode;
	
	/**
	 * 预交金凭证号码
	 * true
	 */
	private String voucherNo;
	
	/**
	 * 预交金凭证金额
	 * true
	 */
	private String voucherAmt;

	public String getVoucherBatchCode() {
		return voucherBatchCode;
	}

	public void setVoucherBatchCode(String voucherBatchCode) {
		this.voucherBatchCode = voucherBatchCode;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getVoucherAmt() {
		return voucherAmt;
	}

	public void setVoucherAmt(String voucherAmt) {
		this.voucherAmt = voucherAmt;
	}
	
	
}
