package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

public class BlLocation {
	
    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private List<Identifier> identifier;

    public List<Identifier> getIdentifier() {
		return identifier;
	}

	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}

	public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

}