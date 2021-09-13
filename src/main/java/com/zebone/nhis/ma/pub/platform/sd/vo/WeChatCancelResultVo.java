package com.zebone.nhis.ma.pub.platform.sd.vo;

public class WeChatCancelResultVo {
	private String statuscode;
	
	private String messageTxt;
	
	private WeChatCancelResDataVo data;

	public String getStatuscode() {
		return statuscode;
	}

	public void setStatuscode(String statuscode) {
		this.statuscode = statuscode;
	}

	public String getMessageTxt() {
		return messageTxt;
	}

	public void setMessageTxt(String messageTxt) {
		this.messageTxt = messageTxt;
	}

	public WeChatCancelResDataVo getData() {
		return data;
	}

	public void setData(WeChatCancelResDataVo data) {
		this.data = data;
	}
	
	
}


