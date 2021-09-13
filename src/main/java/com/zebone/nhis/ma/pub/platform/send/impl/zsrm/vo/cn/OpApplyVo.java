package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Coding;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.PhResource;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Specimen;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.*;

import java.util.Date;
import java.util.List;

/**
 * 手术
 */
public class OpApplyVo extends PhResource {

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



    private  Encounter encounter;

    private Locationlis requester;

    private List<Coding> locationCode;

    private List<ReasonReference> reasonReference;

    private List<TextElement> note;

    private List<RelevantHistory> relevantHistory;


    @JSONField(format="yyyy-MM-dd HH:mm:ss.SSS")
    private  Date authoredOn;

    private String priority;

    private List<Code> bodySite;

    public List<Code> getBodySite() {
        return bodySite;
    }

    public void setBodySite(List<Code> bodySite) {
        this.bodySite = bodySite;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public List<RelevantHistory> getRelevantHistory() {
        return relevantHistory;
    }

    public void setRelevantHistory(List<RelevantHistory> relevantHistory) {
        this.relevantHistory = relevantHistory;
    }

    public List<TextElement> getNote() {
        return note;
    }

    public void setNote(List<TextElement> note) {
        this.note = note;
    }

    public List<ReasonReference> getReasonReference() {
        return reasonReference;
    }

    public void setReasonReference(List<ReasonReference> reasonReference) {
        this.reasonReference = reasonReference;
    }

    public List<Coding> getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(List<Coding> locationCode) {
        this.locationCode = locationCode;
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

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    private List<Result> result;

    private List<Media> media;

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


    public Encounter getEncounter() {
        return encounter;
    }

    public void setEncounter(Encounter encounter) {
        this.encounter = encounter;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }
}
