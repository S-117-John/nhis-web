package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("directTarget")
public class DirectTarget {

	@XStreamAsAttribute
	private String typeCode;
	
	private IdentifiedEntity identifiedEntity;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public IdentifiedEntity getIdentifiedEntity() {
		if(identifiedEntity == null) {
			identifiedEntity = new IdentifiedEntity();
		}		
		return identifiedEntity;
	}

	public void setIdentifiedEntity(IdentifiedEntity identifiedEntity) {
		this.identifiedEntity = identifiedEntity;
	}
}
