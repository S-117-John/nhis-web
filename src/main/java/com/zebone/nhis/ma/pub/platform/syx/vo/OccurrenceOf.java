package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("occurrenceOf")
public class OccurrenceOf {
	private ParentRequestReference parentRequestReference;

	public ParentRequestReference getParentRequestReference() {
		if(parentRequestReference==null) parentRequestReference=new ParentRequestReference();
		return parentRequestReference;
	}

	public void setParentRequestReference(
			ParentRequestReference parentRequestReference) {
		this.parentRequestReference = parentRequestReference;
	}
	
}
