package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;
import java.util.Map;

public class EsbResponse {
    private String staus;
    private List<String> identifier;
    private String status;
    private Map<String,Object> statusreason;
    private List<String> category;
    private Map<String,Object> medicationcodeableconcept;
    private Map<String,Object> medicationreference;
    private Map<String,Object> subject;
    private Map<String,Object> encounter;
    private List<String> supportinginformation;
    private String authoredon;
    private Map<String,Object> requester;
    private Map<String,Object> perform;
    private Map<String,Object> performertype;
    private Map<String,Object> recorder;
    private Map<String,Object> groupidentifier;
    private List<String> note;
    private List<String> insurance;
    private List<String> dosageinstruction;
    private Map<String,Object> dispenserequest;

    public String getStaus() {
        return staus;
    }

    public void setStaus(String staus) {
        this.staus = staus;
    }

    public List<String> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<String> identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getStatusreason() {
        return statusreason;
    }

    public void setStatusreason(Map<String, Object> statusreason) {
        this.statusreason = statusreason;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Map<String, Object> getMedicationcodeableconcept() {
        return medicationcodeableconcept;
    }

    public void setMedicationcodeableconcept(Map<String, Object> medicationcodeableconcept) {
        this.medicationcodeableconcept = medicationcodeableconcept;
    }

    public Map<String, Object> getMedicationreference() {
        return medicationreference;
    }

    public void setMedicationreference(Map<String, Object> medicationreference) {
        this.medicationreference = medicationreference;
    }

    public Map<String, Object> getSubject() {
        return subject;
    }

    public void setSubject(Map<String, Object> subject) {
        this.subject = subject;
    }

    public Map<String, Object> getEncounter() {
        return encounter;
    }

    public void setEncounter(Map<String, Object> encounter) {
        this.encounter = encounter;
    }

    public List<String> getSupportinginformation() {
        return supportinginformation;
    }

    public void setSupportinginformation(List<String> supportinginformation) {
        this.supportinginformation = supportinginformation;
    }

    public String getAuthoredon() {
        return authoredon;
    }

    public void setAuthoredon(String authoredon) {
        this.authoredon = authoredon;
    }

    public Map<String, Object> getRequester() {
        return requester;
    }

    public void setRequester(Map<String, Object> requester) {
        this.requester = requester;
    }

    public Map<String, Object> getPerform() {
        return perform;
    }

    public void setPerform(Map<String, Object> perform) {
        this.perform = perform;
    }

    public Map<String, Object> getPerformertype() {
        return performertype;
    }

    public void setPerformertype(Map<String, Object> performertype) {
        this.performertype = performertype;
    }

    public Map<String, Object> getRecorder() {
        return recorder;
    }

    public void setRecorder(Map<String, Object> recorder) {
        this.recorder = recorder;
    }

    public Map<String, Object> getGroupidentifier() {
        return groupidentifier;
    }

    public void setGroupidentifier(Map<String, Object> groupidentifier) {
        this.groupidentifier = groupidentifier;
    }

    public List<String> getNote() {
        return note;
    }

    public void setNote(List<String> note) {
        this.note = note;
    }

    public List<String> getInsurance() {
        return insurance;
    }

    public void setInsurance(List<String> insurance) {
        this.insurance = insurance;
    }

    public List<String> getDosageinstruction() {
        return dosageinstruction;
    }

    public void setDosageinstruction(List<String> dosageinstruction) {
        this.dosageinstruction = dosageinstruction;
    }

    public Map<String, Object> getDispenserequest() {
        return dispenserequest;
    }

    public void setDispenserequest(Map<String, Object> dispenserequest) {
        this.dispenserequest = dispenserequest;
    }
}
