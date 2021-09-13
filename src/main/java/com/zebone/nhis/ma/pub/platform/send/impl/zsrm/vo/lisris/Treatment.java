package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Extension;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Specimen;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;

import java.util.Date;
import java.util.List;

/**
 * 治疗
 */
public class Treatment extends PhResource {

    private String  status;

    private String intent;

    private List<Category> category;

    private Category code;

    private Locationlis subject;

    private  Encounter encounter;

    private List<Locationlis> performer;

    private List<Code> locationCode;

    private List<ReasonReference> reasonReference;

    private List<TextElement> note;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public List<Category> getCategory() {
        return category;
    }

    public void setCategory(List<Category> category) {
        this.category = category;
    }

    public Category getCode() {
        return code;
    }

    public void setCode(Category code) {
        this.code = code;
    }

    public Locationlis getSubject() {
        return subject;
    }

    public void setSubject(Locationlis subject) {
        this.subject = subject;
    }

    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public List<Locationlis> getPerformer() {
        return performer;
    }

    public void setPerformer(List<Locationlis> performer) {
        this.performer = performer;
    }

    public List<Code> getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(List<Code> locationCode) {
        this.locationCode = locationCode;
    }

    public List<ReasonReference> getReasonReference() {
        return reasonReference;
    }

    public void setReasonReference(List<ReasonReference> reasonReference) {
        this.reasonReference = reasonReference;
    }

    public List<TextElement> getNote() {
        return note;
    }

    public void setNote(List<TextElement> note) {
        this.note = note;
    }

}
