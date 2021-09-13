package com.zebone.nhis.pro.zsrm.sch.vo;

import java.util.Date;

public class PiBlackVo {

    private String pkPi;

    private String pkOrg;

    private String qryType;

    private String euLockType;

    private Date OccurredTime;

    private String note;

    private String pkPv;

    private String pkRecordStr;

    private String pkPilock;

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkOrg() {
        return pkOrg;
    }

    public void setPkOrg(String pkOrg) {
        this.pkOrg = pkOrg;
    }

    public String getQryType() {
        return qryType;
    }

    public void setQryType(String qryType) {
        this.qryType = qryType;
    }

    public String getEuLockType() {
        return euLockType;
    }

    public void setEuLockType(String euLockType) {
        this.euLockType = euLockType;
    }

    public Date getOccurredTime() {
        return OccurredTime;
    }

    public void setOccurredTime(Date occurredTime) {
        OccurredTime = occurredTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getPkRecordStr() {
        return pkRecordStr;
    }

    public void setPkRecordStr(String pkRecordStr) {
        this.pkRecordStr = pkRecordStr;
    }

    public String getPkPilock() {
        return pkPilock;
    }

    public void setPkPilock(String pkPilock) {
        this.pkPilock = pkPilock;
    }
}
