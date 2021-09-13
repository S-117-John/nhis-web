package com.zebone.nhis.webservice.syx.vo.send;

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
