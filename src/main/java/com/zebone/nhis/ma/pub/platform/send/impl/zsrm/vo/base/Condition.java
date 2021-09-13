package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

public class Condition {

    private String resourceType;
    private CodeableConcept code;


    public void setCode(CodeableConcept code) {
        this.code = code;
    }

    public CodeableConcept getCode() {
        return code;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Condition() {
    }

    public Condition(String resourceType, CodeableConcept code) {
        this.resourceType = resourceType;
        this.code = code;
    }
}