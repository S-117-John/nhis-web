package com.zebone.nhis.ma.pub.platform.syx.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("subjectOf3")
public class SubjectOf3 {
	@XStreamAsAttribute
	private String typeCode;
	private Policy  policy;
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public Policy getPolicy() {
		if(policy==null) policy=new Policy();
		return policy;
	}
	public void setPolicy(Policy policy) {
		this.policy = policy;
	}
	
}
