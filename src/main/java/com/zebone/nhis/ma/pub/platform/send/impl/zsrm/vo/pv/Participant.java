package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;

import java.util.List;

public class Participant {

    private List<CodeableConcept> type;

    public void setType(List<CodeableConcept> type) {
        this.type = type;
    }

    public List<CodeableConcept> getType() {
        return type;
    }

}