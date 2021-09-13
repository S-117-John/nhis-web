package com.zebone.nhis.common.module.pi;

import com.zebone.nhis.common.module.BaseModule;
import com.zebone.platform.modules.dao.build.au.Field;
import com.zebone.platform.modules.dao.build.au.PK;
import com.zebone.platform.modules.dao.build.au.Table;

import java.util.Date;

@Table(value="PI_LOCK_RECORD")
public class PiLockRecord extends BaseModule {

    @PK
    @Field(value="PK_PILOCKREC",id= Field.KeyId.UUID)
    private String pkPilockrec;
    @Field(value="PK_PI")
    private String pkPi;
    @Field(value="PK_PV")
    private String pkPv;
    @Field(value="EU_LOCKTYPE")
    private String euLocktype;
    @Field(value="EU_STATUS")
    private String euStatus;
    @Field(value="DATE_RECORD")
    private Date dateRecord;
    @Field(value="PK_PILOCK")
    private String piLock;
    @Field(value="CODE")
    private String code;

    public String getPkPilockrec() {
        return pkPilockrec;
    }

    public void setPkPilockrec(String pkPilockrec) {
        this.pkPilockrec = pkPilockrec;
    }

    public String getPkPi() {
        return pkPi;
    }

    public void setPkPi(String pkPi) {
        this.pkPi = pkPi;
    }

    public String getPkPv() {
        return pkPv;
    }

    public void setPkPv(String pkPv) {
        this.pkPv = pkPv;
    }

    public String getEuLocktype() {
        return euLocktype;
    }

    public void setEuLocktype(String euLocktype) {
        this.euLocktype = euLocktype;
    }

    public String getEuStatus() {
        return euStatus;
    }

    public void setEuStatus(String euStatus) {
        this.euStatus = euStatus;
    }

    public Date getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(Date dateRecord) {
        this.dateRecord = dateRecord;
    }

    public String getPiLock() {
        return piLock;
    }

    public void setPiLock(String piLock) {
        this.piLock = piLock;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
