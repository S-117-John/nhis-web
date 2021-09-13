package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;

public class Contact {
    public Contact() {
    }

    public Contact(List<CodeableConcept> relationship, TextElement name, List<Identifier> telecom, Address address, String gender) {
        this.relationship = relationship;
        this.name = name;
        this.telecom = telecom;
        this.address = address;
        this.gender = gender;
    }

    private List<CodeableConcept> relationship;
    private TextElement name;
    private List<Identifier> telecom;
    private Address address;
    private String gender;
    private List<Extension> extension;

    public List<CodeableConcept> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<CodeableConcept> relationship) {
        this.relationship = relationship;
    }

    public void setName(TextElement name) {
        this.name = name;
    }

    public TextElement getName() {
        return name;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }
}