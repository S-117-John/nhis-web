package com.zebone.nhis.webservice.syx.vo.send;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("replacementOf")
public class ReplacementOf {
	@XStreamAsAttribute
	private String typeCode;
	
	private PriorRegistration priorRegistration;

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public PriorRegistration getPriorRegistration() {
		if(priorRegistration==null)priorRegistration = new PriorRegistration();
		return priorRegistration;
	}

	public void setPriorRegistration(PriorRegistration priorRegistration) {
		this.priorRegistration = priorRegistration;
	}
	
	
}
