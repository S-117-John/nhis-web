package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * 预约节点
 */
public class Appointment {

    private String resourceType;
    private List<Identifier> identifier;
    /**booked | arrived | fulfilled | cancelled | checked-in | waitlist
     已预约|已签到|已就诊|取消预约|就诊中|候诊*/
    private String status;
    private TextElement cancelationReason;
    private String description;
    private String start;
    private String end;
    @JsonDeserialize(using = ZsphJsonDateDeserializer.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date created;
    private List<CodeableConcept> appointmentType;
    private List<Outcome> actor;
    private List<Extension> extension;


    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TextElement getCancelationReason() {
        return cancelationReason;
    }

    public void setCancelationReason(TextElement cancelationReason) {
        this.cancelationReason = cancelationReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public List<CodeableConcept> getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(List<CodeableConcept> appointmentType) {
        this.appointmentType = appointmentType;
    }

    public List<Outcome> getActor() {
        return actor;
    }

    public void setActor(List<Outcome> actor) {
        this.actor = actor;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }
}