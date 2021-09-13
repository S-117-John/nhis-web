package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn.LocalLocation;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;


public class Encounter extends PhResource {

    private String status;

    @JSONField(name = "class")
    private Coding clas;
    private List<CodeableConcept> type;
    private Patient subject;
    private List<Participant> participant;
    private Period period;
    private List<TextElement> reasonCode;
    private List<Diagnosis> diagnosis;
    private Hospitalization hospitalization;
    private List<LocalLocation> location;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Coding getClas() {
        return clas;
    }

    public void setClas(Coding clas) {
        this.clas = clas;
    }

    public List<CodeableConcept> getType() {
        return type;
    }

    public void setType(List<CodeableConcept> type) {
        this.type = type;
    }

    public Patient getSubject() {
        return subject;
    }

    public void setSubject(Patient subject) {
        this.subject = subject;
    }

    public List<Participant> getParticipant() {
        return participant;
    }

    public void setParticipant(List<Participant> participant) {
        this.participant = participant;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public List<TextElement> getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(List<TextElement> reasonCode) {
        this.reasonCode = reasonCode;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public Hospitalization getHospitalization() {
        return hospitalization;
    }

    public void setHospitalization(Hospitalization hospitalization) {
        this.hospitalization = hospitalization;
    }

    public List<LocalLocation> getLocation() {
        return location;
    }

    public void setLocation(List<LocalLocation> location) {
        this.location = location;
    }
}