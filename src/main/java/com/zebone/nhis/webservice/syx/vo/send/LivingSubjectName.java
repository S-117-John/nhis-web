package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("livingSubjectName")
public class LivingSubjectName {
	private Value value;
	private SemanticsText semanticsText;
	public Value getValue() {
		if(value==null)value=new Value();
		return value;
	}
	public void setValue(Value value) {
		
				
		this.value = value;
	}
	public SemanticsText getSemanticsText() {
		if(semanticsText==null)semanticsText=new SemanticsText();
		return semanticsText;
	}
	public void setSemanticsText(SemanticsText semanticsText) {
		this.semanticsText = semanticsText;
	}
	
}
