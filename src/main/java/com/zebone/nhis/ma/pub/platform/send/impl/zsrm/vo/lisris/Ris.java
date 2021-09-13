package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;

import java.util.Date;
import java.util.List;

/**
 * 检查
 */
public class Ris extends PhResource {

    private String  status;

    private String intent;

    private List<Category> category;

    private Category code;

    private Locationlis subject;

    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private Date effectiveDateTime;

    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private Date issued;

    private List<Locationlis> performer;

    private List<Specimen> specimen;


    private  Encounter encounter;

    private List<ResultRis> result;

    private List<Media> media;

    private List<ReasonReference> reasonReference;

    private List<TextElement> note;

    private List<RelevantHistory> relevantHistory;

    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private  Date authoredOn;

    private Locationlis requester;

    private List<Code> locationCode;

    private List<Code> bodySite;

    public List<Code> getBodySite() {
        return bodySite;
    }

    public void setBodySite(List<Code> bodySite) {
        this.bodySite = bodySite;
    }

    public Date getAuthoredOn() {
        return authoredOn;
    }

    public void setAuthoredOn(Date authoredOn) {
        this.authoredOn = authoredOn;
    }

    public Locationlis getRequester() {
        return requester;
    }

    public void setRequester(Locationlis requester) {
        this.requester = requester;
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

    public List<RelevantHistory> getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(List<RelevantHistory> relevantHistory) {
        this.relevantHistory = relevantHistory;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Date getEffectiveDateTime() {
        return effectiveDateTime;
    }

    public void setEffectiveDateTime(Date effectiveDateTime) {
        this.effectiveDateTime = effectiveDateTime;
    }

    public Date getIssued() {
        return issued;
    }

    public void setIssued(Date issued) {
        this.issued = issued;
    }

    public List<Locationlis> getPerformer() {
        return performer;
    }

    public void setPerformer(List<Locationlis> performer) {
        this.performer = performer;
    }

    public List<Specimen> getSpecimen() {
        return specimen;
    }

    public void setSpecimen(List<Specimen> specimen) {
        this.specimen = specimen;
    }


    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public List<ResultRis> getResult() {
        return result;
    }

    public void setResult(List<ResultRis> result) {
        this.result = result;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
