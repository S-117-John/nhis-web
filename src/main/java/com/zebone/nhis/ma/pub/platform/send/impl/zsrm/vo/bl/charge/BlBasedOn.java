package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import java.util.List;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

public class BlBasedOn {

    private List<Identifier> identifier;
    
    private List<BlCategory> category;
    
    private BlCodeableConcept code;

	public List<Identifier> getIdentifier() {
		return identifier;
	}

	public List<BlCategory> getCategory() {
		return category;
	}

	public BlCodeableConcept getCode() {
		return code;
	}

	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}

	public void setCategory(List<BlCategory> category) {
		this.category = category;
	}

	public void setCode(BlCodeableConcept code) {
		this.code = code;
	}
}