package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("birthTime")
public class BirthTime {
	@XStreamAsAttribute
	private String value;

	private OriginalText originalText;
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public OriginalText getOriginalText() {
		if(originalText==null)originalText=new OriginalText();
		return originalText;
	}

	public void setOriginalText(OriginalText originalText) {
		this.originalText = originalText;
	}
	
}
