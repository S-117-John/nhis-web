package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Actor;

import java.util.List;

public class CnOrderOperation extends PhResource {

    private String status;

    private TextElement statusReason;

    private List<CodeableConcept> category;

    private CodeableConcept  medicationCodeableConcept;

    private OrderReference medicationReference;

    private Actor subject;

    private OrderReference encounter;
    private List<OrderReference> supportingInformation;
    private String authoredOn;
    private OrderReference requester;
    private OrderReference performer;
    private TextElement performerType;
    private OrderReference recorder;
    private Identifier groupIdentifier;
    private List<TextElement> note;
    private List<Insurance> insurance;
    private List<OrderReference> dosageInstruction;
    private OrderReference dispenseRequest;


    public static class Insurance{
        @JSONField(name = "class")
        private List<InsuranceClass> insuranceClass;

        public Insurance(List<InsuranceClass> insuranceClass) {
            this.insuranceClass = insuranceClass;
        }

        public List<InsuranceClass> getInsuranceClass() {
            return insuranceClass;
        }

        public void setInsuranceClass(List<InsuranceClass> insuranceClass) {
            this.insuranceClass = insuranceClass;
        }
    }

    public static class InsuranceClass{
        private String value;
        private String name;

        public InsuranceClass(String value, String name) {
            this.value = value;
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
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

    public OrderReference getMedicationReference() {
        return medicationReference;
    }

    public void setMedicationReference(OrderReference medicationReference) {
        this.medicationReference = medicationReference;
    }

    public Actor getSubject() {
        return subject;
    }

    public void setSubject(Actor subject) {
        this.subject = subject;
    }

    public OrderReference getEncounter() {
        return encounter;
    }

    public void setEncounter(OrderReference encounter) {
        this.encounter = encounter;
    }

    public List<OrderReference> getSupportingInformation() {
        return supportingInformation;
    }

    public void setSupportingInformation(List<OrderReference> supportingInformation) {
        this.supportingInformation = supportingInformation;
    }

    public String getAuthoredOn() {
        return authoredOn;
    }

    public void setAuthoredOn(String authoredOn) {
        this.authoredOn = authoredOn;
    }

    public OrderReference getRequester() {
        return requester;
    }

    public void setRequester(OrderReference requester) {
        this.requester = requester;
    }

    public OrderReference getPerformer() {
        return performer;
    }

    public void setPerformer(OrderReference performer) {
        this.performer = performer;
    }

    public TextElement getPerformerType() {
        return performerType;
    }

    public void setPerformerType(TextElement performerType) {
        this.performerType = performerType;
    }

    public OrderReference getRecorder() {
        return recorder;
    }

    public void setRecorder(OrderReference recorder) {
        this.recorder = recorder;
    }

    public Identifier getGroupIdentifier() {
        return groupIdentifier;
    }

    public void setGroupIdentifier(Identifier groupIdentifier) {
        this.groupIdentifier = groupIdentifier;
    }

    public List<TextElement> getNote() {
        return note;
    }

    public void setNote(List<TextElement> note) {
        this.note = note;
    }

    public List<Insurance> getInsurance() {
        return insurance;
    }

    public void setInsurance(List<Insurance> insurance) {
        this.insurance = insurance;
    }

    public List<OrderReference> getDosageInstruction() {
        return dosageInstruction;
    }

    public void setDosageInstruction(List<OrderReference> dosageInstruction) {
        this.dosageInstruction = dosageInstruction;
    }

    public OrderReference getDispenseRequest() {
        return dispenseRequest;
    }

    public void setDispenseRequest(OrderReference dispenseRequest) {
        this.dispenseRequest = dispenseRequest;
    }
}
