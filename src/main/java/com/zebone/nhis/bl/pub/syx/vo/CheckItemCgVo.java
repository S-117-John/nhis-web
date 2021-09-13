package com.zebone.nhis.bl.pub.syx.vo;

public class CheckItemCgVo {
	
	private String errMsg;
	
	private Boolean errFlag = true;

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Boolean getErrFlag() {
		return errFlag;
	}

	public void setErrFlag(Boolean errFlag) {
		this.errFlag = errFlag;
	}
}
