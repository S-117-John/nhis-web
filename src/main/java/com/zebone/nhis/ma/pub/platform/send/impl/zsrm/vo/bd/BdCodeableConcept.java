package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;

import java.util.List;

public class BdCodeableConcept {

    public BdCodeableConcept(){}

    public BdCodeableConcept(List<BdCoding> coding) {
        this.coding = coding;
    }

    public BdCodeableConcept(String text, List<BdCoding> coding) {
        this.coding = coding;
        this.text = text;
    }

    private List<BdCoding> coding;
    private String text;
    public List<BdCoding> getCoding() {
        return coding;
    }

    public void setCoding(List<BdCoding> coding) {
        this.coding = coding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
