package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("targetMessage")
public class TargetMessage {
	private Id id ;

	public Id getId() {
		if(id==null)id=new Id();
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

}
