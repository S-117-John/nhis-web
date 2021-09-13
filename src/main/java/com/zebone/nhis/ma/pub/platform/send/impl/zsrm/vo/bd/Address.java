package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;

import java.util.List;

/**
 * 地址信息
 */
public class Address {

    public Address() {
    }

    public Address(String text) {
        this.text = text;
    }

    public Address(String text, String resourceType, List<Identifier> identifier) {
        this.text = text;
        this.resourceType = resourceType;
        this.identifier = identifier;
    }

    private String text;
    private String resourceType;
    private List<Identifier> identifier;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
