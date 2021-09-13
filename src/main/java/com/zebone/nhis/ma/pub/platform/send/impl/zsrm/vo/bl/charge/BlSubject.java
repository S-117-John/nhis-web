package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import org.codehaus.jackson.map.annotate.JsonDeserialize;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

public class BlSubject {
	
    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private Identifier identifier;
    private String birthDate;

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    
}