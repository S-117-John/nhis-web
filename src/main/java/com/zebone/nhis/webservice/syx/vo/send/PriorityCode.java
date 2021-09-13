package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("priorityCode")
public class PriorityCode {
	@XStreamAsAttribute
	private String code;
	@XStreamAsAttribute
	private String codeSystem;
	
	private DisplayName displayName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCodeSystem() {
		return codeSystem;
	}

	public void setCodeSystem(String codeSystem) {
		this.codeSystem = codeSystem;
	}

	public DisplayName getDisplayName() {
		if(displayName==null) displayName=new DisplayName();
		return displayName;
	}

	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}
	
}
