package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.Date;
import java.util.List;

/**
 * 人员消息体
 */
public class Practitioner extends Outcome {
    @JsonProperty("name")
    private List<Identifier> name;
    private List<Identifier> telecom;
    private List<Identifiers> address;
    private String gender;
    private String birthDate;
    private List<Condition> qualification;
    private List<BdExtension> extension;
    private List<BdContained> contained;
    private String hospitalId;

    public List<Identifier> getTelecom() {
        return telecom;
    }

    public void setTelecom(List<Identifier> telecom) {
        this.telecom = telecom;
    }

    public List<Identifiers> getAddress() {
        return address;
    }

    public void setAddress(List<Identifiers> address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public List<Condition> getQualification() {
        return qualification;
    }

    public void setQualification(List<Condition> qualification) {
        this.qualification = qualification;
    }

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }

    @Override
    public List<Identifier> getName() {
        return name;
    }

    public void setName(List<Identifier> name) {
        this.name = name;
    }

    public List<BdContained> getContained() {
        return contained;
    }

    public void setContained(List<BdContained> contained) {
        this.contained = contained;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }
}

