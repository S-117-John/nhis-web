package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bl.charge;

import java.util.List;

public class BlCodeableConcept {

    public BlCodeableConcept(){}

    public BlCodeableConcept(List<BlCoding> coding) {
        this.coding = coding;
    }

    public BlCodeableConcept(String text, List<BlCoding> coding) {
        this.coding = coding;
        this.text = text;
    }

    private List<BlCoding> coding;
    private String text;
    public List<BlCoding> getCoding() {
        return coding;
    }

    public void setCoding(List<BlCoding> coding) {
        this.coding = coding;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
