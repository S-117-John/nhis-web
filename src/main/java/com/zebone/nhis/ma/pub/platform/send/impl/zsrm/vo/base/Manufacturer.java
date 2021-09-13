package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;

public class Manufacturer {

    private List<Identifier> identifier;
    private String name;

    public Manufacturer() {
    }

    public Manufacturer(List<Identifier> identifier, String name) {
        this.identifier = identifier;
        this.name = name;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}