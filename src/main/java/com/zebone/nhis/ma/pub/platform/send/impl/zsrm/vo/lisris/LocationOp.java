package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

public class LocationOp {

    private String resourceType;

    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private IdentifierOp identifier;
    private String gender;
    private String birthDate;
    private List<IdentifierOp> telecom;
    private List<TextElement> address;

    public IdentifierOp getIdentifier() {
        return identifier;
    }

    public void setIdentifier(IdentifierOp identifier) {
        this.identifier = identifier;
    }

    public List<IdentifierOp> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<IdentifierOp> telecom) {
        this.telecom = telecom;
    }

    public List<TextElement> getAddress() {
        return address;
    }

    public void setAddress(List<TextElement> address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }


    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }
}