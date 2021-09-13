package com.zebone.nhis.ma.pub.platform.pskq.model;

/**
 * @author tjq
 * ENCOUNTER
 * 门诊挂号信息数据体-2020-09-27
 */
public class Encounter {

    private PatientInfo patientInfo;

    private Outpatient outPatient;

    public PatientInfo getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(PatientInfo patientInfo) {
        this.patientInfo = patientInfo;
    }

    public Outpatient getOutPatient() {
        return outPatient;
    }

    public void setOutPatient(Outpatient outPatient) {
        this.outPatient = outPatient;
    }
}
