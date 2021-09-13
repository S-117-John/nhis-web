package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("denominator")
public class Denominator {
	@XStreamAsAttribute
	private String XSI_TYPE;
	@XStreamAsAttribute
	private String unit;
	@XStreamAsAttribute
	private String value;

	public String getXSI_TYPE() {
		return XSI_TYPE;
	}

	public void setXSI_TYPE(String xSI_TYPE) {
		XSI_TYPE = xSI_TYPE;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
