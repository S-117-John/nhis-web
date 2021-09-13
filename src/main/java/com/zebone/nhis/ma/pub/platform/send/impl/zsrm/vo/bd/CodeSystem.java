package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;

import java.util.List;

/**
 * 诊断
 */
public class CodeSystem extends Outcome {
    private List<Concept> concept;

    public List<Concept> getConcept() {
        return concept;
    }

    public void setConcept(List<Concept> concept) {
        this.concept = concept;
    }
}
