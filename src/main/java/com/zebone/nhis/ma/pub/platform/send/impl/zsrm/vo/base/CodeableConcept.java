package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base;

import java.util.List;

public class CodeableConcept {

    public CodeableConcept(){}

    public CodeableConcept(List<Coding> coding) {
        this.coding = coding;
    }

    private List<Coding> coding;

    public void setCoding(List<Coding> coding) {
        this.coding = coding;
    }

    public List<Coding> getCoding() {
        return coding;
    }


}