package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Condition;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;

public class Diagnosis {

    private Condition condition;
    private CodeableConcept use;
    private String rank;

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setUse(CodeableConcept use) {
        this.use = use;
    }

    public CodeableConcept getUse() {
        return use;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

}