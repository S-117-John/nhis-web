package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("expectedUseTime")
public class ExpectedUseTime {
	@XStreamAsAttribute
	private String validTimeLow;
	@XStreamAsAttribute
	private String validTimeHigh;
	public String getValidTimeLow() {
		return validTimeLow;
	}
	public void setValidTimeLow(String validTimeLow) {
		this.validTimeLow = validTimeLow;
	}
	public String getValidTimeHigh() {
		return validTimeHigh;
	}
	public void setValidTimeHigh(String validTimeHigh) {
		this.validTimeHigh = validTimeHigh;
	}
	
	
}
