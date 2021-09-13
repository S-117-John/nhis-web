package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

import java.util.List;

import java.util.Date;

public class Other {

    private String resourceType;
    private List<Identifier> identifier;

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

    public Other() {
    }

    public Other(String resourceType, List<Identifier> identifier) {
        this.resourceType = resourceType;
        this.identifier = identifier;
    }
}
