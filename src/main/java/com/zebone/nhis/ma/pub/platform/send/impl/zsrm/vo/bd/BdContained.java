package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;

import java.util.List;

public class BdContained {
    private String id;
    private String resourceType;
    private List<BdCodeableConcept> code;
    private List<BdLocation> location;
    private List<BdCodeableConcept> specialty;

    public BdContained() {
    }

    public BdContained(String resourceType, List<BdCodeableConcept> code, List<BdLocation> location, List<BdCodeableConcept> specialty,String id) {
        this.resourceType = resourceType;
        this.code = code;
        this.location = location;
        this.specialty = specialty;
        this.id=id;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<BdCodeableConcept> getCode() {
        return code;
    }

    public void setCode(List<BdCodeableConcept> code) {
        this.code = code;
    }

    public List<BdLocation> getLocation() {
        return location;
    }

    public void setLocation(List<BdLocation> location) {
        this.location = location;
    }

    public List<BdCodeableConcept> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<BdCodeableConcept> specialty) {
        this.specialty = specialty;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
