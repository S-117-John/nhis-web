package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;

public class Outcome {

    private String resourceType;
    private List<Issue> issue;
    private String id;
    private String implicitRules;
    private List<Identifier> identifier;
    private Boolean active;
    private List<CodeableConcept> type;
    private Object name;
    private List<String> alias;
    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setIssue(List<Issue> issue) {
        this.issue = issue;
    }

    public List<Issue> getIssue() {
        return issue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImplicitRules() {
        return implicitRules;
    }

    public void setImplicitRules(String implicitRules) {
        this.implicitRules = implicitRules;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<CodeableConcept> getType() {
        return type;
    }

    public void setType(List<CodeableConcept> type) {
        this.type = type;
    }

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }
}