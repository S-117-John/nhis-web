package com.zebone.nhis.ma.pub.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("result")
public class Result {
	private String id;
	private String text;
	private String requestId;
	private String requestTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	
}
