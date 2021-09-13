package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv.Diagnosis;

import java.util.List;

public class BabyAdmitResource extends PhResource {

    private String status;
    private List<Diagnosis> diagnosis;
    private List<LocalLocation> location;
    private Patient subject;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public List<LocalLocation> getLocation() {
        return location;
    }

    public void setLocation(List<LocalLocation> location) {
        this.location = location;
    }

    public Patient getSubject() {
        return subject;
    }

    public void setSubject(Patient subject) {
        this.subject = subject;
    }
}