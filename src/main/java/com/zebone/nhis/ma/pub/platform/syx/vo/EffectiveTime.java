package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("effectiveTime")
public class EffectiveTime {
	@XStreamAsAttribute
	private String XSI_TYPE;
	@XStreamAsAttribute
	private String validTimeLow;
	@XStreamAsAttribute
	private String validTimeHigh;
	private Low low;
	private Any any;
	private High high;
	private Code code;
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}

	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}

	public Any getAny() {
		if(any==null)any=new Any();
		return any;
	}

	public void setAny(Any any) {
		this.any = any;
	}

	public Low getLow() {
		if(low==null)low=new Low();
		return low;
	}

	public void setLow(Low low) {
		this.low = low;
	}

	public High getHigh() {
		if(high==null)high=new High();
		return high;
	}

	public void setHigh(High high) {
		this.high = high;
	}

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

	public Code getCode() {
		if(code==null) code=new Code();
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}
	
}
