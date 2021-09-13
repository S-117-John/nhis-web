package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import java.util.List;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

public class BlEnterer {
	
    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private List<Identifier> identifier;
    
    public BlEnterer(Object name, List<Identifier> identifier) {
        this.name = name;
        this.identifier = identifier;
    }
    
	public Object getName() {
		return name;
	}
	public List<Identifier> getIdentifier() {
		return identifier;
	}
	public void setName(Object name) {
		this.name = name;
	}
	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}
    
}