package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("time")
public class Time {
	@XStreamAsAttribute
	private String XSI_TYPE;
	@XStreamAsAttribute
	private String value;
	
	private Low low;
	private Any any;

	
	
	public String getXSI_TYPE() {
		return XSI_TYPE;
	}

	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Low getLow() {
		if(low==null)low=new Low();
		return low;
	}

	public void setLow(Low low) {
		this.low = low;
	}

	public Any getAny() {
		if(any==null)any=new Any();
		return any;
	}

	public void setAny(Any any) {
		this.any = any;
	}
	
	
}
