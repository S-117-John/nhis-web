package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

import java.util.List;

public class BdLocation {
    private List<Identifier> identifier;
    private String display;
    private String resourceType;

    public BdLocation() {
    }

    public BdLocation(String resourceType, List<Identifier> identifier, String display) {
        this.identifier = identifier;
        this.display = display;
        this.resourceType = resourceType;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
