package com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.cn;

import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Identifier;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.Response;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.base.TextElement;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.Encounter;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.lisris.EncounterBact;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.MzRecordSubject;
import com.zebone.nhis.ma.pub.platform.send.impl.zsrm.vo.patient.Patient;

import java.util.List;
import java.util.Map;

/**
 * @date 2021/05/21 15:41
 **/
public class MzRecordInfoResponse extends Response {
    private String status;
    private TextElement diagnose;
    private TextElement disrecord;
    private EncounterBact encounter;
    private List<Identifier> identifier;
    private TextElement pasthistory;
    private MzRecordSubject subject;
    private String visitTime;

    public String getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(String visitTime) {
        this.visitTime = visitTime;
    }

    public MzRecordSubject getSubject() {
        return subject;
    }

    public void setSubject(MzRecordSubject subject) {
        this.subject = subject;
    }

    public TextElement getPasthistory() {
        return pasthistory;
    }

    public void setPasthistory(TextElement pasthistory) {
        this.pasthistory = pasthistory;
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public EncounterBact getEncounter() {
        return encounter;
    }

    public void setEncounter(EncounterBact encounter) {
        this.encounter = encounter;
    }

    public TextElement getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(TextElement diagnose) {
        this.diagnose = diagnose;
    }

    public TextElement getDisrecord() {
        return disrecord;
    }

    public void setDisrecord(TextElement disrecord) {
        this.disrecord = disrecord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
