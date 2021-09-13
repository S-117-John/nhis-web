package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd.BdExtension;

import java.util.Date;
import java.util.List;

public class OpCnOrder extends Outcome {
    private String status;
    private TextElement statusReason;
    private List<CodeableConcept> category;
    private CodeableConcept  medicationCodeableConcept;
    private Reference medicationReference;
    private Reference subject;
    private Reference encounter;
    private List<Reference> supportingInformation;
    @JSONField(format="yyyy-MM-dd HH:mm:ss.SS")
    private Date authoredOn;
    private Reference requester;
    private Reference performer;
    private Reference recorder;
    private Reference groupIdentifier;
    private List<TextElement> note;
    private List<Reference> dosageInstruction;
    private Reference dispenseRequest;
    private List<BdExtension> extension;
    private String outPrescriptionFlag;
    private String outPrescriptionState;

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public TextElement getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(TextElement statusReason) {
        this.statusReason = statusReason;
    }

    public List<CodeableConcept> getCategory() {
        return category;
    }

    public void setCategory(List<CodeableConcept> category) {
        this.category = category;
    }

    public CodeableConcept getMedicationCodeableConcept() {
        return medicationCodeableConcept;
    }

    public void setMedicationCodeableConcept(CodeableConcept medicationCodeableConcept) {
        this.medicationCodeableConcept = medicationCodeableConcept;
    }

    public Reference getMedicationReference() {
        return medicationReference;
    }

    public void setMedicationReference(Reference medicationReference) {
        this.medicationReference = medicationReference;
    }

    public Reference getSubject() {
        return subject;
    }

    public void setSubject(Reference subject) {
        this.subject = subject;
    }

    public Reference getEncounter() {
        return encounter;
    }

    public void setEncounter(Reference encounter) {
        this.encounter = encounter;
    }

    public List<Reference> getSupportingInformation() {
        return supportingInformation;
    }

    public void setSupportingInformation(List<Reference> supportingInformation) {
        this.supportingInformation = supportingInformation;
    }

    public Date getAuthoredOn() {
        return authoredOn;
    }

    public void setAuthoredOn(Date authoredOn) {
        this.authoredOn = authoredOn;
    }

    public Reference getRequester() {
        return requester;
    }

    public void setRequester(Reference requester) {
        this.requester = requester;
    }

    public Reference getPerformer() {
        return performer;
    }

    public void setPerformer(Reference performer) {
        this.performer = performer;
    }

    public Reference getRecorder() {
        return recorder;
    }

    public void setRecorder(Reference recorder) {
        this.recorder = recorder;
    }

    public Reference getGroupIdentifier() {
        return groupIdentifier;
    }

    public void setGroupIdentifier(Reference groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }

    public List<TextElement> getNote() {
        return note;
    }

    public void setNote(List<TextElement> note) {
        this.note = note;
    }

    public List<Reference> getDosageInstruction() {
        return dosageInstruction;
    }

    public void setDosageInstruction(List<Reference> dosageInstruction) {
        this.dosageInstruction = dosageInstruction;
    }

    public Reference getDispenseRequest() {
        return dispenseRequest;
    }

    public void setDispenseRequest(Reference dispenseRequest) {
        this.dispenseRequest = dispenseRequest;
    }

    public String getOutPrescriptionFlag() {
        return outPrescriptionFlag;
    }

    public void setOutPrescriptionFlag(String outPrescriptionFlag) {
        this.outPrescriptionFlag = outPrescriptionFlag;
    }

    public String getOutPrescriptionState() {
        return outPrescriptionState;
    }

    public void setOutPrescriptionState(String outPrescriptionState) {
        this.outPrescriptionState = outPrescriptionState;
    }
}
