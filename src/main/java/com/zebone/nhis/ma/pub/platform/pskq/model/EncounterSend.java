package com.zebone.nhis.ma.pub.platform.pskq.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.pskq.model.message.DataElement;

import java.util.List;

/**
 * @author tjq
 * ENCOUNTER
 * 推送门诊挂号信息数据体-2020-09-27
 */
public class EncounterSend {
    @JSONField(name = "PATIENT",ordinal = 1)
    private List<DataElement> patientInfo;

    @JSONField(name = "OUTPATIENT",ordinal = 2)
    private List<DataElement> outPatient;

    public List<DataElement> getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(List<DataElement> patientInfo) {
        this.patientInfo = patientInfo;
    }

    public List<DataElement> getOutPatient() {
        return outPatient;
    }

    public void setOutPatient(List<DataElement> outPatient) {
        this.outPatient = outPatient;
    }
}
