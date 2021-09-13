package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("code")
public class Code {
	@XStreamAsAttribute
	private String code;
	@XStreamAsAttribute
	private String codeSystem;
	@XStreamAsAttribute
	private String codeSystemName;
	private DisplayName displayName;
	private OriginalText originalText;
	private Time time;
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

	public String getCodeSystemName() {
		return codeSystemName;
	}

	public void setCodeSystemName(String codeSystemName) {
		this.codeSystemName = codeSystemName;
	}

	public DisplayName getDisplayName() {
		if(displayName==null)displayName=new DisplayName();
		return displayName;
	}

	public void setDisplayName(DisplayName displayName) {
		this.displayName = displayName;
	}

	public OriginalText getOriginalText() {
		if(originalText==null)originalText=new OriginalText();
		return originalText;
	}

	public void setOriginalText(OriginalText originalText) {
		this.originalText = originalText;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
}
