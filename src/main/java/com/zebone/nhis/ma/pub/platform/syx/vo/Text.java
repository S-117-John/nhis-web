package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("text")
public class Text {
	@XStreamAsAttribute
	private String value;
	
	private Description description; 
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Description getDescription() {
		if (description==null) {
			description = new Description();
		}
		return description;
	}

	public void setDescription(Description description) {
		this.description = description;
	}
	
}
