package com.zebone.nhis.ma.pub.platform.pskq.model;


/**
 *
 * 住院登记数据集

 */
public class InEncounter {

    private Patient patient;

    private EncounterInpatient encounterInpatient;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public EncounterInpatient getEncounterInpatient() {
        return encounterInpatient;
    }

    public void setEncounterInpatient(EncounterInpatient encounterInpatient) {
        this.encounterInpatient = encounterInpatient;
    }
}
