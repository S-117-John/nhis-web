package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.bd;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.CodeableConcept;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Outcome;

import java.util.List;

/**
 * 诊疗项目消息--医嘱项目
 */
public class bdOrdSubstance extends Outcome {
    private String status;
    private CodeableConcept code;
    private List<CodeableConcept> category;
    private List<CodeableConcept> substanceCodeableConcept;
    private List<BdExtension> extension;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public CodeableConcept getCode() {
        return code;
    }

    public void setCode(CodeableConcept code) {
        this.code = code;
    }

    public List<CodeableConcept> getCategory() {
        return category;
    }

    public void setCategory(List<CodeableConcept> category) {
        this.category = category;
    }

    public List<CodeableConcept> getSubstanceCodeableConcept() {
        return substanceCodeableConcept;
    }

    public void setSubstanceCodeableConcept(List<CodeableConcept> substanceCodeableConcept) {
        this.substanceCodeableConcept = substanceCodeableConcept;
    }

    public List<BdExtension> getExtension() {
        return extension;
    }

    public void setExtension(List<BdExtension> extension) {
        this.extension = extension;
    }
}
