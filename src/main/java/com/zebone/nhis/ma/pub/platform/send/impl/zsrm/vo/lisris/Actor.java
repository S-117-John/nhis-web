package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

public class Actor {

    private String resourceType;

    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private List<Identifier> identifier;
    private String gender;
    private String birthDate;
    private List<Identifier> telecom;
    private TextElement address;

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }

    public TextElement getAddress() {
        return address;
    }

    public void setAddress(TextElement address) {
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