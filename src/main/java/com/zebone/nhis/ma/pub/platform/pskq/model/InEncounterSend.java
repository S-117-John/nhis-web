package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

public class InEncounterSend {
    @JSONField(name = "PATIENT",ordinal = 1)
    private List<DataElement> patient;

    @JSONField(name = "INPATIENT",ordinal = 2)
    private List<DataElement> inPatient;

    public List<DataElement> getPatient() {
        return patient;
    }

    public void setPatient(List<DataElement> patient) {
        this.patient = patient;
    }

    public List<DataElement> getInPatient() {
        return inPatient;
    }

    public void setInPatient(List<DataElement> inPatient) {
        this.inPatient = inPatient;
    }
}
