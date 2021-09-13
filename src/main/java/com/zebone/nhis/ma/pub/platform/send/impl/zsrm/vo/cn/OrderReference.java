package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;

public class OrderReference {
    private CodeableConcept form;
    @JSONField(name = "name")
    private Object name;
    private String gender;
    @JSONField(name = "class")
    private Coding clas;

    @JSONField(name = "identifier")
    private List<Identifier> identifiers;

    private Timing timing;
    private CodeableConcept route;
    private List<Timing> doseAndRate;
    private Identifier maxDosePerAdministration;

    private ValuePeriod validityPeriod;
    private OrderReference performer;
    private Mumerator quantity;

    public OrderReference(List<Identifier> identifiers, Object name) {
        this.identifiers = identifiers;
        this.name = name;
    }
    public OrderReference() {
    }

    public OrderReference(Timing timing, CodeableConcept route, List<Timing> doseAndRate, Identifier maxDosePerAdministration) {
        this.timing = timing;
        this.route = route;
        this.doseAndRate = doseAndRate;
        this.maxDosePerAdministration = maxDosePerAdministration;
    }

    public OrderReference(ValuePeriod validityPeriod, OrderReference performer) {
        this.validityPeriod = validityPeriod;
        this.performer = performer;
    }

    public CodeableConcept getForm() {
        return form;
    }

    public void setForm(CodeableConcept form) {
        this.form = form;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Coding getClas() {
        return clas;
    }

    public void setClas(Coding clas) {
        this.clas = clas;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<Identifier> identifiers) {
        this.identifiers = identifiers;
    }

    public Timing getTiming() {
        return timing;
    }

    public void setTiming(Timing timing) {
        this.timing = timing;
    }

    public CodeableConcept getRoute() {
        return route;
    }

    public void setRoute(CodeableConcept route) {
        this.route = route;
    }

    public List<Timing> getDoseAndRate() {
        return doseAndRate;
    }

    public void setDoseAndRate(List<Timing> doseAndRate) {
        this.doseAndRate = doseAndRate;
    }

    public Identifier getMaxDosePerAdministration() {
        return maxDosePerAdministration;
    }

    public void setMaxDosePerAdministration(Identifier maxDosePerAdministration) {
        this.maxDosePerAdministration = maxDosePerAdministration;
    }

    public ValuePeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(ValuePeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public OrderReference getPerformer() {
        return performer;
    }

    public void setPerformer(OrderReference performer) {
        this.performer = performer;
    }

    public Mumerator getQuantity() {
        return quantity;
    }

    public void setQuantity(Mumerator quantity) {
        this.quantity = quantity;
    }
}
