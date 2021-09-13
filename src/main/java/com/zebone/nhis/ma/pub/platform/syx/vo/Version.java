package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("version")
public class Version {
	@XStreamAsAttribute
	private String code;
	private DisplayName displayName;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public DisplayName getDisplayName() {
		if(displayName==null) displayName=new DisplayName();
		return displayName;
	}
	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}
	
}
