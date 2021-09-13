package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("livingSubjectId")
public class LivingSubjectId {
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
