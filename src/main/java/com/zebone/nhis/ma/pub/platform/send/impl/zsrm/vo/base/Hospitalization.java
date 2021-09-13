package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Hospitalization {

    private CodeableConcept admitSource;
    private CodeableConcept dischargeDisposition;

    public CodeableConcept getAdmitSource() {
        return admitSource;
    }

    public void setAdmitSource(CodeableConcept admitSource) {
        this.admitSource = admitSource;
    }

    public CodeableConcept getDischargeDisposition() {
        return dischargeDisposition;
    }

    public void setDischargeDisposition(CodeableConcept dischargeDisposition) {
        this.dischargeDisposition = dischargeDisposition;
    }
}