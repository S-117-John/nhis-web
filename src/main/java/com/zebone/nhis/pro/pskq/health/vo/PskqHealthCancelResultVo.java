package com.zebone.nhis.pro.pskq.health.vo;

public class PskqHealthCancelResultVo {
	private String statuscode;
	
	private String messageTxt;
	
	private PskqHealthCancelResDataVo data;

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

	public PskqHealthCancelResDataVo getData() {
		return data;
	}

	public void setData(PskqHealthCancelResDataVo data) {
		this.data = data;
	}
	
	
}


