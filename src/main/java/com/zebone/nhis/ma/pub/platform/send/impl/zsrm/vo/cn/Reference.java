package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.List;
import java.util.Map;

public class Reference {
    public Reference() {
    }

    public Reference(String resourceType, CodeableConcept form, Numerator amount, List<Extension> extension) {
        this.resourceType = resourceType;
        this.form = form;
        this.amount = amount;
        this.extension = extension;
    }

    public Reference(String resourceType, Identifier identifier, List<TextElement> name, String gender) {
        this.resourceType = resourceType;
        this.identifier = identifier;
        this.name = name;
        this.gender = gender;
    }

    public Reference(String resourceType, Identifier identifier, Coding clas) {
        this.resourceType = resourceType;
        this.identifier = identifier;
        this.clas = clas;
    }

    public Reference(String resourceType, Identifier identifier, List<TextElement> name) {
        this.resourceType = resourceType;
        this.identifier = identifier;
        this.name = name;
    }

    public Reference(String resourceType, Identifier identifier, String names) {
        this.resourceType = resourceType;
        this.identifier = identifier;
        this.names = names;
    }

    public Reference(Identifier identifier) {
        this.identifier = identifier;
    }

    public Reference(Timing timing, CodeableConcept route, List<Timing> doseAndRate, Identifier maxDosePerAdministration) {
        this.timing = timing;
        this.route = route;
        this.doseAndRate = doseAndRate;
        this.maxDosePerAdministration = maxDosePerAdministration;
    }

    public Reference(ValuePeriod validityPeriod, Reference performer) {
        this.validityPeriod = validityPeriod;
        this.performer = performer;
    }

    public Reference(String resourceType, String names, List<Identifier> identifierList) {
        this.resourceType = resourceType;
        this.names = names;
        this.identifierList = identifierList;
    }

    private String resourceType;
    private CodeableConcept form;
    private Numerator amount;
    private List<Extension> extension;

    private Identifier identifier;
    private List<TextElement> name;
    private String gender;
    @JSONField(name = "class")
    private Coding clas;
    @JSONField(name = "name")
    private String names;
    @JSONField(name = "identifier")
    private List<Identifier> identifierList;

    private Timing timing;
    private CodeableConcept route;
    private List<Timing> doseAndRate;
    private Identifier maxDosePerAdministration;
    private Map<String, Object> expectedSupplyDuration;
    private ValuePeriod validityPeriod;
    private Reference performer;

    public Map<String, Object> getExpectedSupplyDuration() {
        return expectedSupplyDuration;
    }

    public void setExpectedSupplyDuration(Map<String, Object> expectedSupplyDuration) {
        this.expectedSupplyDuration = expectedSupplyDuration;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public CodeableConcept getForm() {
        return form;
    }

    public void setForm(CodeableConcept form) {
        this.form = form;
    }

    public Numerator getAmount() {
        return amount;
    }

    public void setAmount(Numerator amount) {
        this.amount = amount;
    }

    public List<Extension> getExtension() {
        return extension;
    }

    public void setExtension(List<Extension> extension) {
        this.extension = extension;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public List<TextElement> getName() {
        return name;
    }

    public void setName(List<TextElement> name) {
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

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
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

    public Reference getPerformer() {
        return performer;
    }

    public void setPerformer(Reference performer) {
        this.performer = performer;
    }

    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }
}
