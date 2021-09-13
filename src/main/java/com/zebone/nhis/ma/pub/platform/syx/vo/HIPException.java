package com.zebone.nhis.ma.pub.platform.syx.vo;

public class HIPException extends Exception {
	private String errorText;
	private String sendType;
	public HIPException(){}
	public HIPException(String message,Throwable cause){
		super(message,cause);
	}
	public HIPException(String message,String sendType){
		super(message);
		this.errorText=message;
		this.sendType=sendType;
	}
	public HIPException(Throwable cause){
		super(cause);
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public String getSendType() {
		return sendType;
	}
	public void setSendType(String sendType) {
		this.sendType = sendType;
	}
	
}
