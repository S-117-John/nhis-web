package com.zebone.nhis.webservice.pskq.model.message;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class PatientResultResponse {

    @JSONField(name = "PATIENT")
    private List<DataElement> patient;

    public List<DataElement> getPatient() {
        return patient;
    }

    public void setPatient(List<DataElement> patient) {
        this.patient = patient;
    }
}
