package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;

public class Location {

    private String resourceType;
    private String name;
    private List<Identifier> identifier;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
}