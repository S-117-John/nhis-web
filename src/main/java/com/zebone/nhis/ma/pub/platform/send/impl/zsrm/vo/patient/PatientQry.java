package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Participant;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

public class PatientQry extends PhResource {

    private Boolean active;
    private List<TextElement> name;
    private List<Identifier> telecom;
    private String gender;
    private String birthDate;
    private List<Address> address;
    private CodeableConcept maritalStatus;
    private List<Contact> contact;
    private List<Participant> participant;

    private  List<Link> link;

    public List<Link> getLink() {
        return link;
    }

    public void setLink(List<Link> link) {
        this.link = link;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public void setName(List<TextElement> name) {
        this.name = name;
    }
    public List<TextElement> getName() {
        return name;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }
    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public String getGender() {
        return gender;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public String getBirthDate() {
        return birthDate;
    }

    public void setAddress(List<Address> address) {
        this.address = address;
    }
    public List<Address> getAddress() {
        return address;
    }

    public void setMaritalStatus(CodeableConcept maritalStatus) {
        this.maritalStatus = maritalStatus;
    }
    public CodeableConcept getMaritalStatus() {
        return maritalStatus;
    }

    public void setContact(List<Contact> contact) {
        this.contact = contact;
    }
    public List<Contact> getContact() {
        return contact;
    }

    public boolean isActive() {
        return active;
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }
}
