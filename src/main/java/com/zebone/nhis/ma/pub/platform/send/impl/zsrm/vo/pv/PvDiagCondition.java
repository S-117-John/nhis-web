package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.pv;

import com.alibaba.fastjson.annotation.JSONField;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.common.ZsphJsonDateDeserializer;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.*;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

import java.util.Date;
import java.util.List;

/**
 * 患者诊断Condition信息节点
 */
public class PvDiagCondition extends PhResource {
    private List<CodeableConcept> category;
    private CodeableConcept code;
    private ConditionRecorder subject;
    private ConditionRecorder  encounter;
    @JsonDeserialize(using = ZsphJsonDateDeserializer.class)
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date recordedDate;
    private ConditionRecorder recorder;

    public List<CodeableConcept> getCategory() {
        return category;
    }

    public void setCategory(List<CodeableConcept> category) {
        this.category = category;
    }

    public CodeableConcept getCode() {
        return code;
    }

    public void setCode(CodeableConcept code) {
        this.code = code;
    }

    public ConditionRecorder getSubject() {
        return subject;
    }

    public void setSubject(ConditionRecorder subject) {
        this.subject = subject;
    }

    public ConditionRecorder getEncounter() {
        return encounter;
    }

    public void setEncounter(ConditionRecorder encounter) {
        this.encounter = encounter;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public ConditionRecorder getRecorder() {
        return recorder;
    }

    public void setRecorder(ConditionRecorder recorder) {
        this.recorder = recorder;
    }
}
