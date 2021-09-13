package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;

import java.util.List;

/**
 * 预约 Resource节点
 */
public class ApptResource extends PhResource {

    private Patient subject;

    private List<Appointment> appointment;

    public Patient getSubject() {
        return subject;
    }

    public void setSubject(Patient subject) {
        this.subject = subject;
    }

    public List<Appointment> getAppointment() {
        return appointment;
    }

    public void setAppointment(List<Appointment> appointment) {
        this.appointment = appointment;
    }
}
