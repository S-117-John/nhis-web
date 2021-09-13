package com.zebone.nhis.cn.ipdw.vo;

public class OrdCaVo {
	
	//人员图片签名
	private byte[] imgSign;
	//会诊签名应答
	private byte[] imgSignResp;

	public byte[] getImgSignResp() {
		return imgSignResp;
	}

	public void setImgSignResp(byte[] imgSignResp) {
		this.imgSignResp = imgSignResp;
	}

	public byte[] getImgSign() {
		return imgSign;
	}

	public void setImgSign(byte[] imgSign) {
		this.imgSign = imgSign;
	}
}
