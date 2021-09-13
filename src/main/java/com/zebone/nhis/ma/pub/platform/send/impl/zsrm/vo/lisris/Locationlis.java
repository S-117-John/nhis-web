package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.NameObjDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.List;

public class Locationlis {

    private String resourceType;

    @JsonDeserialize(using = NameObjDeserializer.class)
    private Object name;
    private Identifier identifier;
    private String gender;
    private String birthDate;
    private String flagSpec;//特诊标志
    private List<Identifier> telecom;
    private List<TextElement> address;

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<Identifier> telecom) {
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

    public String getFlagSpec() { return flagSpec; }

    public void setFlagSpec(String flagSpec) { this.flagSpec = flagSpec; }
}