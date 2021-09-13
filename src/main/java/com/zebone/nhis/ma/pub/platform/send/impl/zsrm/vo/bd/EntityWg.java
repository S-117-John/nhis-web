package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

import java.util.List;

public class EntityWg {

    private String resourceType;
    private List<Identifier> practitioner;
    private List<Identifier> identifier;

    public EntityWg() {
    }

    public EntityWg(String resourceType, List<Identifier> practitioner, List<Identifier> identifier) {
        this.resourceType = resourceType;
        this.practitioner = practitioner;
        this.identifier = identifier;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public List<Identifier> getPractitioner() {
        return practitioner;
    }

    public void setPractitioner(List<Identifier> practitioner) {
        this.practitioner = practitioner;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }
}